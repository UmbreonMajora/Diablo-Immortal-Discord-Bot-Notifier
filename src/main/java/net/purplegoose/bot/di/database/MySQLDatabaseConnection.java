package net.purplegoose.bot.di.database;

import com.mysql.cj.jdbc.MysqlDataSource;
import lombok.extern.slf4j.Slf4j;
import net.purplegoose.didnb.utils.ConfigUtil;
import org.apache.commons.io.IOUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class MySQLDatabaseConnection implements IDatabaseConnection {

    private static final String STARTUP_SCRIPT_FOLDER = "start-up";
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
            log.info("Executing start up scripts...");
            for (String scriptName : loadStartUpScripts()) {
                String statement = loadResourceToString(STARTUP_SCRIPT_FOLDER + "/" + scriptName);
                connection.prepareStatement(statement).executeUpdate();
                log.info("> executed {}.", scriptName);
            }
            log.info("Done executing start up scripts!");
        } catch (SQLException e) {
            log.error(e.getMessage(), e);
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
                log.error(e.getMessage(), e);
            }
        }
    }

    @Override
    public Connection getConnection() {
        try {
            return dataSource.getConnection();
        } catch (SQLException e) {
            log.error(e.getMessage(), e);
            return null;
        }
    }

    private List<String> loadStartUpScripts() {
        log.info("Loading scripts for start up...");
        List<String> filenames = new ArrayList<>();

        try (InputStream in = ClassLoader.getSystemClassLoader().getResourceAsStream(STARTUP_SCRIPT_FOLDER)) {
            assert in != null;
            try (BufferedReader br = new BufferedReader(new InputStreamReader(in))) {
                String resource;

                while ((resource = br.readLine()) != null) {
                    log.info("> Added {} to start up scripts.", resource);
                    filenames.add(resource);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        log.info("Loaded {} start up scripts!", filenames.size());
        return filenames;
    }

    private String loadResourceToString(String path) {
        InputStream stream = ClassLoader.getSystemClassLoader()
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
