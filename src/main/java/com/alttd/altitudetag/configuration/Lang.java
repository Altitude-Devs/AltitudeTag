package com.alttd.altitudetag.configuration;

import java.io.File;
import java.util.Arrays;

import com.alttd.altitudeapi.utils.CollectionUtils;
import com.alttd.altitudeapi.utils.StringUtils;
import com.alttd.altitudetag.AltitudeTag;
import org.apache.commons.lang.mutable.MutableBoolean;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

/**
 * The system for processing and sending messages to players.
 *
 * @author Michael Ziluck
 */
public enum Lang
{
    /**
     * The prefix before most of the lang messages.
     */
    PREFIX("prefix", "&3[&bAltitudeTag&3] &f{message}"),
    /**
     * When a player misuses a command.
     */
    USAGE("usage_message", "&6&lUSAGE &e» &f{usage}"),
    /**
     * When players do not have permission to do something.
     */
    NO_PERMS("no_permissions", "You don't have permission to do that."),
    /**
     * When a player does not have access to any sub-commands.
     */
    NO_SUBS("no_sub_access", "You don't have access to any sub-commands."),
    /**
     * When the configuration files were successfully reloaded.
     */
    RELOAD("reload", "Reloaded the config and lang files."),
    /**
     * The header and footer for all commands.
     */
    HEADER_FOOTER("header_footer", "&7&m-----------------------------------"),
    /**
     * When the console tries to run a player-only command.
     */
    ONLY_PLAYERS("only_players", "Only players can run that command."),
    SET_LEADERBOARD_LOCATION("set_leaderboard_location", "Successfully set the leaderboard location.");

    private String[] message;

    private String path;

    Lang(String path, String... message)
    {
        this.path = path;
        this.message = message;
    }

    /**
     * Retrieves the message for this Lang object. This can be changed by editing the language configuration files, so
     * they should NOT be treated as constants. Additionally their Strings should NOT be stored to reference anything.
     *
     * @return the message for this Lang object.
     */
    public String[] getRawMessage()
    {
        return message;
    }

    /**
     * Sets the message for this Lang object. This should not be done after startup to ensure data security.
     *
     * @param message the new message.
     */
    public void setRawMessage(String... message)
    {
        this.message = message;
    }

    /**
     * Retrieves the message for this Lang object. This can be changed by editing the language configuration files, so
     * they should NOT be treated as constants. Additionally, their Strings should NOT be stored to reference anything.
     * Lastly, this returns the combined version of the message in the case that there are multiple.
     *
     * @return the message for this Lang object.
     */
    public String getRawMessageCompiled()
    {
        return StringUtils.compile(message);
    }

    /**
     * @return the path of option in the lang.yml file.
     */
    public String getPath()
    {
        return path;
    }

    /**
     * Sends this Lang object to the CommandSender target. The parameters replace all placeholders that exist in the
     * String as well.
     *
     * @param sender     the CommandSender receiving the message.
     * @param parameters all additional arguments to fill placeholders.
     */
    public void send(CommandSender sender, Object... parameters)
    {
        sender.sendMessage(getMessage(parameters));
    }

    /**
     * Renders this message and returns it. Similar behavior to {@link #send(CommandSender, Object...)}, but instead of sending the message, it simply returns it.
     *
     * @param parameters all additional arguments to fill placeholders.
     *
     * @return the compiled message.
     */
    public String[] getMessage(Object... parameters)
    {
        String[] args = Arrays.copyOf(message, message.length);
        for (int i = 0; i < args.length; i++)
        {
            args[i] = renderString(args[i], parameters);
        }
        return args;
    }

    /**
     * Render a string with the proper parameters.
     *
     * @param string the rendered string.
     * @param args   the placeholders and proper content.
     *
     * @return the rendered string.
     */
    public static String renderString(String string, Object... args)
    {
        if (args.length % 2 != 0)
        {
            throw new IllegalArgumentException("Message rendering requires arguments of an even number. " + Arrays.toString(args) + " given.");
        }

        for (int i = 0; i < args.length; i += 2)
        {
            string = string.replace(args[i].toString(), CollectionUtils.firstNonNull(args[i + 1], "").toString());
        }

        return string;
    }

    public static void update()
    {
        File langFile = new File(AltitudeTag.getInstance().getDataFolder(), "lang.yml");
        FileConfiguration config = YamlConfiguration.loadConfiguration(langFile);

        final MutableBoolean save = new MutableBoolean(false);

        for (Lang lang : values())
        {
            if (!config.isSet(lang.getPath()) || !Config.successful(() -> lang.setRawMessage(config.getString(lang.getPath()))))
            {
                config.set(lang.getPath(), lang.getRawMessage());
                error(lang.getPath());
                if (!save.booleanValue())
                {
                    save.setValue(true);
                }
            }
        }
    }

    /**
     * Alerts the console that there was an error loading a config value.
     *
     * @param location the location that caused an error.
     */
    private static void error(String location)
    {
        AltitudeTag.getInstance().getLogger().severe("Error loading the lang value '" + location + "'. Reverted it to default.");
    }
}
