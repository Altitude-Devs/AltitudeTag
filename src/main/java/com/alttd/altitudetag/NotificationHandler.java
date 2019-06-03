package com.alttd.altitudetag;

import com.alttd.altitudetag.configuration.Config;
import com.alttd.altitudetag.configuration.Lang;
import com.alttd.altitudetag.utils.BarUtils;
import com.destroystokyo.paper.Title;
import org.bukkit.Bukkit;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;

public class NotificationHandler
{
    // TODO move all notifications here
    public static void sendVictimTitle(final Player player, boolean connection)
    {
        if (Config.NOTIFICATION_VICTIM_TITLE_ENABLED.getValue())
        {
            Runnable sendTitle = () ->
            {
                if (player.isOnline())
                {
                    Title.Builder builder = Title.builder()
                                                 .fadeIn(Config.NOTIFICATION_VICTIM_TITLE_FADE_IN.getValue())
                                                 .fadeOut(Config.NOTIFICATION_VICTIM_TITLE_FADE_OUT.getValue())
                                                 .stay(Config.NOTIFICATION_VICTIM_TITLE_DURATION.getValue())
                                                 .title(Config.NOTIFICATION_VICTIM_TITLE_MESSAGE.getValue());

                    if (Config.NOTIFICATION_VICTIM_TITLE_SUB_TITLE_ENABLED.getValue())
                    {
                        builder.subtitle(Config.NOTIFICATION_VICTIM_TITLE_SUB_TITLE_MESSAGE.getValue());
                    }

                    player.sendTitle(builder.build());
                }
            };
            if (connection && Config.NOTIFICATION_VICTIM_TITLE_JOIN_DELAY_ENABLED.getValue())
            {
                Bukkit.getScheduler().runTaskLater(AltitudeTag.getInstance(), sendTitle, Config.NOTIFICATION_VICTIM_TITLE_JOIN_DELAY_LENGTH.getValue());
            }
            else
            {
                sendTitle.run();
            }
        }
    }

    public static void sendTaggerNotifications(Player tagger, String tagged)
    {
        // get their tags...
        AltitudeTag.getTags(tagger.getUniqueId(), (tags) ->
        {
            // if they left, we can stop
            if (!tagger.isOnline())
            {
                return;
            }

            if (Config.NOTIFICATION_ATTACKER_CHAT_ENABLED.getValue())
            {
                tagger.sendMessage(Lang.renderString(Config.NOTIFICATION_ATTACKER_CHAT_MESSAGE.getValue(),
                                                     "{target}", tagged,
                                                     "{amount}", tags));
            }
        });
    }

    public static void sendGlobalNotifications(String attacker, String victim)
    {
        if (attacker == null && Config.NOTIFICATION_GLOBAL_CHAT_OTHER_REASON_ENABLED.getValue())
        {
            // TODO make this actually do what it's supposed to
            Bukkit.getOnlinePlayers().forEach(p -> p.sendMessage(Lang.renderString(Config.NOTIFICATION_GLOBAL_CHAT_OTHER_REASON_MESSAGE.getValue(),
                                                                                   "{target}", victim)));
        }
        if (attacker != null && Config.NOTIFICATION_GLOBAL_CHAT_AFTER_TAG_ENABLED.getValue())
        {
            Bukkit.getOnlinePlayers().forEach(p -> p.sendMessage(Lang.renderString(Config.NOTIFICATION_GLOBAL_CHAT_AFTER_TAG_MESSAGE.getValue(),
                                                                                   "{target}", victim,
                                                                                   "{attacker}", attacker)));
        }

        if (Config.NOTIFICATION_GLOBAL_BOSS_BAR_ENABLED.getValue())
        {
            AltitudeTag.getBossBar().setTitle(Lang.renderString(Config.NOTIFICATION_GLOBAL_BOSS_BAR_MESSAGE.getValue(), "{player}", victim,
                                                                BarUtils.parseBarColor(Config.NOTIFICATION_GLOBAL_BOSS_BAR_COLOR.getValue()),
                                                                BarUtils.parseBarStyle(Config.NOTIFICATION_GLOBAL_BOSS_BAR_SEGMENTS.getValue())));
        }
    }

    public static void loadBossBar()
    {
        if (Config.NOTIFICATION_GLOBAL_BOSS_BAR_ENABLED.getValue())
        {
            BossBar bossBar = Bukkit.createBossBar(Lang.renderString(Config.NOTIFICATION_GLOBAL_BOSS_BAR_MESSAGE.getValue(), "{player}", "No One"),
                                                   BarUtils.parseBarColor(Config.NOTIFICATION_GLOBAL_BOSS_BAR_COLOR.getValue()),
                                                   BarUtils.parseBarStyle(Config.NOTIFICATION_GLOBAL_BOSS_BAR_SEGMENTS.getValue()));

            bossBar.setProgress(Config.NOTIFICATION_GLOBAL_BOSS_BAR_PERCENT.getValue() / 100.0);

            AltitudeTag.setBossBar(bossBar);
        }
    }
}
