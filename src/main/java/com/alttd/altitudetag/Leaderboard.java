package com.alttd.altitudetag;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.UUID;
import java.util.function.Consumer;

import org.bukkit.Bukkit;

public class Leaderboard
{
    public static void initialize()
    {
        //language=SQL
        String sql = "CREATE TABLE IF NOT EXISTS Players ("
                     + " PlayerId INT NOT NULL AUTO_INCREMENT,"
                     + " PlayerUuidMost BIGINT NOT NULL,"
                     + " PlayerUuidLeast BIGINT NOT NULL,"
                     + " PlayerTags INT NOT NULL DEFAULT(1),"
                     + " PRIMARY KEY (PlayerId)" +
                     ");";

        try
        {
            Statement statement = TagConnection.getConnection().createStatement();
            statement.execute(sql);
        }
        catch (SQLException ex)
        {
            ex.printStackTrace();
        }
    }

    /**
     * Adds a tag for the given player.
     *
     * @param uuid the player to add a tag for.
     */
    public static void addTag(UUID uuid, Runnable runnable)
    {
        // call the database action asynchronously
        Bukkit.getScheduler().runTaskAsynchronously(AltitudeTag.getInstance(), () ->
        {
            String sql;
            // if they've tagged before we want to update rather than insert
            if (hasTagged(uuid))
            {
                sql = "UPDATE Players SET PlayerTags = PlayerTags + 1 WHERE PlayerUuidMost = ? AND PlayerUuidLeast = ?;";
            }
            else
            {
                sql = "INSERT INTO Players (PlayerUuidMost, PlayerUuidLeast) VALUES (?, ?);";
            }
            // prepare the statement
            try (PreparedStatement ps = TagConnection.getConnection().prepareStatement(sql))
            {
                // set the parameters
                ps.setLong(1, uuid.getMostSignificantBits());
                ps.setLong(2, uuid.getLeastSignificantBits());

                // execute the code
                ps.execute();

                // run the runnable if it's not null
                if (runnable != null)
                {
                    runnable.run();
                }
            }
            catch (SQLException ex)
            {
                ex.printStackTrace();
            }
        });
    }

    /**
     * Returns the number of tags the given player has.
     *
     * @param uuid the player's uuid.
     */
    public static void getTags(UUID uuid, Consumer<Integer> consumer)
    {
        Bukkit.getScheduler().runTaskAsynchronously(AltitudeTag.getInstance(), () ->
        {
            String sql = "SELECT PlayerTags FROM Players WHERE PlayerUuidMost = ? AND PlayerUuidLeast = ?;";

            try (PreparedStatement ps = TagConnection.getConnection().prepareStatement(sql))
            {
                ps.setLong(1, uuid.getMostSignificantBits());
                ps.setLong(2, uuid.getLeastSignificantBits());

                ResultSet rs = ps.getResultSet();

                if (rs.next())
                {
                    // call the consumer when the query returns back
                    consumer.accept(rs.getInt(1));
                }

            }
            catch (SQLException ex)
            {
                ex.printStackTrace();
            }
        });
    }

    public static boolean hasTagged(UUID uuid)
    {
        String sql = "SELECT COUNT(*) FROM Players WHERE PlayerUuidMost = ? AND PlayerUuidLeast = ?;";

        try (PreparedStatement ps = TagConnection.getConnection().prepareStatement(sql))
        {
            ps.setLong(1, uuid.getMostSignificantBits());
            ps.setLong(2, uuid.getLeastSignificantBits());

            ResultSet rs = ps.getResultSet();

            return rs.next() && rs.getInt(1) > 0;
        }
        catch (SQLException ex)
        {
            ex.printStackTrace();
        }

        return false;
    }
}
