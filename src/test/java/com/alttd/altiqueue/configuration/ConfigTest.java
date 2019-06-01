package com.alttd.altiqueue.configuration;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.junit.Test;

public class ConfigTest
{
    private List<String> updateStrings = new ArrayList<>();

    private List<String> declarationStrings = new ArrayList<>();

    @Test
    public void test_defaults()
    {
        YamlConfiguration config = YamlConfiguration.loadConfiguration(new File("src/main/resources/config.yml"));

        printChildren(config);

        //declarationStrings.forEach(System.out::println);
        //updateStrings.forEach(System.out::println);
    }

    private void printChildren(ConfigurationSection config)
    {
        for (String key : config.getKeys(false))
        {
            if (config.isConfigurationSection(key))
            {
                printChildren(Objects.requireNonNull(config.getConfigurationSection(key)));
            }
            else
            {
                String path = config.getCurrentPath() + "." + key;
                String value = config.isString(key) ? "\"" + config.get(key) + "\"" : Objects.requireNonNull(config.get(key)).toString();
                String type = config.isBoolean(key) ? "Boolean"
                                                    : config.isDouble(key)
                                                      ? "Double"
                                                      : config.isInt(key)
                                                        ? "Integer"
                                                        : "String";
                String enumTitle = path.replace(".", "_").replace("-", "_").toUpperCase();

                updateStrings.add(String.format("updateValue(config, save, \"%s\", %s);", path, enumTitle));
                declarationStrings.add(String.format("public static final MutableValue<%s> %s = new MutableValue<>(%s);", type, enumTitle, value));
            }
        }
    }


}
