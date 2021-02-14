package org.pokecentral.netfixes.api;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.eventhandler.Event;

/**
 * Event thrown when Netfixes catches an exception in the {@link net.minecraft.network.play.client.CPacketCustomPayload}
 * packet. Only a few exceptions that are thrown in the packet handler are supported. To see which are, refer to
 * {@link org.pokecentral.netfixes.coremod.patches.CustomPayload}.
 *
 * This event is thrown at most every
 * {@link org.pokecentral.netfixes.ExceptionReporter#DELAY_PER_USER}ms per player.
 */
public class CustomPayloadExceptionEvent extends Event {
    private final EntityPlayerMP player;
    private final String message;
    private final Exception exception;

    public CustomPayloadExceptionEvent(EntityPlayerMP player, String message, Exception exception) {
        this.player = player;
        this.message = message;
        this.exception = exception;
    }

    public EntityPlayerMP getPlayer() {
        return player;
    }

    public String getMessage() {
        return message;
    }

    public Exception getException() {
        return exception;
    }
}
