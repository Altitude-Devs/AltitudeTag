package com.alttd.altitudetag.listeners;

import java.util.Optional;
import java.util.UUID;

import com.alttd.altitudetag.AltitudeTag;
import com.alttd.altitudetag.NotificationHandler;
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
            AltitudeTag.setTagger(event.getPlayer().getUniqueId());

            NotificationHandler.sendVictimTitle(event.getPlayer(), true);
        }
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent event)
    {
        if (event.getPlayer().getUniqueId().equals(AltitudeTag.getTagger()))
        {
            if (Bukkit.getOnlinePlayers().size() == 1)
            {
                AltitudeTag.setTagger(null);
            }
            else
            {
                Optional<? extends Player> optional = Bukkit.getOnlinePlayers().stream().filter(p -> p != event.getPlayer()).findAny();
                if (!optional.isPresent())
                {
                    throw new IllegalStateException("There is more than one player on but for some reason they are all: " + event.getPlayer().getUniqueId());
                }
                UUID uuid = optional.get().getUniqueId();
                AltitudeTag.setTagger(uuid);
                NotificationHandler.sendVictimTitle(optional.get(), false);
            }
        }
    }
}
