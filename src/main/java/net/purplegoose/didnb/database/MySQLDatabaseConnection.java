package net.purplegoose.didnb.database;

import com.mysql.cj.jdbc.MysqlDataSource;
import net.purplegoose.didnb.utils.ConfigUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;

public class MySQLDatabaseConnection implements IDatabaseConnection {

    private final Logger LOGGER = LoggerFactory.getLogger(MySQLDatabaseConnection.class);

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
            connection.prepareStatement("SELECT 1;").executeQuery();
            //connection.prepareStatement(SQLStatements.getCreateReactionRolesStatement());
            //connection.prepareStatement(SQLStatements.getCreateMessagesTableStatement());
            //connection.prepareStatement(SQLStatements.getCreateGuildsTableStatement());
            //connection.prepareStatement(SQLStatements.getCreateChannelTableStatement());
        } catch (SQLException e) {
            LOGGER.error(e.getMessage(), e);
            e.printStackTrace();
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
}
