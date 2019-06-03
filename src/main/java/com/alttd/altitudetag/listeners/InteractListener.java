package com.alttd.altitudetag.listeners;

import com.alttd.altitudetag.AltitudeTag;
import com.alttd.altitudetag.NotificationHandler;
import com.alttd.altitudetag.TagCause;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class InteractListener implements Listener
{
    @EventHandler
    public void onHit(EntityDamageByEntityEvent event)
    {
        if (event.getDamager() instanceof Player && event.getEntity() instanceof Player)
        {
            final Player tagger = (Player) event.getDamager();
            if (!AltitudeTag.getTagger().equals(tagger.getUniqueId()))
            {
                return;
            }

            final Player tagged = (Player) event.getEntity();

            // add the new tag
            AltitudeTag.addTag(tagger.getUniqueId(), () ->
            {
                // if they left, we can stop
                if (tagger.isOnline())
                {
                    NotificationHandler.sendTaggerNotifications(tagger, tagged.getName());
                }
            });

            AltitudeTag.setTagger(tagged.getUniqueId(), TagCause.NORMAL);

            NotificationHandler.sendVictimTitle(tagged, false);
        }
    }
}
