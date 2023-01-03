package me.umbreon.didn.database;

import com.mysql.cj.jdbc.MysqlDataSource;
import me.umbreon.didn.logger.FileLogger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

public class MySQLDatabaseConnection implements IDatabaseConnection {

    private final Logger LOGGER = LoggerFactory.getLogger(MySQLDatabaseConnection.class);

    private MysqlDataSource dataSource;

    public MySQLDatabaseConnection() {
        createConnection();
    }

    @Override
    public boolean createConnection() {
        Properties prop = new Properties();
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        InputStream stream = loader.getResourceAsStream("database.properties");

        try {
            prop.load(stream);
        } catch (IOException e) {
            e.printStackTrace();
        }

        String host = prop.getProperty("host");
        String database = prop.getProperty("database");
        String username = prop.getProperty("username");
        String password = prop.getProperty("password");
        int port = Integer.parseInt(prop.getProperty("port"));

        dataSource = new MysqlDataSource();
        dataSource.setServerName(host);
        dataSource.setPortNumber(port);
        dataSource.setDatabaseName(database);
        dataSource.setUser(username);
        dataSource.setPassword(password);
        dataSource.setUrl("jdbc:mysql://" + host + ":" + port + "/" + database + "?verifyServerCertificate=false&useSSL=true");

        try (Connection connection = dataSource.getConnection()) {
            /* Todo: implement start up scripts
            String startSqlFilesPath = "run";
            for (String sqlFile : FilesUtil.getResourceFiles(startSqlFilesPath, getClass())) {
                connection.prepareStatement(FilesUtil.loadResourceToString(startSqlFilesPath + "/" + sqlFile))
                        .executeUpdate();
            }
             */
            connection.prepareStatement("SELECT 1;").executeQuery();
        } catch (SQLException e) {
            FileLogger.createClientFileErrorLog(e.getMessage(), e);
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
                FileLogger.createClientFileErrorLog(e.getMessage(), e);
                LOGGER.error(e.getMessage(), e);
            }
        }
    }

    @Override
    public Connection getConnection() {
        try {
            return dataSource.getConnection();
        } catch (SQLException e) {
            FileLogger.createClientFileErrorLog(e.getMessage(), e);
            LOGGER.error(e.getMessage(), e);
            return null;
        }
    }
}
