package net.purplegoose.didnb.database;

import java.sql.Connection;

public interface IDatabaseConnection {

    boolean createConnection();

    void closeConnection();

    Connection getConnection();

}
