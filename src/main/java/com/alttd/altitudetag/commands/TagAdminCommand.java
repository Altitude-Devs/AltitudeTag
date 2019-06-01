package com.alttd.altitudetag.commands;

import com.alttd.altitudeapi.commands.ValidBaseCommand;
import com.alttd.altitudetag.Permission;
import com.alttd.altitudetag.commands.admin.TagAdminLocationCommand;

public class TagAdminCommand extends ValidBaseCommand
{
    protected TagAdminCommand()
    {
        super("admin", "Management commands", Permission.COMMAND_ADMIN.getPermission(), new String[]{ "a" });

        addSubCommand(new TagAdminLocationCommand());
    }
}
