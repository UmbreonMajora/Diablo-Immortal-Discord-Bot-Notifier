package me.umbreon.didn.utils;

import me.umbreon.didn.commands.server.LanguageCommand;
import me.umbreon.didn.logger.FileLogger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConfigUtil.class);

    private static int DEFAULT_WARN_TIME;
    private static String CLIENT_TOKEN;
    private static int CUSTOM_NOTIFICATION_MAX;

    private static String DATABASE_HOST;
    private static String DATABASE;
    private static String DATABASE_USER;
    private static String DATABASE_PASSWORD;
    private static String DATABASE_PORT;

    static {
        Properties clientProperties = new Properties();
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        InputStream stream = loader.getResourceAsStream("client.properties");

        try {
            clientProperties.load(stream);
            LOGGER.info("Loaded client.properties file.");
            FileLogger.createClientFileLog("Loaded client.properties file.");
        } catch (IOException e) {
            LOGGER.warn("Failed to load client.properties file! Shutting down...");
            FileLogger.createClientFileLog("Failed to load client.properties file! Shutting down...");
            e.printStackTrace();
            System.exit(0);
        }

        DEFAULT_WARN_TIME = Integer.parseInt(clientProperties.getProperty("DEFAULT-WARN-TIME"));
        CUSTOM_NOTIFICATION_MAX = Integer.parseInt(clientProperties.getProperty("CUSTOM-NOTIFICATION-MAX"));
        CLIENT_TOKEN = clientProperties.getProperty("TOKEN");
        DATABASE_HOST = clientProperties.getProperty("HOST");
        DATABASE = clientProperties.getProperty("DATABASE");
        DATABASE_USER = clientProperties.getProperty("USERNAME");
        DATABASE_PASSWORD = clientProperties.getProperty("PASSWORD");
        DATABASE_PORT = clientProperties.getProperty("PORT");
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
}
