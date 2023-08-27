package net.purplegoose.didnb;

import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.purplegoose.didnb.annotations.GameDataCacheSet;
import net.purplegoose.didnb.cache.CustomMessagesCache;
import net.purplegoose.didnb.cache.ErrorCache;
import net.purplegoose.didnb.cache.GameDataCache;
import net.purplegoose.didnb.cache.GuildsCache;
import net.purplegoose.didnb.database.DatabaseRequests;
import net.purplegoose.didnb.database.MySQLDatabaseConnection;
import net.purplegoose.didnb.enums.Language;
import net.purplegoose.didnb.events.*;
import net.purplegoose.didnb.notifier.CustomMessagesNotifier;
import net.purplegoose.didnb.notifier.InformationNotifier;
import net.purplegoose.didnb.notifier.Notifier;
import net.purplegoose.didnb.utils.ConfigUtil;

import java.lang.reflect.Field;
import java.sql.SQLException;
import java.util.Arrays;

@Slf4j
public class DiabloImmortalDiscordNotifier {

    public static void main(String[] args) {
        boolean registerCommandsOnGuildReady = false;
        if (args.length > 0) {
            registerCommandsOnGuildReady = Boolean.parseBoolean(args[0]);
        }
        // Init caches
        GameDataCache gameDataCache = new GameDataCache();
        GuildsCache guildsCache = new GuildsCache();
        CustomMessagesCache customMessagesCache = new CustomMessagesCache();
        // Init database
        MySQLDatabaseConnection mySQLDatabaseConnection = new MySQLDatabaseConnection();
        DatabaseRequests databaseRequests = new DatabaseRequests(mySQLDatabaseConnection, customMessagesCache);
        // Fill caches
        if (!fillGameDataCache(gameDataCache, databaseRequests)) return;
        if (!fillGuildsCache(guildsCache, databaseRequests)) return;
        // Register events
        JDA jda = registerEventListeners(databaseRequests, guildsCache, gameDataCache, customMessagesCache, registerCommandsOnGuildReady);
        // Run scheduler
        runScheduler(gameDataCache, guildsCache, databaseRequests, jda);
        logContainingLanguages();
    }

    private static JDA registerEventListeners(DatabaseRequests databaseRequests, GuildsCache guildsCache,
                                              GameDataCache gameDataCache, CustomMessagesCache customMessagesCache,
                                              boolean registerCommandsOnGuildReady) {
        JDA jda;
        try {
            JDABuilder jdaBuilder = JDABuilder.createDefault(ConfigUtil.getClientToken())
                    .addEventListeners(new ChannelDelete(databaseRequests, guildsCache))
                    .addEventListeners(new GuildJoin(databaseRequests, guildsCache))
                    .addEventListeners(new SlashCommandInteraction(guildsCache, databaseRequests, gameDataCache, customMessagesCache))
                    .addEventListeners(new GuildLeave(databaseRequests, guildsCache));

            if (registerCommandsOnGuildReady) jdaBuilder.addEventListeners(new GuildReady());

            jda = jdaBuilder.build().awaitReady();
        } catch (InterruptedException e) {
            log.error("Failed to register event listeners.", e);
            return null;
        }
        return jda;
    }

    private static boolean fillGuildsCache(GuildsCache guildsCache, DatabaseRequests databaseRequests) {
        try {
            guildsCache.setGuilds(databaseRequests.loadDataFromDatabaseToCache());
            return true;
        } catch (SQLException e) {
            log.error("Failed to load guilds cache.", e);
            return false;
        }
    }

    private static void runScheduler(GameDataCache gameDataCache, GuildsCache guildsCache, DatabaseRequests databaseRequests, JDA jda) {
        Notifier notifier = new Notifier(guildsCache, gameDataCache, new ErrorCache(databaseRequests));
        notifier.runNotificationScheduler(jda);

        CustomMessagesNotifier customMessagesNotifier = new CustomMessagesNotifier(databaseRequests, guildsCache);
        customMessagesNotifier.runCustomMessagesNotifier(jda);

        InformationNotifier informationNotifier = new InformationNotifier();
        informationNotifier.runInformationNotifier(jda);
    }

    private static boolean fillGameDataCache(GameDataCache gameDataCache, DatabaseRequests databaseRequests) {
        for (Field s : GameDataCache.class.getDeclaredFields()) {
            GameDataCacheSet gameDataCacheSet = s.getAnnotation(GameDataCacheSet.class);
            s.setAccessible(true);
            try {
                String tableName = gameDataCacheSet.tableName();
                boolean everyday = gameDataCacheSet.everyday();
                s.set(gameDataCache, databaseRequests.getEventTimes(tableName, everyday));
            } catch (IllegalAccessException e) {
                log.error("Failed to load event game data cache.", e);
                return false;
            }
        }
        return true;
    }

    private static void logContainingLanguages() {
        log.info("Loaded with these languages: " + Arrays.toString(Language.values()));
    }

}
