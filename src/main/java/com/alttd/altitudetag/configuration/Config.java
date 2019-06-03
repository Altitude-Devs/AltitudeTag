package com.alttd.altitudetag.configuration;

import java.util.Objects;

import com.alttd.altitudeapi.utils.MutableValue;
import com.alttd.altitudetag.AltitudeTag;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;

public final class Config
{
    /**
     * The plugin's version
     */
    public static final MutableValue<String> VERSION = new MutableValue<>("1.1.2");

    /**
     * The hostname of the database if one is used.
     */
    public static final MutableValue<String> DATABASE_HOSTNAME = new MutableValue<>("localhost");

    /**
     * The port of the database if one is used.
     */
    public static final MutableValue<Integer> DATABASE_PORT = new MutableValue<>(27017);

    /**
     * The username of the user for the database if one is used.
     */
    public static final MutableValue<String> DATABASE_USERNAME = new MutableValue<>("root");

    /**
     * The password of the database user if one is used.
     */
    public static final MutableValue<String> DATABASE_PASSWORD = new MutableValue<>("password");

    /**
     * The database to use in the database if one is used.
     */
    public static final MutableValue<String> DATABASE_DATABASE = new MutableValue<>("factions");

    /**
     * How long the system should wait when trying to connect to a database.
     */
    public static final MutableValue<Integer> DATABASE_TIMEOUT = new MutableValue<>(100);

    /**
     * The description given to a connection if the DBMS supports that feature.
     */
    public static final MutableValue<String> DATABASE_DESCRIPTION = new MutableValue<>("Altitude Connection");

    public static final MutableValue<Boolean> LEADERBOARD_ENABLED = new MutableValue<>(true);
    public static final MutableValue<String>  LEADERBOARD_TITLE   = new MutableValue<>("&b&lAltitude Tag Leaderboard");
    public static final MutableValue<String>  LEADERBOARD_FORMAT  = new MutableValue<>("&6{rank}. &e{player} &7- &e{tags}");
    public static final MutableValue<Integer> LEADERBOARD_TOP     = new MutableValue<>(10);

    public static final MutableValue<String> LEADERBOARD_LOCATION_WORLD = new MutableValue<>("world");
    public static final MutableValue<Double> LEADERBOARD_LOCATION_X     = new MutableValue<>(10.0);
    public static final MutableValue<Double> LEADERBOARD_LOCATION_Y     = new MutableValue<>(50.0);
    public static final MutableValue<Double> LEADERBOARD_LOCATION_Z     = new MutableValue<>(10.0);

    public static final MutableValue<Boolean> NOTIFICATION_VICTIM_TITLE_ENABLED  = new MutableValue<>(true);
    public static final MutableValue<String>  NOTIFICATION_VICTIM_TITLE_MESSAGE  = new MutableValue<>("&6Tag");
    public static final MutableValue<Integer> NOTIFICATION_VICTIM_TITLE_DURATION = new MutableValue<>(100);
    public static final MutableValue<Integer> NOTIFICATION_VICTIM_TITLE_FADE_IN  = new MutableValue<>(0);
    public static final MutableValue<Integer> NOTIFICATION_VICTIM_TITLE_FADE_OUT = new MutableValue<>(0);

    public static final MutableValue<Boolean> NOTIFICATION_VICTIM_TITLE_SUB_TITLE_ENABLED = new MutableValue<>(true);
    public static final MutableValue<String>  NOTIFICATION_VICTIM_TITLE_SUB_TITLE_MESSAGE = new MutableValue<>("&eYou're it!");

    public static final MutableValue<Boolean> NOTIFICATION_VICTIM_TITLE_JOIN_DELAY_ENABLED = new MutableValue<>(true);
    public static final MutableValue<Integer> NOTIFICATION_VICTIM_TITLE_JOIN_DELAY_LENGTH  = new MutableValue<>(60);

    public static final MutableValue<Boolean> NOTIFICATION_ATTACKER_CHAT_ENABLED = new MutableValue<>(true);
    public static final MutableValue<String>  NOTIFICATION_ATTACKER_CHAT_MESSAGE = new MutableValue<>("&7You just tagged &e{target}&7! You have &e{amount}&7 total tags.");

    public static final MutableValue<Boolean> NOTIFICATION_GLOBAL_CHAT_AFTER_TAG_ENABLED = new MutableValue<>(true);
    public static final MutableValue<String>  NOTIFICATION_GLOBAL_CHAT_AFTER_TAG_MESSAGE = new MutableValue<>("&e* {attacker} tagged {target}, run!");

