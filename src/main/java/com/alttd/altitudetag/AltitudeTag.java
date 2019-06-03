package com.alttd.altitudetag;

import java.io.File;
import java.util.UUID;
import java.util.function.Consumer;

import com.alttd.altitudeapi.commands.CommandHandler;
import com.alttd.altitudeapi.commands.CommandLang;
import com.alttd.altitudetag.commands.TagCommand;
import com.alttd.altitudetag.configuration.Config;
import com.alttd.altitudetag.configuration.Lang;
import com.alttd.altitudetag.listeners.ConnectionListener;
import com.alttd.altitudetag.listeners.InteractListener;
import org.bukkit.Bukkit;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class AltitudeTag extends JavaPlugin
{
    private static AltitudeTag instance;

    private UUID tagger;

    private BossBar bossBar;

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

        reloadConfiguration();

        // update the CommandLang values
        CommandLang.NO_PERMISSION.setValue(Lang.NO_PERMS.getRawMessage()[0]);
        CommandLang.HEADER_FOOTER.setValue(Lang.HEADER_FOOTER.getRawMessage()[0]);
        CommandLang.NO_SUBS.setValue(Lang.NO_SUBS.getRawMessage()[0]);
        CommandLang.ONLY_PLAYERS.setValue(Lang.ONLY_PLAYERS.getRawMessage()[0]);
        CommandLang.USAGE_FORMAT.setValue(Lang.USAGE.getRawMessage()[0]);

        try
        {
            TagConnection.initialize();
        }
        catch (Exception ex)
        {
            getLogger().severe("*** Could not connect to the database. ***");
            getLogger().severe("*** This plugin will be disabled. ***");
            this.setEnabled(false);
            return;
        }
        Leaderboard.initialize();

        NotificationHandler.loadBossBar();

        CommandHandler.initialize();
        CommandHandler.getInstance().registerCommand(new TagCommand(), this);

        Bukkit.getPluginManager().registerEvents(new ConnectionListener(), this);
        Bukkit.getPluginManager().registerEvents(new InteractListener(), this);
    }

    private static void reloadConfiguration()
    {
        // check lang
        File langFile = new File(instance.getDataFolder(), "lang.yml");
        if (!langFile.exists())
        {
            instance.saveResource("lang.yml", false);
        }
        Lang.update();

        // check config
        File configFile = new File(instance.getDataFolder(), "config.yml");
        if (!configFile.exists())
        {
            instance.saveDefaultConfig();
        }
        Config.update();
    }

    public static BossBar getBossBar()
    {
        return instance.bossBar;
    }

    public static void setBossBar(BossBar bossBar)
    {
        instance.bossBar = bossBar;
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
     * Set the current tagger.
     *
     * @param tagger the new tagger.
     */
    public static void setTagger(UUID tagger)
    {
        // announce that a new person is it
        Player previousPlayer = Bukkit.getPlayer(instance.tagger);

        if (tagger != null)
        {
            NotificationHandler.sendGlobalNotifications(previousPlayer != null
                                                        ? previousPlayer.getName()
                                                        : null, Bukkit.getPlayer(tagger).getName());
        }
        instance.tagger = tagger;
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
