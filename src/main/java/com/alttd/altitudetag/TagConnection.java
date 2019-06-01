package com.alttd.altitudetag;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import com.alttd.altitudetag.configuration.Config;

public class TagConnection
{
    private static TagConnection instance;

    private Connection connection;

    private String host;
    private String database;
    private String username;
    private String password;
    private int    port;
    private String description;

    private TagConnection()
    {
        this.host = Config.DATABASE_HOSTNAME.getValue();
        this.database = Config.DATABASE_DATABASE.getValue();
        this.username = Config.DATABASE_USERNAME.getValue();
        this.password = Config.DATABASE_PASSWORD.getValue();
        this.port = Config.DATABASE_PORT.getValue();
        this.description = Config.DATABASE_DESCRIPTION.getValue();

        try
        {
            instance.openConnection();
        }
        catch (SQLException | ClassNotFoundException ex)
        {
            ex.printStackTrace();
        }
    }

    private void openConnection() throws SQLException, ClassNotFoundException
    {
        if (connection != null && !connection.isClosed())
        {
            return;
        }

        synchronized (this)
        {
            if (connection != null && !connection.isClosed())
            {
                return;
            }
            Class.forName("org.mariadb.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mariadb://" + this.host + ":" + this.port + "/" + this.database, this.username, this.password);
        }
    }

    public static Connection getConnection()
    {
        try
        {
            instance.openConnection();
        }
        catch (SQLException | ClassNotFoundException ex)
        {
            ex.printStackTrace();
        }

        return instance.connection;
    }

    public static void initialize()
    {
        instance = new TagConnection();
    }
}
