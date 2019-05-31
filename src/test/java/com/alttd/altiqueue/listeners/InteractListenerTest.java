package com.alttd.altiqueue.listeners;

import java.util.UUID;
import java.util.function.Consumer;

import com.alttd.altitudetag.AltitudeTag;
import com.alttd.altitudetag.configuration.Lang;
import com.alttd.altitudetag.listeners.InteractListener;
import org.bukkit.entity.Player;
import org.bukkit.entity.Sheep;
import org.bukkit.entity.Wolf;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.doNothing;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.spy;
import static org.powermock.api.mockito.PowerMockito.verifyStatic;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ AltitudeTag.class })
public class InteractListenerTest
{
    private InteractListener listener;

    private EntityDamageByEntityEvent event;

    private Player attacker;
    private UUID   attackerUuid = UUID.fromString("00000000-0000-0000-0000-000000000001");

    private Player victim;
    private UUID   victimUuid = UUID.fromString("00000000-0000-0000-0000-000000000000");

    @Before
    public void setup()
    {
        listener = new InteractListener();
    }

    private void setup_both_players()
    {
        attacker = mock(Player.class);
        when(attacker.getUniqueId()).thenReturn(attackerUuid);

        victim = mock(Player.class);
        when(victim.getUniqueId()).thenReturn(victimUuid);

        event = spy(new EntityDamageByEntityEvent(attacker, victim, DamageCause.ENTITY_ATTACK, 1d));

        mockStatic(AltitudeTag.class);
        when(AltitudeTag.getTagger()).thenCallRealMethod();
        when(AltitudeTag.setTagger(any())).thenCallRealMethod();

        Whitebox.setInternalState(AltitudeTag.class, "instance", mock(AltitudeTag.class));
    }

    @Test
    public void test_two_players_normal()
    {
        setup_both_players();

        // override addTag
        ArgumentCaptor<Runnable> runnableCaptor = ArgumentCaptor.forClass(Runnable.class);

        doNothing().when(AltitudeTag.class);
        AltitudeTag.addTag(eq(attackerUuid), runnableCaptor.capture());

        // override getTag
        ArgumentCaptor<Consumer> consumerCaptor = ArgumentCaptor.forClass(Consumer.class);

        doNothing().when(AltitudeTag.class);
        AltitudeTag.getTags(eq(attackerUuid), consumerCaptor.capture());

        // they're always online
        when(attacker.isOnline()).thenReturn(true);

        // call the event
        listener.onHit(event);

        // capture the runnable and run it
        Runnable addTagRunnable = runnableCaptor.getValue();
        addTagRunnable.run();

        // capture the consumer and run it
        Consumer<Integer> getTagsConsumer = consumerCaptor.getValue();
        getTagsConsumer.accept(10);

        verify(attacker, times(1)).sendMessage(Lang.TAGGED.getMessage("{tags}", 10));
        assertEquals(victimUuid, AltitudeTag.getTagger());
        verify(victim, times(1)).sendMessage(Lang.YOURE_IT.getMessage());
    }

    @Test
    public void test_two_players_tagger_leave_before_message()
    {
        setup_both_players();

        // override addTag
        ArgumentCaptor<Runnable> runnableCaptor = ArgumentCaptor.forClass(Runnable.class);
        doNothing().when(AltitudeTag.class);
        AltitudeTag.addTag(eq(attackerUuid), runnableCaptor.capture());

        // override getTag
        ArgumentCaptor<Consumer> consumerCaptor = ArgumentCaptor.forClass(Consumer.class);
        doNothing().when(AltitudeTag.class);
        AltitudeTag.getTags(eq(attackerUuid), consumerCaptor.capture());

        // they're always online
        when(attacker.isOnline()).thenReturn(true).thenReturn(false);

        // call the event
        listener.onHit(event);

        // capture the runnable and run it
        Runnable addTagRunnable = runnableCaptor.getValue();
        addTagRunnable.run();

        // capture the consumer and run it
        Consumer<Integer> getTagsConsumer = consumerCaptor.getValue();
        getTagsConsumer.accept(10);

        // assertions
        verify(attacker, times(2)).isOnline();

        verify(attacker, never()).sendMessage((String) any());
        verify(attacker, never()).sendMessage((String[]) any());

        assertEquals(victimUuid, AltitudeTag.getTagger());

        verify(victim, times(1)).sendMessage(Lang.YOURE_IT.getMessage());
    }

    @Test
    public void test_two_players_tagger_leave_before_get_tags()
    {
        setup_both_players();

        // override addTag
        ArgumentCaptor<Runnable> runnableCaptor = ArgumentCaptor.forClass(Runnable.class);

        doNothing().when(AltitudeTag.class);
        AltitudeTag.addTag(eq(attackerUuid), runnableCaptor.capture());

        // they're always offline
        when(attacker.isOnline()).thenReturn(false);

        // call the event
        listener.onHit(event);

        // capture the runnable and run it
        Runnable addTagRunnable = runnableCaptor.getValue();
        addTagRunnable.run();

        verify(attacker, never()).sendMessage((String) any());
        verify(attacker, never()).sendMessage((String[]) any());

        verifyStatic(AltitudeTag.class, never());
        AltitudeTag.getTags(any(), any());

        assertEquals(victimUuid, AltitudeTag.getTagger());
        verify(victim, times(1)).sendMessage(Lang.YOURE_IT.getMessage());
    }

    @Test
    public void test_no_players()
    {
        Wolf attacker = mock(Wolf.class);
        Sheep victim = mock(Sheep.class);

        event = spy(new EntityDamageByEntityEvent(attacker, victim, DamageCause.ENTITY_ATTACK, 1d));

        listener.onHit(event);

        verify(event, never()).getEntity();
    }

    @Test
    public void test_player_attacker_non_player_victim()
    {
        Player attacker = mock(Player.class);
        Sheep victim = mock(Sheep.class);

        event = spy(new EntityDamageByEntityEvent(attacker, victim, DamageCause.ENTITY_ATTACK, 1d));

        listener.onHit(event);

        verify(event, times(1)).getEntity();
    }

    @Test
    public void test_non_player_attacker_player_victim()
    {
        Wolf attacker = mock(Wolf.class);
        Player victim = mock(Player.class);

        event = spy(new EntityDamageByEntityEvent(attacker, victim, DamageCause.ENTITY_ATTACK, 1d));

        listener.onHit(event);

        verify(event, never()).getEntity();
    }
}
