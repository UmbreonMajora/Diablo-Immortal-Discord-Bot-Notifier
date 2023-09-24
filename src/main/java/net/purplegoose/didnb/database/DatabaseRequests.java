package net.purplegoose.didnb.database;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.purplegoose.didnb.cache.CustomMessagesCache;
import net.purplegoose.didnb.data.*;
import net.purplegoose.didnb.enums.GameEvent;
import net.purplegoose.didnb.enums.Language;
import net.purplegoose.didnb.enums.Weekday;
import net.purplegoose.didnb.news.dto.ArticleDTO;
import net.purplegoose.didnb.news.dto.NewsChannelDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.DayOfWeek;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@AllArgsConstructor
@Slf4j
public class DatabaseRequests {

    private static final String SCHEDULED_EVENTS_SETTINGS_TABLE = "scheduled_events_settings";
    private final IDatabaseConnection databaseConnection;
    private final CustomMessagesCache customMessagesCache;

    public HashSet<EventGameData> getEventTimes(String table, boolean everyDay) {
        HashSet<EventGameData> listEventTimeTables = new HashSet<>();
        try (
                Connection connection = databaseConnection.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM " + table)
        ) {
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    String warnRange = resultSet.getString("warn_range");
                    String[] tmp = warnRange.split("-");
                    String eventStartTime = tmp[1];
                    String weekday = everyDay ? null : resultSet.getString("warn_day");
                    EventGameData eventGameData = new EventGameData(warnRange, weekday, eventStartTime);
                    listEventTimeTables.add(eventGameData);
                }
            }
        } catch (SQLException e) {
            log.error(e.getMessage(), e);
        }
        return listEventTimeTables;
    }

    public Set<EventTime> loadGameEventData() throws SQLException {
        Set<EventTime> gameEventData = new HashSet<>();

        try (
                Connection connection = databaseConnection.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM game_event_times")
        ) {
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    String weekdayString = resultSet.getString("weekday");
                    Weekday weekday = Weekday.findWeekdayByRawName(weekdayString);
                    String eventName = resultSet.getString("event_name");
                    GameEvent gameEvent = GameEvent.findGameEventByRawName(eventName);
                    String warnStartTime = resultSet.getString("warn_start_time");
                    String warnEndTime = resultSet.getString("warn_end_time");
                    int startHour = resultSet.getInt("start_hour");
                    int startMinute = resultSet.getInt("start_minute");
                    int endHour = resultSet.getInt("end_hour");
                    int endMinute = resultSet.getInt("end_minute");

                    if (weekday == null) {
                        log.error("Failed to load a game time from database.");
                        continue;
                    }

                    if (weekdayString.equalsIgnoreCase(Weekday.EVERYDAY.rawName)) {
                        for (DayOfWeek day : DayOfWeek.values()) {
                            weekday = Weekday.findWeekdayByRawName(day.toString());
                            gameEventData.add(new EventTime(gameEvent, weekday, warnStartTime, warnEndTime, startHour, startMinute, endHour, endMinute));
                        }
                    } else {
                        gameEventData.add(new EventTime(gameEvent, weekday, warnStartTime, warnEndTime, startHour, startMinute, endHour, endMinute));
                    }
                }
            }
        }
        return gameEventData;
    }

    /**
     * Get all news channels from database.
     *
     * @return list with all news channels in database.
     */
    public List<NewsChannelDTO> getAllNewsChannels() {

    }

    public Map<String, ClientGuild> loadDataFromDatabaseToCache() throws SQLException {
        Map<String, ClientGuild> clientGuildData = new ConcurrentHashMap<>();

        try (
                Connection connection = databaseConnection.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(SQLStatements.GET_ALL_GUILDS_STATEMENT)
        ) {
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    String guildID = resultSet.getString("guildID");

                    String languageShortFormAsString = resultSet.getString("language");
                    Language guildLanguage = Language.findLanguageByShortName(languageShortFormAsString);

                    String timeZone = resultSet.getString("timezone");

                    String adminRoleID = resultSet.getString("admin_role_id");

                    int warnTime = resultSet.getInt("warn_time");

                    boolean isWarnMessagesEnabled = (resultSet.getInt("warn_messages_enabled") == 1);
                    boolean isEventMessagesEnabled = (resultSet.getInt("event_messages_enabled") == 1);
                    boolean isDaylightTimeEnabled = (resultSet.getInt("daylight_time_enabled") == 1);
                    boolean isPremiumServer = (resultSet.getInt("premium_server") == 1);
                    int autoDeleteTimeInHours = resultSet.getInt("auto_delete_time");
                    boolean isAutoDeleteEnabled = (resultSet.getInt("auto_delete_enabled") == 1);
                    long embedLeadTime = resultSet.getLong("embed_lead_time");

                    ClientGuild clientGuild = new ClientGuild(guildID, guildLanguage, timeZone, adminRoleID, warnTime,
                            isWarnMessagesEnabled, isEventMessagesEnabled, isDaylightTimeEnabled, isPremiumServer,
                            autoDeleteTimeInHours, isAutoDeleteEnabled, embedLeadTime, null);
                    clientGuildData.put(guildID, clientGuild);
                }
            }
        }

        try (
                Connection connection = databaseConnection.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(SQLStatements.GET_ALL_CUSTOM_MESSAGES_STATEMENT)
        ) {
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    String guildID = resultSet.getString("guildID");
                    String channelID = resultSet.getString("channelID");
                    String message = resultSet.getString("message");
                    String day = resultSet.getString("day");
                    String time = resultSet.getString("time");
                    boolean repeat = (resultSet.getInt("message_repeat") == 1);
                    String id = resultSet.getString("message_id");
                    boolean enabled = (resultSet.getInt("message_enabled") == 1);
                    customMessagesCache.addIdentifier(id);
                    CustomNotification cn = new CustomNotification(channelID, guildID, message, day, time, id, repeat, enabled);
                    if (clientGuildData.containsKey(guildID)) {
                        clientGuildData.get(guildID).addCustomNotification(cn);
                    } else {
                        log.error("Failed to add custom message with id " + id + " to " + guildID + ".");
                    }
                }
            }
        }

        try (
                Connection connection = databaseConnection.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(SQLStatements.GET_ALL_CHANNELS_STATEMENT)
        ) {
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    String textChannelID = resultSet.getString("textChannelID");
                    String guildID = resultSet.getString("guildID");
                    String roleID = resultSet.getString("roleID");

                    boolean message = (resultSet.getInt("message") == 1);
                    boolean headup = (resultSet.getInt("headUp") == 1);
                    boolean ancientarena = (resultSet.getInt("ancientarena") == 1);
                    boolean ancientnightmare = (resultSet.getInt("ancientnightmare") == 1);
                    boolean assembly = (resultSet.getInt("assembly") == 1);
                    boolean battlegrounds = (resultSet.getInt("battlegrounds") == 1);
                    boolean vault = (resultSet.getInt("vault") == 1);
                    boolean demongates = (resultSet.getInt("demongates") == 1);
                    boolean shadowlottery = (resultSet.getInt("shadowlottery") == 1);
                    boolean hauntedcarriage = (resultSet.getInt("hauntedcarriage") == 1);
                    boolean hauntedcarriageembed = (resultSet.getInt("hauntedcarriageembed") == 1);
                    boolean demongatesembed = (resultSet.getInt("demongatesembed") == 1);
                    boolean ancientnightmareembed = (resultSet.getInt("ancientnightmareembed") == 1);
                    boolean ancientarenaembed = (resultSet.getInt("ancientarenaembed") == 1);
                    boolean wrathborneInvasionEnabled = (resultSet.getInt("wrathborneinvasion") == 1);
                    boolean isOnSlaughtMessagesEnabled = (resultSet.getInt("onslaught_event_enabled") == 1);
                    boolean towerOfVictoryMessagesEnabled = (resultSet.getInt("tower_of_victory_enabled") == 1);

                    NotificationChannel notificationChannel = new NotificationChannel(roleID, guildID, textChannelID,
                            headup, message, assembly, vault, demongates, ancientarena, shadowlottery,
                            battlegrounds, hauntedcarriage, ancientnightmare, demongatesembed, ancientarenaembed,
                            hauntedcarriageembed, ancientnightmareembed, wrathborneInvasionEnabled, isOnSlaughtMessagesEnabled,
                            towerOfVictoryMessagesEnabled, false);

                    if (clientGuildData.containsKey(guildID)) {
                        clientGuildData.get(guildID).addNewNotificationChannel(notificationChannel);
                    } else {
                        log.error("Failed to add notification channel with channel id " + textChannelID + " to " + guildID + ".");
                    }
                }
            }
        }

        try (
                Connection connection = databaseConnection.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(SQLStatements.GET_ALL_SCHEDULED_EVENTS_SETTINGS_STATEMENT)
        ) {
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    String guildID = resultSet.getString("guildID");
                    boolean ancientArena = (resultSet.getInt("ancient_arena") == 1);
                    boolean ancientNightmare = (resultSet.getInt("ancient_nightmare") == 1);
                    boolean assembly = (resultSet.getInt("assembly") == 1);
                    boolean battlegrounds = (resultSet.getInt("battlegrounds") == 1);
                    boolean demonGates = (resultSet.getInt("demon_gates") == 1);
                    boolean hauntedCarriage = (resultSet.getInt("haunted_carriage") == 1);
                    boolean stormpoint = (resultSet.getInt("stormpoint") == 1);
                    boolean shadowLottery = (resultSet.getInt("shadow_lottery") == 1);
                    boolean shadowWar = (resultSet.getInt("shadow_war") == 1);
                    boolean accursedTower = (resultSet.getInt("accursed_tower") == 1);
                    boolean vault = (resultSet.getInt("vault") == 1);
                    boolean wrathborneInvasion = (resultSet.getInt("wrathborne_invasion") == 1);

                    ScheduledEventsSetting seSetting = new ScheduledEventsSetting(guildID, ancientArena,
                            ancientNightmare, assembly, battlegrounds, demonGates, hauntedCarriage, stormpoint,
                            shadowLottery, shadowWar, accursedTower, vault, wrathborneInvasion);

                    if (clientGuildData.containsKey(guildID)) {
                        clientGuildData.get(guildID).setSeSetting(seSetting);
                    } else {
                        log.error("Failed to load ScheduledEventsSettings for guild with id {}.", guildID);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        //todo:
        List<NewsChannelDTO> list = new ArrayList<>();
        try (
                Connection connection = databaseConnection.getConnection();
                PreparedStatement ps = connection.prepareStatement(SQLStatements.GET_ALL_ARTICLES_STATEMENT)
        ) {
            try (ResultSet resultSet = ps.executeQuery()) {
                while (resultSet.next()) {
                    String id = resultSet.getString("channelid");
                    String guildID = resultSet.getString("guildid");
                    boolean isDiabloImmortalNewsEnabled = (resultSet.getInt("di_enabled") == 1);
                    boolean isHeartstoneEnabled = (resultSet.getInt("heartstone_enabled") == 1);

                    if (clientGuildData.containsKey(guildID)) {
                        clientGuildData.get(guildID).addNewNotificationChannel();
                    }


                }
            }
        } catch (SQLException e) {
            log.error(e.getMessage(), e);
        }
        return list;

        return clientGuildData;
    }

    // Client Guild

    public void createGuild(ClientGuild clientGuild) {
        try (
                Connection connection = databaseConnection.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(SQLStatements.CREATE_NEW_GUILD_STATEMENT)
        ) {
            preparedStatement.setString(1, clientGuild.getGuildID());
            preparedStatement.setString(2, clientGuild.getLanguage().shortName);
            preparedStatement.setString(3, clientGuild.getTimeZone());
            preparedStatement.setBoolean(4, clientGuild.isWarnMessagesEnabled());
            preparedStatement.setBoolean(5, clientGuild.isEventMessageEnabled());
            preparedStatement.setBoolean(6, clientGuild.isDaylightTimeEnabled());
            preparedStatement.setBoolean(7, clientGuild.isPremiumServer());
            preparedStatement.setLong(8, clientGuild.getEmbedLeadTime());
            preparedStatement.executeUpdate();
            log.info("Registered new guild in database! GuildID: " + clientGuild.getGuildID());
        } catch (SQLException e) {
            log.error(e.getMessage(), e);
        }
    }

    public void updateGuild(ClientGuild clientGuild) {
        try (
                Connection connection = databaseConnection.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(SQLStatements.UPDATE_GUILD_STATEMENT)
        ) {
            preparedStatement.setString(1, clientGuild.getLanguage().shortName);
            preparedStatement.setString(2, clientGuild.getTimeZone());
            preparedStatement.setBoolean(3, clientGuild.isWarnMessagesEnabled());
            preparedStatement.setBoolean(4, clientGuild.isEventMessageEnabled());
            preparedStatement.setString(5, clientGuild.getGuildAdminRoleID());
            preparedStatement.setInt(6, clientGuild.getWarnTimeInMinutes());
            preparedStatement.setBoolean(7, clientGuild.isDaylightTimeEnabled());
            preparedStatement.setBoolean(8, clientGuild.isPremiumServer());
            preparedStatement.setBoolean(9, clientGuild.isAutoDeleteEnabled());
            preparedStatement.setInt(10, clientGuild.getAutoDeleteTimeInHours());
            preparedStatement.setLong(11, clientGuild.getEmbedLeadTime());
            preparedStatement.setString(12, clientGuild.getGuildID());
            preparedStatement.executeUpdate();
            log.info("Updated " + clientGuild.getGuildID() + " in database.");
        } catch (SQLException e) {
            log.error(e.getMessage(), e);
        }
    }

    public void deleteGuildByGuildID(String guildID) {
        try (
                Connection connection = databaseConnection.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(SQLStatements.DELETE_GUILD_BY_GUILD_ID_STATEMENT)
        ) {
            preparedStatement.setString(1, guildID);
            preparedStatement.executeUpdate();
            log.info("Deleted guild " + guildID + " from database.");
        } catch (SQLException e) {
            log.error(e.getMessage(), e);
        }
    }

    // Custom Notifications

    public void createCustomNotification(CustomNotification customNotification) {
        try (
                Connection connection = databaseConnection.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(SQLStatements.CREATE_CUSTOM_MESSAGE_STATEMENT)
        ) {
            preparedStatement.setString(1, customNotification.getGuildID());
            preparedStatement.setString(2, customNotification.getChannelID());
            preparedStatement.setString(3, customNotification.getMessage());
            preparedStatement.setString(4, customNotification.getWeekday());
            preparedStatement.setString(5, customNotification.getTime());
            preparedStatement.setBoolean(6, customNotification.isRepeating());
            preparedStatement.setString(7, customNotification.getCustomMessageID());
            preparedStatement.executeUpdate();
            log.info("Created Custom Notification for guild " + customNotification.getGuildID());
        } catch (SQLException e) {
            log.error(e.getMessage(), e);
        }
    }

    public void updateCustomNotification(CustomNotification customNotification) {
        try (
                Connection connection = databaseConnection.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(SQLStatements.UPDATE_CUSTOM_MESSAGE_STATEMENT)
        ) {
            preparedStatement.setString(1, customNotification.getMessage());
            preparedStatement.setString(2, customNotification.getWeekday());
            preparedStatement.setString(3, customNotification.getTime());
            preparedStatement.setBoolean(4, customNotification.isRepeating());
            preparedStatement.setBoolean(5, customNotification.isEnabled());
            preparedStatement.setString(6, customNotification.getChannelID());
            preparedStatement.executeUpdate();
            log.info("Updated Custom Notification for guild " + customNotification.getGuildID());
        } catch (SQLException e) {
            log.error(e.getMessage(), e);
        }
    }

    public void deleteCustomNotificationByID(String customMessageID) {
        try (
                Connection connection = databaseConnection.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(SQLStatements.DELETE_CUSTOM_MESSAGE_BY_ID_STATEMENT)
        ) {
            preparedStatement.setString(1, customMessageID);
            preparedStatement.executeUpdate();
            log.info("Deleted Custom Notification with ID " + customMessageID);
        } catch (SQLException e) {
            log.error(e.getMessage(), e);
        }
    }

    public void deleteMessagesByGuildID(String guildID) {
        try (
                Connection connection = databaseConnection.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(SQLStatements.DELETE_MESSAGE_BY_GUILD_ID_STATEMENT)
        ) {
            preparedStatement.setString(1, guildID);
            preparedStatement.executeUpdate();
            log.info("Deleted messages of " + guildID + " from database.");
        } catch (SQLException e) {
            log.error(e.getMessage(), e);
        }
    }

    // Notification Channel

    public void createNotificationChannel(NotificationChannel notificationChannel) {
        try (
                Connection connection = databaseConnection.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(SQLStatements.CREATE_CHANNEL_STATEMENT)
        ) {
            preparedStatement.setString(1, notificationChannel.getTextChannelID());
            preparedStatement.setString(2, notificationChannel.getGuildID());
            preparedStatement.setString(3, notificationChannel.getMentionRoleID());
            preparedStatement.setBoolean(4, notificationChannel.isEventMessageEnabled());
            preparedStatement.setBoolean(5, notificationChannel.isEventWarnMessage());
            preparedStatement.setBoolean(6, notificationChannel.isAncientArenaMessageEnabled());
            preparedStatement.setBoolean(7, notificationChannel.isAncientNightmareMessageEnabled());
            preparedStatement.setBoolean(8, notificationChannel.isAssemblyMessageEnabled());
            preparedStatement.setBoolean(9, notificationChannel.isBattlegroundsMessageEnabled());
            preparedStatement.setBoolean(10, notificationChannel.isVaultMessageEnabled());
            preparedStatement.setBoolean(11, notificationChannel.isDemonGatesMessageEnabled());
            preparedStatement.setBoolean(12, notificationChannel.isShadowLotteryMessageEnabled());
            preparedStatement.setBoolean(13, notificationChannel.isHauntedCarriageMessageEnabled());
            preparedStatement.setBoolean(14, notificationChannel.isDemonGatesMessageEmbedEnabled());
            preparedStatement.setBoolean(15, notificationChannel.isAncientArenaMessageEmbedEnabled());
            preparedStatement.setBoolean(16, notificationChannel.isHauntedCarriageMessageEmbedEnabled());
            preparedStatement.setBoolean(17, notificationChannel.isAncientNightmareMessageEmbedEnabled());
            preparedStatement.setBoolean(18, notificationChannel.isWrathborneInvasionEnabled());
            preparedStatement.setBoolean(19, notificationChannel.isTowerOfVictoryMessagesEnabled());
            preparedStatement.setBoolean(20, notificationChannel.isShadowWarMessagesEnabled());
            preparedStatement.executeUpdate();
            log.info("Created Notification Channel on guild " + notificationChannel.getGuildID() + " with " +
                    "channel id " + notificationChannel.getTextChannelID());
        } catch (SQLException e) {
            log.error(e.getMessage(), e);
        }
    }

    public void updateNotificationChannel(NotificationChannel notificationChannel) {
        try (
                Connection connection = databaseConnection.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(SQLStatements.UPDATE_CHANNEL_STATEMENT)
        ) {
            preparedStatement.setString(1, notificationChannel.getMentionRoleID());
            preparedStatement.setBoolean(2, notificationChannel.isEventMessageEnabled());
            preparedStatement.setBoolean(3, notificationChannel.isEventWarnMessage());
            preparedStatement.setBoolean(4, notificationChannel.isAncientArenaMessageEnabled());
            preparedStatement.setBoolean(5, notificationChannel.isAncientNightmareMessageEnabled());
            preparedStatement.setBoolean(6, notificationChannel.isAssemblyMessageEnabled());
            preparedStatement.setBoolean(7, notificationChannel.isBattlegroundsMessageEnabled());
            preparedStatement.setBoolean(8, notificationChannel.isVaultMessageEnabled());
            preparedStatement.setBoolean(9, notificationChannel.isDemonGatesMessageEnabled());
            preparedStatement.setBoolean(10, notificationChannel.isShadowLotteryMessageEnabled());
            preparedStatement.setBoolean(11, notificationChannel.isHauntedCarriageMessageEnabled());
            preparedStatement.setBoolean(12, notificationChannel.isDemonGatesMessageEmbedEnabled());
            preparedStatement.setBoolean(13, notificationChannel.isAncientNightmareMessageEmbedEnabled());
            preparedStatement.setBoolean(14, notificationChannel.isHauntedCarriageMessageEmbedEnabled());
            preparedStatement.setBoolean(15, notificationChannel.isWrathborneInvasionEnabled());
            preparedStatement.setBoolean(16, notificationChannel.isTowerOfVictoryMessagesEnabled());
            preparedStatement.setBoolean(17, notificationChannel.isShadowWarMessagesEnabled());
            preparedStatement.setString(18, notificationChannel.getTextChannelID());
            preparedStatement.executeUpdate();
            log.info("Updated Notification Channel on guild " + notificationChannel.getGuildID() + " with channel id " +
                    notificationChannel.getTextChannelID());
        } catch (SQLException e) {
            log.error(e.getMessage(), e);
        }
    }

    public void deleteNotificationChannelByID(String textChannelID) {
        try (
                Connection connection = databaseConnection.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(SQLStatements.DELETE_CHANNEL_BY_ID_STATEMENT)
        ) {
            preparedStatement.setString(1, textChannelID);
            preparedStatement.executeUpdate();
            log.info("Deleted Notification Channel with ID " + textChannelID);
        } catch (SQLException e) {
            log.error(e.getMessage(), e);
        }
    }

    public void deleteChannelsByGuildID(String guildID) {
        try (
                Connection connection = databaseConnection.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(SQLStatements.DELETE_CHANNEL_BY_GUILD_ID_STATEMENT)
        ) {
            preparedStatement.setString(1, guildID);
            preparedStatement.executeUpdate();
            log.info("Deleted channels of " + guildID + " from database.");
        } catch (SQLException e) {
            log.error(e.getMessage(), e);
        }
    }

    public void createScheduledEventsSettings(String guildID) {
        try (
                Connection connection = databaseConnection.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(SQLStatements.CREATE_SCHEDULED_EVENTS_SETTINGS_STATEMENT)
        ) {
            preparedStatement.setString(1, guildID);
            preparedStatement.executeUpdate();
            log.info("Inserted {} into {}.", guildID, SCHEDULED_EVENTS_SETTINGS_TABLE);
        } catch (SQLException e) {
            log.error(e.getMessage(), e);
        }
    }

    public void updateScheduledEventsSettings(ScheduledEventsSetting seSettings) {
        try (
                Connection connection = databaseConnection.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(SQLStatements.UPDATE_SCHEDULED_EVENTS_SETTINGS_STATEMENT)
        ) {
            preparedStatement.setBoolean(1, seSettings.isAncientArenaEnabled());
            preparedStatement.setBoolean(2, seSettings.isAncientNightmareEnabled());
            preparedStatement.setBoolean(3, seSettings.isAssemblyEnabled());
            preparedStatement.setBoolean(4, seSettings.isBattlegroundsEnabled());
            preparedStatement.setBoolean(5, seSettings.isDemonGatesEnabled());
            preparedStatement.setBoolean(6, seSettings.isHauntedCarriageEnabled());
            preparedStatement.setBoolean(7, seSettings.isStormpointEnabled());
            preparedStatement.setBoolean(8, seSettings.isShadowLotteryEnabled());
            preparedStatement.setBoolean(9, seSettings.isShadowWarEnabled());
            preparedStatement.setBoolean(10, seSettings.isAccursedTowerEnabled());
            preparedStatement.setBoolean(11, seSettings.isVaultEnabled());
            preparedStatement.setBoolean(12, seSettings.isWrathborneInvasionEnabled());
            preparedStatement.setString(13, seSettings.getGuildID());
            preparedStatement.executeUpdate();
            log.info("Updated {} in {}," +
                            "ancient_arena = {}," +
                            "ancient_nightmare = {}," +
                            "assembly = {}," +
                            "battlegrounds = {}," +
                            "demon_gates = {}," +
                            "haunted_carriage = {}," +
                            "stormpoint = {}," +
                            "shadow_lottery = {}," +
                            "shadow_war = {}," +
                            "accursed_tower = {}," +
                            "vault = {}," +
                            "wrathborne_invasion = {}.", seSettings.getGuildID(), SCHEDULED_EVENTS_SETTINGS_TABLE,
                    seSettings.isAncientArenaEnabled(), seSettings.isAncientNightmareEnabled(), seSettings.isAssemblyEnabled(),
                    seSettings.isBattlegroundsEnabled(), seSettings.isDemonGatesEnabled(), seSettings.isHauntedCarriageEnabled(),
                    seSettings.isStormpointEnabled(), seSettings.isShadowLotteryEnabled(), seSettings.isShadowWarEnabled(),
                    seSettings.isAccursedTowerEnabled(), seSettings.isVaultEnabled(), seSettings.isWrathborneInvasionEnabled());
        } catch (SQLException e) {
            log.error(e.getMessage(), e);
        }
    }

    public void deleteScheduledEventsSettings(String guildID) {
        try (
                Connection connection = databaseConnection.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(SQLStatements.DELETE_SCHEDULED_EVENTS_SETTINGS_STATEMENT)
        ) {
            preparedStatement.setString(1, guildID);
            preparedStatement.executeUpdate();
            log.info("Deleted {} from {}.", guildID, SCHEDULED_EVENTS_SETTINGS_TABLE);
        } catch (SQLException e) {
            log.error(e.getMessage(), e);
        }
    }

    /**
     * Get all articles from the database.
     *
     * @return all articles as list.
     */
    public List<ArticleDTO> getArticles() {
        List<ArticleDTO> list = new ArrayList<>();
        try (
                Connection connection = databaseConnection.getConnection();
                PreparedStatement ps = connection.prepareStatement(SQLStatements.GET_ALL_ARTICLES_STATEMENT)
        ) {
            try (ResultSet resultSet = ps.executeQuery()) {
                while (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    String url = resultSet.getString("url");
                    String category = resultSet.getString("category");
                    list.add(new ArticleDTO(id, url, category));
                }
            }
        } catch (SQLException e) {
            log.error(e.getMessage(), e);
        }
        return list;
    }

    /**
     * Deletes an article from the database.
     *
     * @param id the article-id
     * @return true if successful, false if failure.
     */
    public boolean deleteArticleWithID(int id) {
        try (
                Connection conn = databaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(SQLStatements.DELETE_ARTICLE_WITH_ID_STATEMENT)
        ) {
            ps.setInt(1, id);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            log.error(e.getMessage(), e);
            return false;
        }
    }

    /**
     * Inserts a new article to the database.
     *
     * @param articleDTO the article object.
     * @return true if successful, false if failure.
     */
    public boolean insertNewArticle(ArticleDTO articleDTO) {
        try (
                Connection conn = databaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(SQLStatements.INSERT_ARTICLE_STATEMENT)
        ) {
            ps.setInt(1, articleDTO.getId());
            ps.setString(2, articleDTO.getUrl());
            ps.setString(3, articleDTO.getCategory());
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            log.error(e.getMessage(), e);
            return false;
        }
    }


    /**
     * Add new news channel
     *
     * @param newsChannelDTO the news channel object
     * @return true if successful, false if failure.
     */
    public boolean insertNewsChannel(NewsChannelDTO newsChannelDTO) {
        try (
                Connection conn = databaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(SQLStatements.INSERT_NEWS_CHANNEL_STATEMENT)
        ) {
            ps.setString(1, newsChannelDTO.getChannelID());
            ps.setString(2, newsChannelDTO.getGuildID());
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            log.error(e.getMessage(), e);
            return false;
        }
    }

    /**
     * Deletes a news channel from the database
     *
     * @param id the news channel id
     * @return true if successful, false if failure.
     */
    public boolean deleteNewsChannelByID(String id) {
        try (
                Connection conn = databaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(SQLStatements.DELETE_NEWS_CHANNEL_BY_ID_STATEMENT)
        ) {
            ps.setString(1, id);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            log.error(e.getMessage(), e);
            return false;
        }
    }

    /**
     * Updates the news channel in the database
     * @param newsChannelDTO the to update news channel object
     * @return true if successful, false if failure
     */
    public boolean updateNewsChannel(NewsChannelDTO newsChannelDTO) {
        try (
                Connection conn = databaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(SQLStatements.UPDATE_CHANNEL_STATEMENT)
        ) {
            ps.setBoolean(1, newsChannelDTO.isDiabloImmortalNewsEnabled());
            ps.setBoolean(2, newsChannelDTO.isHearthStoneNewsEnabled());
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            log.error(e.getMessage(), e);
            return false;
        }
    }
}