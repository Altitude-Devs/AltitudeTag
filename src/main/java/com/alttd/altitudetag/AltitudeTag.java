package com.alttd.altitudetag;

import java.util.UUID;
import java.util.function.Consumer;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class AltitudeTag extends JavaPlugin
{
    private static AltitudeTag instance;

    private UUID tagger;

    /**
     * Enable the plugin
     */
    public void onEnable()
    {
        instance = this;

        if (!Bukkit.getPluginManager().isPluginEnabled("HolographicDisplays"))
        {
            getLogger().severe("*** HolographicDisplays is not installed or not enabled. ***");
            getLogger().severe("*** This plugin will be disabled. ***");
            this.setEnabled(false);
            return;
        }

        Bukkit.getScheduler().runTaskTimerAsynchronously(this, this::updateLeaderboard, 20, 10);
    }

    private void updateLeaderboard()
    {

    }

    /**
     * Set the current tagger.
     *
     * @param tagger the new tagger.
     *
     * @return the previous tagger.
     */
    public static UUID setTagger(UUID tagger)
    {
        UUID prev = instance.tagger;
        instance.tagger = tagger;

        // announce that a new person is it
        Bukkit.getOnlinePlayers().stream().filter(player -> !player.getUniqueId().equals(tagger)).forEach(player -> player.sendMessage());

        return prev;
    }

    /**
     * Returns the current tagger.
     *
     * @return the current tagger.
     */
    public static UUID getTagger()
    {
        return instance.tagger;
    }

    /**
     * Adds a tag for the given player.
     *
     * @param uuid the player to add a tag for.
     */
    public static void addTag(UUID uuid, Runnable runnable)
    {
        Leaderboard.addTag(uuid, runnable);
    }

    public static void getTags(UUID uuid, Consumer<Integer> consumer)
    {
        Leaderboard.getTags(uuid, consumer);
    }

    /**
     * Returns the singleton instance of this plugin.
     *
     * @return the singleton instance of this plugin.
     */
    public static AltitudeTag getInstance()
    {
        return instance;
    }
}
