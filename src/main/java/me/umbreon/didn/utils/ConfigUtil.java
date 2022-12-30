package me.umbreon.didn.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigUtil {

    private static int DEFAULT_WARN_TIME;
    private static String CLIENT_TOKEN;

    static {
        Properties PROPERTIES = new Properties();
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        InputStream stream = loader.getResourceAsStream("client.properties");

        try {
            PROPERTIES.load(stream);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            DEFAULT_WARN_TIME = Integer.parseInt(PROPERTIES.getProperty("DEFAULT-WARN-TIME"));
            CLIENT_TOKEN = PROPERTIES.getProperty("TOKEN");
        } catch (NumberFormatException e) {
            DEFAULT_WARN_TIME = 15;
            CLIENT_TOKEN = null;
        }
    }

    public static int getDefaultWarnTime() {
        return DEFAULT_WARN_TIME;
    }

    public static String getClientToken() {
        return CLIENT_TOKEN;
    }
}
