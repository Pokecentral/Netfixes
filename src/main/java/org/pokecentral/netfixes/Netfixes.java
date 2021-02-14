package org.pokecentral.netfixes;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventBus;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(
        modid = Netfixes.MODID,
        name = Netfixes.NAME,
        version = Netfixes.VERSION,
        acceptableRemoteVersions = "*",
        serverSideOnly = true,
        dependencies = "required-after:pixelmon",
        acceptedMinecraftVersions = "[1.12.2]"
)
public class Netfixes {
    public static final String MODID = "netfixes";
    public static final String NAME = "Netfixes";
    public static final String VERSION = "1.0";

    public static final Logger logger = LogManager.getLogger("Netfixes");

    public static final EventBus EVENT_BUS = new EventBus();
}
