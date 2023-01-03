package me.umbreon.didn.database;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class SQLStatements {

    private static final String DEFAULT_PATH = "sql-scripts/";

    private static final String GET_ALL_GUILDS_STATEMENT;
    private static final String CREATE_NEW_GUILD_STATEMENT;
    private static final String DELETE_GUILD_BY_GUILD_ID_STATEMENT;
    private static final String UPDATE_GUILD_STATEMENT;

    private static final String GET_ALL_CUSTOM_MESSAGES_STATEMENT;
    private static final String CREATE_CUSTOM_MESSAGE_STATEMENT;
    private static final String DELETE_CUSTOM_MESSAGE_BY_ID_STATEMENT;
    private static final String DELETE_MESSAGE_BY_GUILD_ID_STATEMENT;
    private static final String UPDATE_CUSTOM_MESSAGE_STATEMENT;

    private static final String GET_ALL_CHANNELS_STATEMENT;
    private static final String CREATE_CHANNEL_STATEMENT;
    private static final String DELETE_CHANNEL_BY_ID_STATEMENT;
    private static final String UPDATE_CHANNEL_STATEMENT;
    private static final String DELETE_CHANNEL_BY_GUILD_ID_STATEMENT;

    private static final String GET_ALL_REACTION_ROLES_STATEMENT;
    private static final String CREATE_REACTION_ROLE_STATEMENT;
    private static final String DELETE_REACTION_ROLE_BY_MESSAGE_ID_STATEMENT;
    private static final String DELETE_REACTION_ROLES_BY_GUILD_ID_STATEMENT;
    //TODO: UPDATE_STATEMENTS?

    private static final String GET_CUSTOM_MESSAGE_NEXT_AUTO_INCREMENT_VALUE;

    static {

        GET_ALL_GUILDS_STATEMENT = loadResourceToString(DEFAULT_PATH + "get-all-guilds.sql");
        CREATE_NEW_GUILD_STATEMENT = loadResourceToString(DEFAULT_PATH + "create-new-guild.sql");
        UPDATE_GUILD_STATEMENT = loadResourceToString(DEFAULT_PATH + "update-guild.sql");
        DELETE_GUILD_BY_GUILD_ID_STATEMENT = loadResourceToString(DEFAULT_PATH + "delete-guild-by-guildid.sql");

        GET_ALL_CUSTOM_MESSAGES_STATEMENT = loadResourceToString(DEFAULT_PATH + "get-all-custom-messages.sql");
        CREATE_CUSTOM_MESSAGE_STATEMENT = loadResourceToString(DEFAULT_PATH + "create-custom-message.sql");
        DELETE_CUSTOM_MESSAGE_BY_ID_STATEMENT = loadResourceToString(DEFAULT_PATH + "delete-custom-message-by-id.sql");
        UPDATE_CUSTOM_MESSAGE_STATEMENT = loadResourceToString(DEFAULT_PATH + "update-custom-notification.sql");
        DELETE_MESSAGE_BY_GUILD_ID_STATEMENT = loadResourceToString(DEFAULT_PATH + "delete-message-by-guildid.sql");

        GET_ALL_CHANNELS_STATEMENT = loadResourceToString(DEFAULT_PATH + "get-all-channels.sql");
        CREATE_CHANNEL_STATEMENT = loadResourceToString(DEFAULT_PATH + "create-channel.sql");
        DELETE_CHANNEL_BY_ID_STATEMENT = loadResourceToString(DEFAULT_PATH + "delete-channel-by-id.sql");
        UPDATE_CHANNEL_STATEMENT = loadResourceToString(DEFAULT_PATH + "update-channel.sql");
        DELETE_CHANNEL_BY_GUILD_ID_STATEMENT = loadResourceToString(DEFAULT_PATH + "delete-channels-by-guildid.sql");

        GET_ALL_REACTION_ROLES_STATEMENT = loadResourceToString(DEFAULT_PATH + "get-all-reaction-roles.sql");
        CREATE_REACTION_ROLE_STATEMENT = loadResourceToString(DEFAULT_PATH + "create-reaction-role.sql");
        DELETE_REACTION_ROLE_BY_MESSAGE_ID_STATEMENT = loadResourceToString(DEFAULT_PATH + "delete-reaction-role-by-message-id.sql");
        DELETE_REACTION_ROLES_BY_GUILD_ID_STATEMENT = loadResourceToString(DEFAULT_PATH + "delete-reaction-roles-by-guildid.sql");

        GET_CUSTOM_MESSAGE_NEXT_AUTO_INCREMENT_VALUE = loadResourceToString(DEFAULT_PATH + "get-custom-message-next-auto-increment-value.sql");
    }

    private static String loadResourceToString(String path) {
        InputStream stream = Thread.currentThread()
                .getContextClassLoader()
                .getResourceAsStream(path);
        try {
            return IOUtils.toString(stream, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        } finally {
            IOUtils.closeQuietly(stream);
        }
    }

    /*
     * Guilds Statements Getter
     */

    public static String getGetAllGuildsStatement() {
        return GET_ALL_GUILDS_STATEMENT;
    }

    public static String getCreateNewGuildStatement() {
        return CREATE_NEW_GUILD_STATEMENT;
    }

    public static String getUpdateGuildStatement() {
        return UPDATE_GUILD_STATEMENT;
    }

    public static String getDeleteGuildByGuildIdStatement() {
        return DELETE_GUILD_BY_GUILD_ID_STATEMENT;
    }

    /*
     * Custom Message Statements Getter
     */

    public static String getGetAllCustomMessagesStatement() {
        return GET_ALL_CUSTOM_MESSAGES_STATEMENT;
    }

    public static String getCreateCustomMessageStatement() {
        return CREATE_CUSTOM_MESSAGE_STATEMENT;
    }

    public static String getDeleteCustomMessageByIdStatement() {
        return DELETE_CUSTOM_MESSAGE_BY_ID_STATEMENT;
    }

    public static String getUpdateCustomMessageStatement() {
        return UPDATE_CUSTOM_MESSAGE_STATEMENT;
    }

    public static String getDeleteMessageByGuildIdStatement() {
        return DELETE_MESSAGE_BY_GUILD_ID_STATEMENT;
    }

    /*
     * Channel Statements Getter
     */

    public static String getGetAllChannelsStatement() {
        return GET_ALL_CHANNELS_STATEMENT;
    }

    public static String getCreateChannelStatement() {
        return CREATE_CHANNEL_STATEMENT;
    }

    public static String getDeleteChannelByIdStatement() {
        return DELETE_CHANNEL_BY_ID_STATEMENT;
    }

    public static String getUpdateChannelStatement() {
        return UPDATE_CHANNEL_STATEMENT;
    }

    public static String getDeleteChannelByGuildIdStatement() {
        return DELETE_CHANNEL_BY_GUILD_ID_STATEMENT;
    }

    /*
     * Reaction Roles Statements Getter
     */

    public static String getGetAllReactionRolesStatement() {
        return GET_ALL_REACTION_ROLES_STATEMENT;
    }

    public static String getCreateReactionRoleStatement() {
        return CREATE_REACTION_ROLE_STATEMENT;
    }

    public static String getDeleteReactionRoleByMessageIdStatement() {
        return DELETE_REACTION_ROLE_BY_MESSAGE_ID_STATEMENT;
    }

    public static String getDeleteReactionRolesByGuildIdStatement() {
        return DELETE_REACTION_ROLES_BY_GUILD_ID_STATEMENT;
    }

    /*
     *
     */

    public static String getGetCustomMessageNextAutoIncrementValue() {
        return GET_CUSTOM_MESSAGE_NEXT_AUTO_INCREMENT_VALUE;
    }
}
