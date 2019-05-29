package com.alttd.altitudetag;

import java.util.UUID;
import java.util.function.Consumer;

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
