package com.alttd.altitudetag.commands.admin;

import java.util.List;

import com.alttd.altitudeapi.commands.CommandArgument;
import com.alttd.altitudeapi.commands.ValidCommand;
import com.alttd.altitudetag.Leaderboard;
import com.alttd.altitudetag.Permission;
import com.alttd.altitudetag.configuration.Lang;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TagAdminLocationCommand extends ValidCommand
{
    public TagAdminLocationCommand()
    {
        super("location",
              "Sets the leaderboard location",
              Permission.COMMAND_ADMIN_LOCATION.getPermission(),
              true,
              new String[]{ "loc", "setloc", "setlocation", "leaderboard", "board" });
    }

    @Override
    public void validRun(CommandSender commandSender, String[] strings, List<CommandArgument<?>> list)
    {
        Player player = (Player) commandSender;

        Leaderboard.setLocation(player.getLocation());

        Lang.SET_LEADERBOARD_LOCATION.send(player);
    }
}
