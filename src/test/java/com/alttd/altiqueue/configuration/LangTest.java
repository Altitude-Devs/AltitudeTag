package com.alttd.altiqueue.configuration;

import java.io.File;

import com.alttd.altitudetag.configuration.Lang;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class LangTest
{
    private FileConfiguration config;

    @Before
    public void setup()
    {
        File file = new File("src/main/resources/lang.yml");

        config = YamlConfiguration.loadConfiguration(file);
    }

    @Test
    public void test_file_contains_options()
    {
        for (Lang lang : Lang.values())
        {
            System.out.println(lang.getPath() + ": \"" + lang.getRawMessageCompiled() + "\"");
        }
        for (Lang lang : Lang.values())
        {
            if (!config.contains(lang.getPath()))
            {
                Assert.fail("Value missing from lang.yml: " + lang.name());
            }
        }
    }

    @Test
    public void test_defaults_match()
    {
        for (Lang lang : Lang.values())
        {
            if (!config.getString(lang.getPath()).equals(lang.getRawMessageCompiled()))
            {
                Assert.fail("Lang values don't match: " + lang.name());
            }
        }
    }
}
