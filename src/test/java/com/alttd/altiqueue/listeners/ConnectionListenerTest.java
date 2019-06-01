package com.alttd.altiqueue.listeners;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.alttd.altitudetag.AltitudeTag;
import com.alttd.altitudetag.NotificationHandler;
import com.alttd.altitudetag.listeners.ConnectionListener;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.doReturn;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.verifyStatic;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ AltitudeTag.class, Bukkit.class, NotificationHandler.class })
public class ConnectionListenerTest
{
    private ConnectionListener listener;

    private PlayerJoinEvent joinEvent;

    private PlayerQuitEvent quitEvent;

    private Player player;
    private Player differentPlayer;

    private UUID playerUuid;
    private UUID differentUuid;

    private List<Player> players;

    @Before
    public void setup()
    {
        // create the uuids used
        differentUuid = UUID.fromString("00000000-0000-0000-0000-000000000001");
        playerUuid = UUID.fromString("00000000-0000-0000-0000-000000000000");

        listener = new ConnectionListener();

        // set up the base player
        player = mock(Player.class);
        when(player.getUniqueId()).thenReturn(playerUuid);

        // set up the other player
        differentPlayer = mock(Player.class);
        when(differentPlayer.getUniqueId()).thenReturn(differentUuid);

        // create the list of players
        players = new ArrayList<>();
        players.add(player);
        players.add(differentPlayer);

        // Bukkit is mine now!
        mockStatic(Bukkit.class);

        // AltitudeTag is... still mine!
        mockStatic(AltitudeTag.class);
        when(AltitudeTag.setTagger(any())).thenCallRealMethod();

        // NotificationHandler is... ah you get the point
        mockStatic(NotificationHandler.class);

        // do return players when Bukkit.getOnlinePlayers() is called
        doReturn(players).when(Bukkit.class);
        Bukkit.getOnlinePlayers();

        // set the instance of AltitudeTag
        Whitebox.setInternalState(AltitudeTag.class, "instance", mock(AltitudeTag.class));

        joinEvent = new PlayerJoinEvent(player, "");
        quitEvent = new PlayerQuitEvent(player, "");
    }

    @Test
    public void test_join_no_tagger()
    {
        // test when there is no tagger
        when(AltitudeTag.getTagger()).thenReturn(null).thenCallRealMethod();

        listener.onJoin(joinEvent);

        assertSame(playerUuid, AltitudeTag.getTagger());
        verifyStatic(NotificationHandler.class, times(1));
        NotificationHandler.sendVictimTitle(player, true);
    }

    @Test
    public void test_join_active_tagger()
    {
        // test when there is a tagger
        when(AltitudeTag.getTagger()).thenReturn(differentUuid);

        listener.onJoin(joinEvent);

        verify(player, never()).sendMessage(any(String.class));
        verify(player, never()).sendMessage(any(String[].class));
        assertNotSame(playerUuid, AltitudeTag.getTagger());
        verifyStatic(Bukkit.class, never());
        Bukkit.getOnlinePlayers();
    }

    @Test
    public void test_quit_not_tagger()
    {
        // test when they leave and aren't the tagger
        when(AltitudeTag.getTagger()).thenReturn(differentUuid);

        listener.onLeave(quitEvent);

        verifyStatic(Bukkit.class, never());
        Bukkit.getOnlinePlayers();
    }

    @Test
    public void test_quit_active_tagger_no_others()
    {
        // set the tagger to be the event player
        when(AltitudeTag.getTagger()).thenReturn(playerUuid);
        // make the event player be the only one online
        players.remove(differentPlayer);

        listener.onLeave(quitEvent);

        verifyStatic(AltitudeTag.class, times(1));
        AltitudeTag.setTagger(null);
    }

    @Test
    public void test_quit_active_tagger_are_others()
    {
        when(AltitudeTag.getTagger()).thenReturn(playerUuid);

        listener.onLeave(quitEvent);

        verifyStatic(AltitudeTag.class, times(1));
        AltitudeTag.setTagger(differentUuid);
    }
}
