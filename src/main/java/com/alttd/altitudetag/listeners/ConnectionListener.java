package com.alttd.altitudetag.listeners;

import com.alttd.altitudetag.AltitudeTag;
import com.alttd.altitudetag.NotificationHandler;
import com.alttd.altitudetag.TagCause;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class ConnectionListener implements Listener
{
    @EventHandler
    public void onJoin(PlayerJoinEvent event)
    {
        if (AltitudeTag.getTagger() == null)
        {
            AltitudeTag.setTagger(event.getPlayer().getUniqueId(), TagCause.FIRST_CONNECT);
        }
        if (AltitudeTag.getBossBar() != null)
        {
            AltitudeTag.getBossBar().addPlayer(event.getPlayer());
        }
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent event)
    {
        if (!event.getPlayer().getUniqueId().equals(AltitudeTag.getTagger()))
        {
            return;
        }

        if (Bukkit.getOnlinePlayers().size() == 1)
        {
            AltitudeTag.setTagger(null, TagCause.DISCONNECT_EMPTY);
            return;
        }

        Player player = AltitudeTag.randomTagger(TagCause.DISCONNECT_TRANSFER, event.getPlayer());

        NotificationHandler.sendVictimTitle(player, false);
    }
}
