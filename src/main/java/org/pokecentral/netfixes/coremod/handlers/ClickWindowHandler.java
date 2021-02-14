package org.pokecentral.netfixes.coremod.handlers;

import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.Container;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.network.play.client.CPacketClickWindow;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import org.pokecentral.netfixes.Netfixes;
import org.pokecentral.netfixes.api.ClickWindowBlockedEvent;

import java.util.HashMap;
import java.util.UUID;

/**
 * In case a plugin has their own implementation which *does* interfere with slots, we want to report any incidents.
 * <p>
 * However, we don't want to throttle this per player. Not throttling still allows the client to spam the console/logs,
 * resulting in crashes anyway.
 */
public class ClickWindowHandler {

    /**
     * Delay between incident messages, per user.
     */
    public static final long DELAY_PER_USER = 2_500L;

    private static final HashMap<UUID, Long> lastIncidentTimes = new HashMap<>();

    public static boolean handleClickWindow(NetHandlerPlayServer handler, CPacketClickWindow packet) {
        ClickType type = packet.getClickType();
        int drag = packet.getUsedButton();
        int slot = packet.getSlotId();

        // This is the only case where slot -999 is handled
        if (slot == -999 && ((type == ClickType.PICKUP || type == ClickType.QUICK_MOVE) && (drag == 0 || drag == 1))) {
            return true;
        }

        Container container = handler.player.openContainer;
        boolean allowed = slot >= 0 && slot < container.inventorySlots.size();
        if (allowed) return true;

        // Throttle the incident logging to avoid crashes
        Long prev = lastIncidentTimes.getOrDefault(handler.player.getUniqueID(), 0L);
        if (prev + DELAY_PER_USER > System.currentTimeMillis()) {
            return false;
        }
        lastIncidentTimes.put(handler.player.getUniqueID(), System.currentTimeMillis());

        // Notify player & log the incident to the logger
        handler.player.sendMessage(new TextComponentString(TextFormatting.RED + "Error: Slot out of bounds. Please report this, including the current time, to an administrator."));
        Netfixes.logger.warn("Player " + handler.player.getName() + " (" + handler.player.getUniqueID() + ") (" + handler.player.getPlayerIP() + ") clicked illegal slot: [windowId=" + packet.getWindowId() + ", slot=" + packet.getSlotId() + ", button=" + packet.getUsedButton() + ", type=" + packet.getClickType() + ", item=\"" + packet.getClickedItem().toString() + "\", actionNumber=" + packet.getActionNumber() + "]");

        // And throw the event for any listeners
        Netfixes.EVENT_BUS.post(new ClickWindowBlockedEvent(handler.player, packet));

        return false;
    }

}
