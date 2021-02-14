package org.pokecentral.netfixes;

import net.minecraft.network.NetHandlerPlayServer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.pokecentral.netfixes.api.CustomPayloadExceptionEvent;

import java.util.HashMap;
import java.util.UUID;

/**
 * A simple utility to report exceptions, this avoids the console from being flooded with errors, while still reporting
 * individual exceptions.
 * <p>
 * Stack traces are not omitted, however, player sources are included to make it easier to track down which player is
 * causing which exceptions.
 */
public class ExceptionReporter {

    /**
     * Delay between incident messages, per user.
     */
    public static final long DELAY_PER_USER = 2_500L;

    private static final HashMap<UUID, Long> lastIncidentTimes = new HashMap<>();

    private static final Logger logger = LogManager.getLogger("Netfixes");

    public static void reportException(NetHandlerPlayServer handler, String message, Exception exception) {
        Long prev = lastIncidentTimes.getOrDefault(handler.player.getUniqueID(), 0L);
        if (prev + DELAY_PER_USER > System.currentTimeMillis()) {
            return;
        }
        lastIncidentTimes.put(handler.player.getUniqueID(), System.currentTimeMillis());

        logger.error("Player " + handler.player.getName() + " (" + handler.player.getUniqueID() + ") (" + handler.player.getPlayerIP() + ") caused the following exception: " + message, exception);

        // And throw the event
        Netfixes.EVENT_BUS.post(new CustomPayloadExceptionEvent(handler.player, message, exception));
    }
}
