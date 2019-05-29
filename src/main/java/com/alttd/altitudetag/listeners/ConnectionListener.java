package com.alttd.altitudetag.listeners;

import com.alttd.altitudeapi.utils.CollectionUtils;
import com.alttd.altitudetag.AltitudeTag;
import com.alttd.altitudetag.configuration.Lang;
import org.bukkit.Bukkit;
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
            Lang.YOURE_IT.send(event.getPlayer());
        }
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent event)
    {
        if (event.getPlayer().getUniqueId().equals(AltitudeTag.getTagger()))
        {
            AltitudeTag.setTagger(CollectionUtils.randomValue(Bukkit.getOnlinePlayers()).getUniqueId());
            Lang.YOURE_IT.send(event.getPlayer());
        }
    }


}
