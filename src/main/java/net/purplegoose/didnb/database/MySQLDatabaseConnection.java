package net.purplegoose.didnb.database;

import com.mysql.cj.jdbc.MysqlDataSource;
import net.purplegoose.didnb.utils.ConfigUtil;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MySQLDatabaseConnection implements IDatabaseConnection {

    private final Logger LOGGER = LoggerFactory.getLogger(MySQLDatabaseConnection.class);
    private static final String STARTUP_SCRIPT_FOLDER = "start";
    private MysqlDataSource dataSource;

    public MySQLDatabaseConnection() {
        createConnection();
    }

    @Override
    public boolean createConnection() {
        String host = ConfigUtil.getDatabaseHost();
        String database = ConfigUtil.getDatabaseDatabase();
        String username = ConfigUtil.getDatabaseUsername();
        String password = ConfigUtil.getDatabasePassword();
        int port = ConfigUtil.getDatabasePort();

        dataSource = new MysqlDataSource();
        dataSource.setServerName(host);
        dataSource.setPortNumber(port);
        dataSource.setDatabaseName(database);
        dataSource.setUser(username);
        dataSource.setPassword(password);
        dataSource.setUrl("jdbc:mysql://" + host + ":" + port + "/" + database + "?verifyServerCertificate=false&useSSL=true");

        try (Connection connection = dataSource.getConnection()) {
            LOGGER.info("Executing start up scripts...");
            for (String scriptName : loadStartUpScripts()) {
                String statement = loadResourceToString(STARTUP_SCRIPT_FOLDER + "/" + scriptName);
                connection.prepareStatement(statement).executeQuery();
                LOGGER.info("> executed {}.", scriptName);
            }
            LOGGER.info("Done executing start up scripts!");
        } catch (SQLException e) {
            LOGGER.error(e.getMessage(), e);
            return false;
        }
        return true;
    }

    @Override
    public void closeConnection() {
        if (dataSource != null) {
            try {
                dataSource.getConnection().close();
            } catch (SQLException e) {
                LOGGER.error(e.getMessage(), e);
            }
        }
    }

    @Override
    public Connection getConnection() {
        try {
            return dataSource.getConnection();
        } catch (SQLException e) {
            LOGGER.error(e.getMessage(), e);
            return null;
        }
    }

    private List<String> loadStartUpScripts() {
        LOGGER.info("Loading scripts for start up...");
        List<String> filenames = new ArrayList<>();

        try (InputStream in = getResourceAsStream();
             BufferedReader br = new BufferedReader(new InputStreamReader(in))) {
            String resource;

            while ((resource = br.readLine()) != null) {
                LOGGER.info("> Added {} to start up scripts.", resource);
                filenames.add(resource);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        LOGGER.info("Loaded {} start up scripts!", filenames.size());
        return filenames;
    }

    private InputStream getResourceAsStream() {
        InputStream in = getContextClassLoader().getResourceAsStream(STARTUP_SCRIPT_FOLDER);
        return in == null ? MySQLDatabaseConnection.class.getResourceAsStream(STARTUP_SCRIPT_FOLDER) : in;
    }

    private ClassLoader getContextClassLoader() {
        return Thread.currentThread().getContextClassLoader();
    }

    private String loadResourceToString(String path) {
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

}
