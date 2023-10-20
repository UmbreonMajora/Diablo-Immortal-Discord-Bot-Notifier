package net.purplegoose.bot.di.database;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class SQLStatements {
    private static final String DEFAULT_PATH = "sql-scripts/";
    public static final String INSERT_CLIENT_GUILD;
    static {
        INSERT_CLIENT_GUILD = loadResourceToString("insert-client-guild.sql");
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