package com.alttd.altitudetag.configuration;

import java.util.Objects;

import com.alttd.altitudeapi.utils.MutableValue;
import com.alttd.altitudetag.AltitudeTag;
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
    public static final MutableValue<String> DATABASE_CONNECTION_DESCRIPTION = new MutableValue<>("Altitude Connection");

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
        updateValue(config, save, "database.description", DATABASE_CONNECTION_DESCRIPTION);

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
            return (T) config.getString(location);
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
