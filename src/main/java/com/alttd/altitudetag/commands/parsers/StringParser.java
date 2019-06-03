package com.alttd.altitudetag.commands.parsers;

import java.util.List;

import com.alttd.altitudeapi.commands.CommandHandler;
import com.alttd.altitudeapi.commands.Parser;
import org.bukkit.command.CommandSender;

public class StringParser implements Parser<String>
{
    @Override
    public String parseArgument(CommandSender sender, String[] label, String rawArgument)
    {
        return rawArgument;
    }

    @Override
    public List<String> getRecommendations(CommandSender sender, String lastWord)
    {
        return CommandHandler.defaultTabComplete(sender, lastWord);
    }
}