    public static final MutableValue<Boolean> NOTIFICATION_GLOBAL_CHAT_OTHER_REASON_ENABLED = new MutableValue<>(true);
    public static final MutableValue<String>  NOTIFICATION_GLOBAL_CHAT_OTHER_REASON_MESSAGE = new MutableValue<>("&e*{target} is it now, run!");

    public static final MutableValue<Boolean> NOTIFICATION_GLOBAL_BOSS_BAR_ENABLED  = new MutableValue<>(true);
    public static final MutableValue<Integer> NOTIFICATION_GLOBAL_BOSS_BAR_SEGMENTS = new MutableValue<>(0);
    public static final MutableValue<String>  NOTIFICATION_GLOBAL_BOSS_BAR_COLOR    = new MutableValue<>("YELLOW");
    public static final MutableValue<Integer> NOTIFICATION_GLOBAL_BOSS_BAR_PERCENT  = new MutableValue<>(0);
    public static final MutableValue<String>  NOTIFICATION_GLOBAL_BOSS_BAR_MESSAGE  = new MutableValue<>("&e{player} is 'it'! Don't let them tag you!");

    public static final MutableValue<Boolean> TAG_BACKS_TRACK_LEADERBOARD = new MutableValue<>(false);
    public static final MutableValue<String> TAG_BACKS_MESSAGE = new MutableValue<>("&e* Have fun, but tag-backs aren't regarded toward the leaderboard.");

    public static final MutableValue<Boolean> TAG_DELAY_ENABLED  = new MutableValue<>(true);
    public static final MutableValue<Integer> TAG_DELAY_DURATION = new MutableValue<>(60);
    public static final MutableValue<String> TAG_DELAY_MESSAGE = new MutableValue<>("&e* Can't tag anyone within 5 seconds of becoming 'it'!");

    /**
     * Update the values from the config file.
     */
    public static void update()
    {
        FileConfiguration config = AltitudeTag.getInstance().getConfig();

        MutableValue<Boolean> save = new MutableValue<>(false);

        // how to optimize operations
        updateValue(config, save, "version", VERSION);

        // database connection information
        updateValue(config, save, "database.hostname", DATABASE_HOSTNAME);
        updateValue(config, save, "database.port", DATABASE_PORT);
        updateValue(config, save, "database.username", DATABASE_USERNAME);
        updateValue(config, save, "database.password", DATABASE_PASSWORD);
        updateValue(config, save, "database.database", DATABASE_DATABASE);
        updateValue(config, save, "database.timeout", DATABASE_TIMEOUT);
        updateValue(config, save, "database.description", DATABASE_DESCRIPTION);

        // leaderboard values
        updateValue(config, save, "leaderboard.enabled", LEADERBOARD_ENABLED);
        updateValue(config, save, "leaderboard.title", LEADERBOARD_TITLE);
        updateValue(config, save, "leaderboard.format", LEADERBOARD_FORMAT);
        updateValue(config, save, "leaderboard.top", LEADERBOARD_TOP);
        updateValue(config, save, "leaderboard.location.world", LEADERBOARD_LOCATION_WORLD);
        updateValue(config, save, "leaderboard.location.x", LEADERBOARD_LOCATION_X);
        updateValue(config, save, "leaderboard.location.y", LEADERBOARD_LOCATION_Y);
        updateValue(config, save, "leaderboard.location.z", LEADERBOARD_LOCATION_Z);

        // notification values
        updateValue(config, save, "notification.victim.title.enabled", NOTIFICATION_VICTIM_TITLE_ENABLED);
        updateValue(config, save, "notification.victim.title.message", NOTIFICATION_VICTIM_TITLE_MESSAGE);
        updateValue(config, save, "notification.victim.title.duration", NOTIFICATION_VICTIM_TITLE_DURATION);
        updateValue(config, save, "notification.victim.title.fade-in", NOTIFICATION_VICTIM_TITLE_FADE_IN);
        updateValue(config, save, "notification.victim.title.fade-out", NOTIFICATION_VICTIM_TITLE_FADE_OUT);
        updateValue(config, save, "notification.victim.title.sub-title.enabled", NOTIFICATION_VICTIM_TITLE_SUB_TITLE_ENABLED);
        updateValue(config, save, "notification.victim.title.sub-title.message", NOTIFICATION_VICTIM_TITLE_SUB_TITLE_MESSAGE);
        updateValue(config, save, "notification.victim.title.join-delay.enabled", NOTIFICATION_VICTIM_TITLE_JOIN_DELAY_ENABLED);
        updateValue(config, save, "notification.victim.title.join-delay.length", NOTIFICATION_VICTIM_TITLE_JOIN_DELAY_LENGTH);
        updateValue(config, save, "notification.attacker.chat.enabled", NOTIFICATION_ATTACKER_CHAT_ENABLED);
        updateValue(config, save, "notification.attacker.chat.message", NOTIFICATION_ATTACKER_CHAT_MESSAGE);
        updateValue(config, save, "notification.global.chat.after-tag.enabled", NOTIFICATION_GLOBAL_CHAT_AFTER_TAG_ENABLED);
        updateValue(config, save, "notification.global.chat.after-tag.message", NOTIFICATION_GLOBAL_CHAT_AFTER_TAG_MESSAGE);
        updateValue(config, save, "notification.global.chat.other-reason.enabled", NOTIFICATION_GLOBAL_CHAT_OTHER_REASON_ENABLED);
        updateValue(config, save, "notification.global.chat.other-reason.message", NOTIFICATION_GLOBAL_CHAT_OTHER_REASON_MESSAGE);
        updateValue(config, save, "notification.global.boss-bar.enabled", NOTIFICATION_GLOBAL_BOSS_BAR_ENABLED);
        updateValue(config, save, "notification.global.boss-bar.segments", NOTIFICATION_GLOBAL_BOSS_BAR_SEGMENTS);
        updateValue(config, save, "notification.global.boss-bar.color", NOTIFICATION_GLOBAL_BOSS_BAR_COLOR);
        updateValue(config, save, "notification.global.boss-bar.percent", NOTIFICATION_GLOBAL_BOSS_BAR_PERCENT);
        updateValue(config, save, "notification.global.boss-bar.message", NOTIFICATION_GLOBAL_BOSS_BAR_MESSAGE);

        if (save.getValue())
        {
            AltitudeTag.getInstance().saveConfig();
        }
    }

