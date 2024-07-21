package com.alttd.altitudetag;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Objects;
import java.util.UUID;
import java.util.function.Consumer;

import com.alttd.altitudetag.configuration.Config;
import com.alttd.altitudetag.configuration.Lang;
import eu.decentsoftware.holograms.api.DHAPI;
import eu.decentsoftware.holograms.api.holograms.Hologram;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;

public class Leaderboard
{
    private static Hologram hologram;

    public static void initialize()
    {
        //language=SQL
        String sql = "CREATE TABLE IF NOT EXISTS Player ("
                     + " player_id INT NOT NULL AUTO_INCREMENT,"
                     + " player_uuid VARCHAR(36) NOT NULL,"
                     + " player_tags INT NOT NULL DEFAULT 0,"
                     + " player_active BIT NOT NULL DEFAULT 1,"
                     + " PRIMARY KEY (player_id),"
                     + " CONSTRAINT uuid_unique UNIQUE (player_uuid)" +
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

        Bukkit.getScheduler().runTaskLater(AltitudeTag.getInstance(), Leaderboard::initializeLeaderboard, 20);
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
            sql = "INSERT INTO Player (player_uuid, player_tags) VALUES (?, 1) ON DUPLICATE KEY UPDATE player_tags = player_tags + 1";

            // prepare the statement
            try (PreparedStatement ps = TagConnection.getConnection().prepareStatement(sql))
            {
                // set the parameters
                ps.setString(1, uuid.toString());

                // execute the code
                ps.execute();

                refreshLeaderboard();

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
            String sql = "SELECT player_tags FROM Player WHERE player_uuid = ?;";

            try (PreparedStatement ps = TagConnection.getConnection().prepareStatement(sql))
            {
                ps.setString(1, uuid.toString());

                ResultSet rs = ps.executeQuery();

                if (rs != null && rs.next())
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

    /**
     * Sets the location of the leaderboard. Changes will also be saved to the config file.
     *
     * @param location the new location for the leaderboard.
     */
    public static void setLocation(Location location)
    {
        Config.LEADERBOARD_LOCATION_WORLD.setValue(location.getWorld().getName());
        Config.LEADERBOARD_LOCATION_X.setValue(location.getX());
        Config.LEADERBOARD_LOCATION_Y.setValue(location.getY());
        Config.LEADERBOARD_LOCATION_Z.setValue(location.getZ());

        FileConfiguration config = AltitudeTag.getInstance().getConfig();
        config.set("leaderboard.location.world", Config.LEADERBOARD_LOCATION_WORLD.getValue());
        config.set("leaderboard.location.x", Config.LEADERBOARD_LOCATION_X.getValue());
        config.set("leaderboard.location.y", Config.LEADERBOARD_LOCATION_Y.getValue());
        config.set("leaderboard.location.z", Config.LEADERBOARD_LOCATION_Z.getValue());
        AltitudeTag.getInstance().saveConfig();

        hologram.setLocation(location);
        refreshLeaderboard();
    }

    private static void initializeLeaderboard()
    {
        if (Config.LEADERBOARD_ENABLED.getValue())
        {
            hologram = getOrCreateHologram(new Location(Bukkit.getWorld(Config.LEADERBOARD_LOCATION_WORLD.getValue()),
                    Config.LEADERBOARD_LOCATION_X.getValue(),
                    Config.LEADERBOARD_LOCATION_Y.getValue(),
                    Config.LEADERBOARD_LOCATION_Z.getValue()));
            for (int i = 0; i < Config.LEADERBOARD_TOP.getValue() + 1; i++)
            {
                DHAPI.addHologramLine(hologram, "");
            }
            DHAPI.setHologramLine(hologram, 0, Config.LEADERBOARD_TITLE.getValue());

            refreshLeaderboard();
        }
    }

    private static void refreshLeaderboard()
    {
        if (!Config.LEADERBOARD_ENABLED.getValue())
        {
            return;
        }

        Objects.requireNonNull(hologram);

        Bukkit.getScheduler().runTask(AltitudeTag.getInstance(), () -> // should become async again
        {
            String sql = "SELECT player_uuid, player_tags FROM Player ORDER BY player_tags DESC LIMIT ?";
            try (PreparedStatement ps = TagConnection.getConnection().prepareStatement(sql))
            {
                ps.setInt(1, Config.LEADERBOARD_TOP.getValue());
                ResultSet rs = ps.executeQuery();
                for (int i = 0; i < Config.LEADERBOARD_TOP.getValue(); i++)
                {
                    String text;
                    if (rs != null && rs.next())
                    {
                        text = Lang.renderString(Config.LEADERBOARD_FORMAT.getValue(),
                                                 "{rank}", i + 1,
                                                 "{player}", Bukkit.getOfflinePlayer(UUID.fromString(rs.getString("player_uuid"))).getName(),
                                                 "{tags}", rs.getInt("player_tags"));
                    }
                    else
                    {
                        text = "";
                    }
                    DHAPI.setHologramLine(hologram, 1 + i, text);
                }
            }
            catch (SQLException ex)
            {
                ex.printStackTrace();
            }
        });

    }

    public static Hologram getOrCreateHologram(Location location) {
        Hologram hologram1 = DHAPI.getHologram("TagLeaderBoard");
        if (hologram1 != null)
            return hologram1;

        return DHAPI.createHologram("TagLeaderBoard", location);
    }
}
