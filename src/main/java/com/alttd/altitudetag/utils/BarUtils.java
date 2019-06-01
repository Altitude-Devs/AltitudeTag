package com.alttd.altitudetag.utils;

import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;

public class BarUtils
{

    /**
     * Parses the String and returns a {@link BarStyle}. If a style could not be parsed, this returns
     * {@link BarStyle#SOLID}.
     *
     * @param style the String to parse
     *
     * @return the parsed {@link BarStyle}.
     */
    public static BarStyle parseBarStyle(int style)
    {
        switch (style)
        {
            case 6:
                return BarStyle.SEGMENTED_6;
            case 10:
                return BarStyle.SEGMENTED_10;
            case 12:
                return BarStyle.SEGMENTED_12;
            case 20:
                return BarStyle.SEGMENTED_20;
            default:
                return BarStyle.SOLID;
        }
    }

    /**
     * Parses the String and returns a {@link BarColor}. If a color could not be parsed, this returns
     * {@link BarColor#YELLOW}.
     *
     * @param color the String to parse
     *
     * @return the parsed {@link BarColor}.
     */
    public static BarColor parseBarColor(String color)
    {
        try
        {
            return BarColor.valueOf(color.toUpperCase());
        }
        catch (Exception ex)
        {
            return BarColor.YELLOW;
        }
    }
}
