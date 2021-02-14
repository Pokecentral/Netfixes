package org.pokecentral.netfixes.api;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.play.client.CPacketClickWindow;
import net.minecraftforge.fml.common.eventhandler.Event;

/**
 * Event thrown when a {@link CPacketClickWindow} was clicked that was deemed illegal.
 * <p>
 * This event is thrown at most once every
 * {@link org.pokecentral.netfixes.coremod.handlers.ClickWindowHandler#DELAY_PER_USER} ms per player.
 */
public class ClickWindowBlockedEvent extends Event {

    private final EntityPlayerMP player;
    private final CPacketClickWindow packet;

    public ClickWindowBlockedEvent(EntityPlayerMP player, CPacketClickWindow packet) {
        this.player = player;
        this.packet = packet;
    }

    public EntityPlayerMP getPlayer() {
        return player;
    }

    public CPacketClickWindow getPacket() {
        return packet;
    }

}
