package net.purplegoose.bot.di.database;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.purplegoose.bot.di.data.ClientGuild;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@Slf4j
@AllArgsConstructor
public class DatabaseRequests {

    private final IDatabaseConnection databaseConnection;

    public void insertClientGuild(ClientGuild clientGuild) {
        try (
                Connection connection = databaseConnection.getConnection();
                PreparedStatement ps = connection.prepareStatement(SQLStatements.INSERT_CLIENT_GUILD)
        ) {
            ps.setString(1, clientGuild.getGuildID());
            ps.executeQuery();
        } catch (SQLException e) {
            log.error(e.getMessage(), e);
        }
    }

}
