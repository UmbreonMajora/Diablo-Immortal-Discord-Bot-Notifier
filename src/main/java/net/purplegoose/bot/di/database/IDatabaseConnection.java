package net.purplegoose.bot.di.database;

import java.sql.Connection;

public interface IDatabaseConnection {

    boolean createConnection();

    void closeConnection();

    Connection getConnection();

}
