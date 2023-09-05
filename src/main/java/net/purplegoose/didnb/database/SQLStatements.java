package net.purplegoose.didnb.database;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class SQLStatements {

    public static final String GET_ALL_GUILDS_STATEMENT;
    public static final String CREATE_NEW_GUILD_STATEMENT;
    public static final String DELETE_GUILD_BY_GUILD_ID_STATEMENT;
    public static final String UPDATE_GUILD_STATEMENT;
    public static final String GET_ALL_CUSTOM_MESSAGES_STATEMENT;
    public static final String CREATE_CUSTOM_MESSAGE_STATEMENT;
    public static final String DELETE_CUSTOM_MESSAGE_BY_ID_STATEMENT;
    public static final String DELETE_MESSAGE_BY_GUILD_ID_STATEMENT;
    public static final String UPDATE_CUSTOM_MESSAGE_STATEMENT;
    public static final String GET_ALL_CHANNELS_STATEMENT;
    public static final String CREATE_CHANNEL_STATEMENT;
    public static final String DELETE_CHANNEL_BY_ID_STATEMENT;
    public static final String UPDATE_CHANNEL_STATEMENT;
    public static final String DELETE_CHANNEL_BY_GUILD_ID_STATEMENT;
    public static final String CREATE_SCHEDULED_EVENTS_SETTINGS_STATEMENT;
    public static final String UPDATE_SCHEDULED_EVENTS_SETTINGS_STATEMENT;
    public static final String DELETE_SCHEDULED_EVENTS_SETTINGS_STATEMENT;
    public static final String GET_ALL_SCHEDULED_EVENTS_SETTINGS_STATEMENT;
    private static final String DEFAULT_PATH = "sql-scripts/";

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

        CREATE_SCHEDULED_EVENTS_SETTINGS_STATEMENT = loadResourceToString(DEFAULT_PATH + "create-scheduled-events-settings.sql");
        UPDATE_SCHEDULED_EVENTS_SETTINGS_STATEMENT = loadResourceToString(DEFAULT_PATH + "update-scheduled-events-settings.sql");
        DELETE_SCHEDULED_EVENTS_SETTINGS_STATEMENT = loadResourceToString(DEFAULT_PATH + "delete-scheduled-events-settings.sql");
        GET_ALL_SCHEDULED_EVENTS_SETTINGS_STATEMENT = loadResourceToString(DEFAULT_PATH + "get-all-scheduled-events-settings.sql");
    }

    private SQLStatements() { /* static use only */ }

    private static String loadResourceToString(String path) {
        InputStream stream = Thread.currentThread()
                .getContextClassLoader()
                .getResourceAsStream(path);
        try {
            assert stream != null : "Stream was null!";
            return IOUtils.toString(stream, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        } finally {
            IOUtils.closeQuietly(stream);
        }
    }

}