    /**
     * Updates the configuration with the given information. If the value fails to load from the config because it does
     * not exist or it is in an invalid format, the system will notify the console.
     *
     * @param config   the config file to load/update.
     * @param location the location in the config.
     * @param mutable  the mutable value to update.
     */
    private static <T> void updateValue(FileConfiguration config, MutableValue<Boolean> save, String location, MutableValue<T> mutable)
    {
        if (!config.isSet(location) || !successful(() -> mutable.setValue(loadValue(config, mutable.getType(), location))))
        {
            error(location);
            config.set(location, mutable.getValue().toString());
            if (!save.getValue())
            {
                save.setValue(true);
            }
        }
    }

    /**
     * Used to check if an operation throws an exception with ease.
     *
     * @param runnable the operation to run.
     *
     * @return {@code true} if the operation does NOT throw an exception.<br>
     * {@code false} if the operation DOES throw an exception.
     */
    protected static boolean successful(Runnable runnable)
    {
        try
        {
            runnable.run();
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * Alerts the console that there was an error loading a config value.
     *
     * @param location the location that caused an error.
     */
    private static void error(String location)
    {
        AltitudeTag.getInstance().getLogger().severe("Error loading the config value '" + location + "'. Reverted it to default.");
    }

    @SuppressWarnings("unchecked")
    public static <T> T loadValue(FileConfiguration config, Class<? super T> clazz, String location)
    {
        if (config == null)
        {
            throw new IllegalArgumentException("Config parameter can't be null.");
        }
        if (clazz == null)
        {
            throw new IllegalArgumentException("Class parameter can't be null.");
        }

        if (clazz == Integer.class)
        {
            return (T) Integer.valueOf(config.getInt(location));
        }
        else if (clazz == String.class)
        {
            return (T) ChatColor.translateAlternateColorCodes('&', config.getString(location));
        }
        else if (clazz == Boolean.class)
        {
            return (T) Boolean.valueOf(config.getBoolean(location));
        }
        else if (clazz == Double.class)
        {
            return (T) Double.valueOf(config.getDouble(location));
        }
        else if (Enum.class.isAssignableFrom(clazz))
        {
            return (T) Enum.valueOf((Class<? extends Enum>) clazz, Objects.requireNonNull(config.getString(location)));
        }

        // TODO throw exception since the type is weird
        return null;
    }
}
