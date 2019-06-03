package com.alttd.altitudetag.commands;

import com.alttd.altitudeapi.commands.ValidBaseCommand;
import com.alttd.altitudetag.AltitudeTag;
import com.alttd.altitudetag.configuration.Config;
import com.alttd.altitudetag.configuration.Lang;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

public class TagCommand extends ValidBaseCommand
{
    public TagCommand()
    {
        super("tag", "Tag, you're it!", "tag.play", new String[]{ "tags", "tagging", "tagger" });

        addSubCommand(new TagAdminCommand());
    }

    @Override
    public void help(CommandSender sender, String[] label)
    {
        if (sender.hasPermission(getSubCommand("admin").getPermission()))
        {
            sender.sendMessage(Lang.renderString(Config.NOTIFICATION_GLOBAL_BOSS_BAR_MESSAGE.getValue(),
                                                 "{player}", Bukkit.getOfflinePlayer(AltitudeTag.getTagger()).getName()));
        }
        else
        {
            super.help(sender, label);
        }
    }
}
