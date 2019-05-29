package com.alttd.altitudetag.listeners;

import com.alttd.altitudetag.AltitudeTag;
import com.alttd.altitudetag.configuration.Lang;
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
            Player tagger = (Player) event.getDamager();
            Player tagged = (Player) event.getEntity();

            // add the new tag
            AltitudeTag.addTag(tagger.getUniqueId(), () ->
            {
                // if they're still online...
                if (tagger.isOnline())
                {
                    // get their tags...
                    AltitudeTag.getTags(tagger.getUniqueId(), (tags) ->
                    {
                        // if they're still online...
                        if (tagger.isOnline())
                        {
                            // let em know how they're doing!
                            Lang.TAGGED.send(tagger, "{tags}", tags);
                        }
                    });
                }
            });

            AltitudeTag.setTagger(tagged.getUniqueId());

            Lang.YOURE_IT.send(tagged);
        }
    }
}
