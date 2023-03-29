package net.purplegoose.didnb.utils;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
@Slf4j
public class ConfigUtil {

    private static final Properties CLIENT_PROPERTIES = new Properties();

    private static final int DEFAULT_WARN_TIME;
    private static final String CLIENT_TOKEN;
    private static final int CUSTOM_NOTIFICATION_MAX;

    private static final String DATABASE_HOST;
    private static final String DATABASE_DATABASE;
    private static final String DATABASE_USERNAME;
    private static final String DATABASE_PASSWORD;
    private static final int DATABASE_PORT;

    static {
        try {
            CLIENT_PROPERTIES.load(new FileInputStream(System.getProperty("user.dir") + "/client.properties"));
            log.info("Loaded client.properties file.");
        } catch (IOException e) {
            e.printStackTrace();
            log.warn("Failed to load client.properties file! Shutting down...");
            System.exit(0);
        }

        DEFAULT_WARN_TIME = Integer.parseInt(CLIENT_PROPERTIES.getProperty("DEFAULT-WARN-TIME"));
        CUSTOM_NOTIFICATION_MAX = Integer.parseInt(CLIENT_PROPERTIES.getProperty("CUSTOM-NOTIFICATION-MAX"));
        CLIENT_TOKEN = CLIENT_PROPERTIES.getProperty("TOKEN");

        DATABASE_HOST = CLIENT_PROPERTIES.getProperty("DATABASE_HOST");
        DATABASE_DATABASE = CLIENT_PROPERTIES.getProperty("DATABASE_DATABASE");
        DATABASE_USERNAME = CLIENT_PROPERTIES.getProperty("DATABASE_USERNAME");
        DATABASE_PASSWORD = CLIENT_PROPERTIES.getProperty("DATABASE_PASSWORD");
        DATABASE_PORT = Integer.parseInt(CLIENT_PROPERTIES.getProperty("DATABASE_PORT"));
    }

    public static int getDefaultWarnTime() {
        return DEFAULT_WARN_TIME;
    }

    public static String getClientToken() {
        return CLIENT_TOKEN;
    }

    public static int getCustomNotificationMax() {
        return CUSTOM_NOTIFICATION_MAX;
    }

    public static String getDatabaseHost() {
        return DATABASE_HOST;
    }

    public static String getDatabaseDatabase() {
        return DATABASE_DATABASE;
    }

    public static String getDatabaseUsername() {
        return DATABASE_USERNAME;
    }

    public static String getDatabasePassword() {
        return DATABASE_PASSWORD;
    }

    public static int getDatabasePort() {
        return DATABASE_PORT;
    }
}
