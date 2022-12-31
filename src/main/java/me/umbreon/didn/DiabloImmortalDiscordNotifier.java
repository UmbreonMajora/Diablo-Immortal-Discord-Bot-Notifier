package me.umbreon.didn;


import me.umbreon.didn.cache.GameDataCache;
import me.umbreon.didn.cache.GuildsCache;
import me.umbreon.didn.database.DatabaseRequests;
import me.umbreon.didn.database.MySQLDatabaseConnection;
import me.umbreon.didn.events.*;
import me.umbreon.didn.logger.FileLogger;
import me.umbreon.didn.notifier.CustomMessagesNotifier;
import me.umbreon.didn.notifier.InformationNotifier;
import me.umbreon.didn.notifier.Notifier;
import me.umbreon.didn.utils.ConfigUtil;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;

import java.sql.SQLException;

public class DiabloImmortalDiscordNotifier {
    /*
     * TODOS:
     * TheGreatMax — Today at 8:08 AM
     * Perfection
     * The other major thing that people was talking about is the possibility of implementing DST time zones as well.
     * Or just a toggle switch for Daylight Savings Time on/off
     * Right now i'm on a DST-7 server but its actually GMT-8 now.
     *
     * Yeah. I believe the easiest method is just a /config DaylightTime on/off and just make it adjust the time by an hour.
     * other then that its gonna get complex as crap.
     * meaning they turn it on during the winter and off during the summer.
     */

    /*
     - Fixes:
     - upcoming & today command no working
     - embedded message where removed (re-add after all testing)
     - languages not implemented.
     - Suggestions:
     - /editcustommessge
     - multiple warn-time-messages for example: 5 minutes & 10 minutes.
     */

    public static void main(String[] args) {
        GameDataCache gameDataCache = new GameDataCache();
        GuildsCache guildsCache = new GuildsCache();

        MySQLDatabaseConnection mySQLDatabaseConnection = new MySQLDatabaseConnection();
        DatabaseRequests databaseRequests = new DatabaseRequests(mySQLDatabaseConnection);
        try {
            guildsCache.setGuilds(databaseRequests.loadDataFromDatabaseToCache());
        } catch (SQLException e) {
            e.printStackTrace();
            FileLogger.createClientFileLog("Failed to run. Loading cache failed.");
            System.exit(0);
            return;
        }

        fillGameDataCache(gameDataCache, databaseRequests);

        JDA jda;
        try {
            jda = JDABuilder.createDefault(ConfigUtil.getClientToken())

                    .addEventListeners(new ChannelDelete(databaseRequests, guildsCache))
                    .addEventListeners(new GuildJoin())
                    .addEventListeners(new GuildReady())
                    .addEventListeners(new MessageDelete(databaseRequests, guildsCache))
                    .addEventListeners(new MessageReactionAdd(guildsCache, databaseRequests))
                    .addEventListeners(new MessageReactionRemove(guildsCache))
                    .addEventListeners(new SlashCommandInteraction(guildsCache, databaseRequests))

                    .build().awaitReady();
        } catch (InterruptedException e) {
            FileLogger.createClientFileLog("Failed to run. Building JDA failed.");
            System.exit(0);
            return;
        }

        runScheduler(gameDataCache, guildsCache, databaseRequests, jda);
    }

    private static void runScheduler(GameDataCache gameDataCache, GuildsCache guildsCache, DatabaseRequests databaseRequests, JDA jda) {
        Notifier notifier = new Notifier(guildsCache, gameDataCache);
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

}
