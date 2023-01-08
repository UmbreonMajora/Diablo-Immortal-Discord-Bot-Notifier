package net.purplegoose.didnb;

import net.purplegoose.didnb.cache.ErrorCache;
import net.purplegoose.didnb.cache.GameDataCache;
import net.purplegoose.didnb.cache.GuildsCache;
import net.purplegoose.didnb.data.ClientGuild;
import net.purplegoose.didnb.data.NotificationChannel;
import net.purplegoose.didnb.database.DatabaseRequests;
import net.purplegoose.didnb.database.MySQLDatabaseConnection;
import net.purplegoose.didnb.events.*;
import net.purplegoose.didnb.notifier.CustomMessagesNotifier;
import net.purplegoose.didnb.notifier.InformationNotifier;
import net.purplegoose.didnb.notifier.Notifier;
import net.purplegoose.didnb.utils.ConfigUtil;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;

import java.sql.SQLException;

public class DiabloImmortalDiscordNotifier {

    public static void main(String[] args) {
        GameDataCache gameDataCache = new GameDataCache();
        GuildsCache guildsCache = new GuildsCache();

        MySQLDatabaseConnection mySQLDatabaseConnection = new MySQLDatabaseConnection();
        DatabaseRequests databaseRequests = new DatabaseRequests(mySQLDatabaseConnection);
        try {
            guildsCache.setGuilds(databaseRequests.loadDataFromDatabaseToCache());
        } catch (SQLException e) {
            e.printStackTrace();
            System.exit(0);
            return;
        }

        fillGameDataCache(gameDataCache, databaseRequests);

        JDA jda;
        try {
            jda = JDABuilder.createDefault(ConfigUtil.getClientToken())

                    .addEventListeners(new ChannelDelete(databaseRequests, guildsCache))
                    .addEventListeners(new GuildJoin(databaseRequests, guildsCache))
                    .addEventListeners(new SlashCommandInteraction(guildsCache, databaseRequests, gameDataCache))
                    .addEventListeners(new GuildLeave(databaseRequests, guildsCache))

                    .build().awaitReady();

            jda.addEventListener(new GuildReady());
        } catch (InterruptedException e) {
            System.exit(0);
            return;
        }

        createShowcaseChannels(guildsCache);
        runScheduler(gameDataCache, guildsCache, databaseRequests, jda);
    }

    private static void runScheduler(GameDataCache gameDataCache, GuildsCache guildsCache, DatabaseRequests databaseRequests, JDA jda) {
        Notifier notifier = new Notifier(guildsCache, gameDataCache, new ErrorCache(databaseRequests));
        notifier.runNotificationScheduler(jda);

        CustomMessagesNotifier customMessagesNotifier = new CustomMessagesNotifier(databaseRequests, guildsCache);
        customMessagesNotifier.runCustomMessagesNotifier(jda);

        InformationNotifier informationNotifier = new InformationNotifier();
        informationNotifier.runInformationNotifier(jda);
    }

    private static void fillGameDataCache(GameDataCache gameDataCache, DatabaseRequests databaseRequests) {
        gameDataCache.setShadowLotteryDataSet(databaseRequests.getEventTimes("shadow_lottery_times", true));
        gameDataCache.setVaultGameDataSet(databaseRequests.getEventTimes("vault_times", true));
        gameDataCache.setHauntedCarriageDataSet(databaseRequests.getEventTimes("haunted_carriage_times", false));
        gameDataCache.setDemonGatesDataSet(databaseRequests.getEventTimes("demon_gates_times", false));
        gameDataCache.setBattlegroundDataSet(databaseRequests.getEventTimes("battleground_times", true));
        gameDataCache.setAncientArenaDataSet(databaseRequests.getEventTimes("ancient_arena_times", false));
        gameDataCache.setAncientNightmareDataSet(databaseRequests.getEventTimes("ancient_nightmare_times", false));
        gameDataCache.setAssemblyDataSet(databaseRequests.getEventTimes("assembly_times", false));
        gameDataCache.setWrathborneInvasionDataSet(databaseRequests.getEventTimes("wrathborne_invasion_times", true));
        gameDataCache.setOnSlaughtDataSet(databaseRequests.getEventTimes("onslaught_times", false));
    }

    private static void createShowcaseChannels(GuildsCache guildsCache) {
        ClientGuild gmt0Guild = new ClientGuild("0");
        gmt0Guild.setHeadUpTime(10);
        NotificationChannel gmt0Channel = new NotificationChannel("1061561889739522058", "0");
        gmt0Channel.setRoleID(null);
        gmt0Guild.addNewNotificationChannel(gmt0Channel);
        guildsCache.addGuild(gmt0Guild);

        ClientGuild gmt1Guild = new ClientGuild("0");
        gmt1Guild.setHeadUpTime(10);
        NotificationChannel gmt1Channel = new NotificationChannel("1061561926804574258", "0");
        gmt1Channel.setRoleID(null);
        gmt1Guild.addNewNotificationChannel(gmt1Channel);
        guildsCache.addGuild(gmt1Guild);
    }

}
