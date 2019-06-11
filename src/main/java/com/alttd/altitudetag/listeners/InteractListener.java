package com.alttd.altitudetag.listeners;

import com.alttd.altitudetag.AltitudeTag;
import com.alttd.altitudetag.NotificationHandler;
import com.alttd.altitudetag.TagCause;
import com.alttd.altitudetag.configuration.Config;
import com.alttd.altitudetag.configuration.Lang;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class InteractListener implements Listener
{
    private static long lastTagTime = -1;

    @EventHandler
    public void onHit(EntityDamageByEntityEvent event)
    {
        if (!(event.getDamager() instanceof Player) || !(event.getEntity() instanceof Player))
        {
            return;
        }

        final Player tagger = (Player) event.getDamager();
        if (!AltitudeTag.getTagger().equals(tagger.getUniqueId()))
        {
            return;
        }

        final Player tagged = (Player) event.getEntity();


        // check for time since last tag
        if (lastTagTime != -1 && Config.TAG_DELAY_ENABLED.getValue() && (System.currentTimeMillis() - lastTagTime) / 50 < Config.TAG_DELAY_DURATION.getValue())
        {
            tagger.sendMessage(Lang.renderString(Config.TAG_DELAY_MESSAGE.getValue()));
            return;
        }

        // add the new tag
        if (!Config.TAG_BACKS_TRACK_LEADERBOARD.getValue() && tagged.getUniqueId().equals(AltitudeTag.getPreviousTagger()))
        {
            tagger.sendMessage(Lang.renderString(Config.TAG_BACKS_MESSAGE.getValue()));
        }
        else
        {
            AltitudeTag.addTag(tagger.getUniqueId(), () ->
            {
                // if they left, we can stop
                if (!tagger.isOnline())
                {
                    return;
                }

                NotificationHandler.sendTaggerNotifications(tagger, tagged.getName());
            });
        }

        AltitudeTag.setTagger(tagged.getUniqueId(), TagCause.NORMAL);

        NotificationHandler.sendVictimTitle(tagged, false);

        lastTagTime = System.currentTimeMillis();
    }
}
