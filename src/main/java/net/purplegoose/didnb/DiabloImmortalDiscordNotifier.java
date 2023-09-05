package net.purplegoose.didnb;

import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.purplegoose.didnb.annotations.GameDataCacheSet;
import net.purplegoose.didnb.cache.*;
import net.purplegoose.didnb.data.ScheduledEventsSetting;
import net.purplegoose.didnb.database.DatabaseRequests;
import net.purplegoose.didnb.database.MySQLDatabaseConnection;
import net.purplegoose.didnb.enums.Language;
import net.purplegoose.didnb.events.*;
import net.purplegoose.didnb.exeption.StartFailureException;
import net.purplegoose.didnb.notifier.ScheduledEventCreator;
import net.purplegoose.didnb.notifier.CustomMessagesNotifier;
import net.purplegoose.didnb.notifier.InformationNotifier;
import net.purplegoose.didnb.notifier.Notifier;
import net.purplegoose.didnb.utils.ConfigUtil;

import java.lang.reflect.Field;
import java.sql.SQLException;
import java.util.Arrays;

@Slf4j
public class DiabloImmortalDiscordNotifier {

    private static boolean registerCommands = true;

    private static GameDataCache gameDataCache = new GameDataCache();
    private static GuildsCache guildsCache = new GuildsCache();
    private static CustomMessagesCache customMessagesCache = new CustomMessagesCache();
    private static ScheduledEventsSettingsCache sesCache = new ScheduledEventsSettingsCache();

    private static MySQLDatabaseConnection mySQLDatabaseConnection = new MySQLDatabaseConnection();
    private static DatabaseRequests databaseRequests = new DatabaseRequests(mySQLDatabaseConnection, customMessagesCache);

    public static void main(String[] args) {

        // Init caches
        gameDataCache = new GameDataCache();
        guildsCache = new GuildsCache();
        customMessagesCache = new CustomMessagesCache();
        sesCache = new ScheduledEventsSettingsCache();

        // Init database
        mySQLDatabaseConnection = new MySQLDatabaseConnection();
        databaseRequests = new DatabaseRequests(mySQLDatabaseConnection, customMessagesCache);

        fillCaches();
        setCommandRegistrationValue(args);

        JDA jda = getJdaClient();

        // Run scheduler
        runScheduler(gameDataCache, guildsCache, databaseRequests, jda);
        logContainingLanguages();

        checkForMissingSESetting(guildsCache, databaseRequests);
    }

    private static void setCommandRegistrationValue(String[] args) {
        if (isArgumentsPresent(args)) {
            registerCommands = Boolean.parseBoolean(args[0]);
        }
    }

    private static void fillCaches() {
        try {
            loadGameDataCache(gameDataCache, databaseRequests);
            fillGuildsCache(guildsCache, databaseRequests);
            fillGameDataCache(gameDataCache, databaseRequests);
        } catch (SQLException e) {
            throw new StartFailureException("Failed to load caches.", e);
        }
    }

    private static boolean isArgumentsPresent(String[] args) {
        return args.length > 0;
    }

    private static JDA getJdaClient() {
        try {
            JDABuilder jdaBuilder = JDABuilder.createDefault(ConfigUtil.getClientToken());
            addEventListeners(jdaBuilder);
            addEventInteractions(jdaBuilder);
            return jdaBuilder.build().awaitReady();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new StartFailureException("Failed to build jda client.", e);
        }
    }

    private static void addEventListeners(JDABuilder jdaBuilder) {
        jdaBuilder.addEventListeners(new ChannelDelete(databaseRequests, guildsCache),
                new GuildJoin(databaseRequests, guildsCache),
                new GuildLeave(databaseRequests, guildsCache));

        if (registerCommands) {
            jdaBuilder.addEventListeners(new GuildReady());
        }

    }

    private static void addEventInteractions(JDABuilder jdaBuilder) {
        jdaBuilder.addEventListeners(new SlashCommandInteraction(guildsCache, databaseRequests, gameDataCache, customMessagesCache, sesCache),
                new StringSelectInteraction(guildsCache, databaseRequests));
    }


    private static boolean loadGameDataCache(GameDataCache gameDataCache, DatabaseRequests databaseRequests) throws SQLException {
        gameDataCache.getGameDataCache().addAll(databaseRequests.loadGameEventData());
        return true;
    }

    private static boolean fillGuildsCache(GuildsCache guildsCache, DatabaseRequests databaseRequests) throws SQLException {
        guildsCache.setGuilds(databaseRequests.loadDataFromDatabaseToCache());
        return true;
    }

    //todo: remove
    private static void checkForMissingSESetting(GuildsCache guildsCache, DatabaseRequests databaseRequests) {
        guildsCache.getAllGuilds().values().forEach(clientGuild -> {
            if (clientGuild.getSeSetting() == null) {
                ScheduledEventsSetting seSetting = new ScheduledEventsSetting(clientGuild.getGuildID());
                clientGuild.setSeSetting(seSetting);
                databaseRequests.createScheduledEventsSettings(seSetting.getGuildID());
            }
        });
    }

    private static void runScheduler(GameDataCache gameDataCache, GuildsCache guildsCache, DatabaseRequests databaseRequests, JDA jda) {
        Notifier notifier = new Notifier(guildsCache, gameDataCache, new ErrorCache(databaseRequests));
        notifier.runNotificationScheduler(jda);

        CustomMessagesNotifier customMessagesNotifier = new CustomMessagesNotifier(databaseRequests, guildsCache);
        customMessagesNotifier.runCustomMessagesNotifier(jda);

        InformationNotifier informationNotifier = new InformationNotifier();
        informationNotifier.runInformationNotifier(jda);

        ScheduledEventCreator seCreator = new ScheduledEventCreator(guildsCache, gameDataCache);
        seCreator.scheduler(jda);
    }

    private static boolean fillGameDataCache(GameDataCache gameDataCache, DatabaseRequests databaseRequests) {
        for (Field s : GameDataCache.class.getDeclaredFields()) {
            if (!s.isAnnotationPresent(GameDataCacheSet.class)) continue;
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
