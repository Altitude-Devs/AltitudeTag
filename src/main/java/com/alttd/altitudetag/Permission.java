package com.alttd.altitudetag;

public enum Permission
{
    COMMAND_ADMIN("tag.commands.admin"),
    COMMAND_ADMIN_LOCATION("tag.commands.admin.location"),
    QUEUE_COMMAND("altiqueue.queue-command");

    private String permission;

    private Permission(String permission)
    {
        this.permission = permission;
    }

    public String getPermission()
    {
        return permission;
    }

}
