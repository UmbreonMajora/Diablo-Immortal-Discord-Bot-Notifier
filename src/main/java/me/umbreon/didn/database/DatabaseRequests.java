package me.umbreon.didn.database;

import me.umbreon.didn.data.*;
import me.umbreon.didn.enums.Language;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class DatabaseRequests {

    private final Logger LOGGER = LoggerFactory.getLogger(DatabaseRequests.class);

    private final IDatabaseConnection databaseConnection;

    public DatabaseRequests(IDatabaseConnection databaseConnection) {
        this.databaseConnection = databaseConnection;
    }

    public Set<EventGameData> getEventTimes(String table, boolean everyDay) {
        Set<EventGameData> listEventTimeTables = new HashSet<>();
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
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listEventTimeTables;
    }

    public Map<String, ClientGuild> loadDataFromDatabaseToCache() throws SQLException {
        Map<String, ClientGuild> clientGuildData = new ConcurrentHashMap<>();

        try (
                Connection connection = databaseConnection.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(SQLStatements.getGetAllGuildsStatement())
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

                    ClientGuild clientGuild = new ClientGuild(guildID, guildLanguage, timeZone, adminRoleID, warnTime, isWarnMessagesEnabled, isEventMessagesEnabled);
                    clientGuildData.put(guildID, clientGuild);
                }
            }
        }

        try (
                Connection connection = databaseConnection.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(SQLStatements.getGetAllCustomMessagesStatement())
        ) {
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    String guildID = resultSet.getString("guildID");
                    String channelID = resultSet.getString("channelID");
                    String message = resultSet.getString("message");
                    String day = resultSet.getString("day");
                    String time = resultSet.getString("time");
                    boolean repeat = (resultSet.getInt("message_repeat") == 1);
                    int id = resultSet.getInt("message_id");
                    boolean enabled = (resultSet.getInt("message_enabled") == 1);
                    CustomNotification customNotification = new CustomNotification(channelID, guildID, message, day, time, id, repeat, enabled);
                    if (clientGuildData.containsKey(guildID)) {
                        clientGuildData.get(guildID).addCustomNotification(customNotification);
                    } else {
                        LOGGER.error("Failed to add custom message with id " + id + " to " + guildID + ".");
                    }
                }
            }
        }

        try (
                Connection connection = databaseConnection.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(SQLStatements.getGetAllChannelsStatement())
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

                    NotificationChannel notificationChannel = new NotificationChannel(roleID, guildID, textChannelID,
                            headup, message, assembly, vault, demongates, ancientarena, shadowlottery,
                            battlegrounds, hauntedcarriage, ancientnightmare, demongatesembed, ancientarenaembed,
                            hauntedcarriageembed, ancientnightmareembed, wrathborneInvasionEnabled, isOnSlaughtMessagesEnabled);

                    if (clientGuildData.containsKey(guildID)) {
                        clientGuildData.get(guildID).addNewNotificationChannel(notificationChannel);
                    } else {
                        LOGGER.error("Failed to add notification channel with channel id " + textChannelID + " to " + guildID + ".");
                    }
                }
            }
        }

        try (
                Connection connection = databaseConnection.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(SQLStatements.getGetAllReactionRolesStatement())
        ) {
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    String messageID = resultSet.getString("messageID");
                    String guildID = resultSet.getString("guildID");
                    String roleID = resultSet.getString("roleID");
                    String emojiCode = resultSet.getString("emojiCode");
                    String emojiType = resultSet.getString("emojiType");
                    ReactionRole reactionRole = new ReactionRole(messageID, guildID, emojiCode, emojiType, roleID);

                    if (clientGuildData.containsKey(guildID)) {
                        clientGuildData.get(guildID).addReactionRole(reactionRole);
                    } else {
                        LOGGER.error("Failed to add reaction role with message id " + messageID + " to " + guildID + ".");
                    }
                }
            }
        }

        return clientGuildData;
    }

    // Client Guild

    public void createGuild(ClientGuild clientGuild) {
        try (
                Connection connection = databaseConnection.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(SQLStatements.getCreateNewGuildStatement())
        ) {
            preparedStatement.setString(1, clientGuild.getGuildID());
            preparedStatement.setString(2, clientGuild.getGuildLanguage().shortName);
            preparedStatement.setString(3, clientGuild.getGuildTimeZone());
            preparedStatement.setBoolean(4, clientGuild.isWarnMessagesEnabled());
            preparedStatement.setBoolean(5, clientGuild.isEventMessageEnabled());
            preparedStatement.executeUpdate();
            LOGGER.info("Registered new guild with ID: " + clientGuild.getGuildID() + " in database!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateGuild(ClientGuild clientGuild) {
        try (
                Connection connection = databaseConnection.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(SQLStatements.getUpdateGuildStatement())
        ) {
            preparedStatement.setString(1, clientGuild.getGuildLanguage().shortName);
            preparedStatement.setString(2, clientGuild.getGuildTimeZone());
            preparedStatement.setBoolean(3, clientGuild.isWarnMessagesEnabled());
            preparedStatement.setBoolean(4, clientGuild.isEventMessageEnabled());
            preparedStatement.setString(5, clientGuild.getGuildAdminRoleID());
            preparedStatement.setInt(6, clientGuild.getHeadUpTime());
            preparedStatement.setString(7, clientGuild.getGuildID());
            preparedStatement.executeUpdate();
            LOGGER.info("Updated Guild " + clientGuild.getGuildID());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Todo: delete Guild is missing.

    // Custom Notifications

    public void createCustomNotification(CustomNotification customNotification) {
        try (
                Connection connection = databaseConnection.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(SQLStatements.getCreateCustomMessageStatement())
        ) {
            preparedStatement.setString(1, customNotification.getGuildID());
            preparedStatement.setString(2, customNotification.getChannelID());
            preparedStatement.setString(3, customNotification.getMessage());
            preparedStatement.setString(4, customNotification.getWeekday());
            preparedStatement.setString(5, customNotification.getTime());
            preparedStatement.setBoolean(6, customNotification.isRepeating());
            preparedStatement.executeUpdate();
            LOGGER.info("Created Custom Notification for guild " + customNotification.getGuildID());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateCustomNotification(CustomNotification customNotification) {
        try (
                Connection connection = databaseConnection.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(SQLStatements.getUpdateCustomMessageStatement())
        ) {
            preparedStatement.setString(1, customNotification.getMessage());
            preparedStatement.setString(2, customNotification.getWeekday());
            preparedStatement.setString(3, customNotification.getTime());
            preparedStatement.setBoolean(4, customNotification.isRepeating());
            preparedStatement.setBoolean(5, customNotification.isEnabled());
            preparedStatement.setString(6, customNotification.getChannelID());
            preparedStatement.executeUpdate();
            LOGGER.info("Updated Custom Notification for guild " + customNotification.getGuildID());
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void deleteCustomNotificationByID(int customMessageID) {
        try (
                Connection connection = databaseConnection.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(SQLStatements.getDeleteCustomMessageByIdStatement())
        ) {
            preparedStatement.setInt(1, customMessageID);
            preparedStatement.executeUpdate();
            LOGGER.info("Deleted Custom Notification with ID " + customMessageID);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int getGetCustomMessageNextAutoIncrementValue() {
        int nextAutoIncrementNumber = 0;
        try (
                Connection connection = databaseConnection.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(SQLStatements.getGetCustomMessageNextAutoIncrementValue())
        ) {
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    nextAutoIncrementNumber = resultSet.getInt("AUTO_INCREMENT");
                } else {
                    throw new SQLException("Failed to get next auto increment number for custom messages");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return nextAutoIncrementNumber + 1;
    }

    // Notification Channel

    public void createNotificationChannel(NotificationChannel notificationChannel) {
        try (
                Connection connection = databaseConnection.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(SQLStatements.getCreateChannelStatement())
        ) {
            preparedStatement.setString(1, notificationChannel.getTextChannelID());
            preparedStatement.setString(2, notificationChannel.getGuildID());
            preparedStatement.setString(3, notificationChannel.getMentionRoleID());
            preparedStatement.setBoolean(4, notificationChannel.isEventMessageEnabled());
            preparedStatement.setBoolean(5, notificationChannel.isWarnMessagesEnabled());
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
            preparedStatement.executeUpdate();
            LOGGER.info("Created Notification Channel on guild " + notificationChannel.getGuildID() + " with " +
                    "channel id " + notificationChannel.getTextChannelID());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateNotificationChannel(NotificationChannel notificationChannel) {
        try (
                Connection connection = databaseConnection.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(SQLStatements.getUpdateChannelStatement())
        ) {
            preparedStatement.setString(1, notificationChannel.getMentionRoleID());
            preparedStatement.setBoolean(2, notificationChannel.isEventMessageEnabled());
            preparedStatement.setBoolean(3, notificationChannel.isWarnMessagesEnabled());
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
            preparedStatement.setString(16, notificationChannel.getTextChannelID());
            preparedStatement.executeUpdate();
            LOGGER.info("Updated Notification Channel on guild " + notificationChannel.getGuildID() + " with channel id " +
                    notificationChannel.getTextChannelID());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteNotificationChannelByID(String textChannelID) {
        try (
                Connection connection = databaseConnection.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(SQLStatements.getDeleteChannelByIdStatement())
        ) {
            preparedStatement.setString(1, textChannelID);
            preparedStatement.executeUpdate();
            LOGGER.info("Deleted Notification Channel with ID " + textChannelID);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Reaction role

    public void createReactionRole(ReactionRole reactionRole) {
        try (
                Connection connection = databaseConnection.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(SQLStatements.getCreateReactionRoleStatement())
        ) {
            preparedStatement.setString(1, reactionRole.getMessageID());
            preparedStatement.setString(2, reactionRole.getGuildID());
            preparedStatement.setString(3, reactionRole.getReactionID());
            preparedStatement.setString(4, reactionRole.getReactionType());
            preparedStatement.setString(5, reactionRole.getRoleID());
            preparedStatement.executeUpdate();
            LOGGER.info("Created new reaction role on " + reactionRole.getGuildID());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteReactionRoleByMessageID(String messageID) {
        try (
                Connection connection = databaseConnection.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(SQLStatements.getDeleteReactionRoleByMessageIdStatement())
        ) {
            preparedStatement.setString(1, messageID);
            preparedStatement.executeUpdate();
            LOGGER.info("Deleted Reaction role with ID " + messageID);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
