package com.alttd.altitudetag.listeners;

import java.util.Optional;

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

        Optional<? extends Player> optional = Bukkit.getOnlinePlayers().stream().filter(p -> p != event.getPlayer()).findAny();
        if (!optional.isPresent())
        {
            throw new IllegalStateException("There is more than one player on but for some reason they are all: " + event.getPlayer().getUniqueId());
        }
        Player player = optional.get();
        AltitudeTag.setTagger(player.getUniqueId(), TagCause.DISCONNECT_TRANSFER);
        NotificationHandler.sendVictimTitle(player, false);
    }
}
