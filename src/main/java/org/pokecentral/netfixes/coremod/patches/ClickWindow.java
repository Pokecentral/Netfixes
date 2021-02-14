package org.pokecentral.netfixes.coremod.patches;

import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.network.play.client.CPacketClickWindow;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;
import org.pokecentral.netfixes.coremod.SimpleMethodTransformer;

/**
 * Fixes a packet exploit in Forge 1.12.2 that crashes the server when a spams
 * {@link net.minecraft.network.play.client.CPacketClickWindow} packets to the server, with invalid slot ids. This
 * causes an index out of bounds exception in the {@link net.minecraft.inventory.Container} class.
 * <p>
 * To circumvent the issue, the slot id is validated in the network handler.
 * <p>
 * If the packet is deemed invalid, the packet is dropped and will not be handled. The implementation of
 * {@link org.pokecentral.netfixes.coremod.handlers.ClickWindowHandler#handleClickWindow(NetHandlerPlayServer, CPacketClickWindow)}
 * takes care of any side effects (logging the event, kicking the player...)
 */
public class ClickWindow extends SimpleMethodTransformer {

    public static final String CLASS_NAME = "net.minecraft.network.NetHandlerPlayServer";
    public static final String METHOD_NAME = "func_147351_a";
    public static final String METHOD_DESC = "(Lnet/minecraft/network/play/client/CPacketClickWindow;)V";

    public ClickWindow() {
        super(CLASS_NAME, METHOD_NAME, METHOD_DESC);
    }

    @Override
    public void transformMethod(ClassNode cn, MethodNode md) {
        LabelNode jmp = new LabelNode();
        InsnList list = new InsnList();
        list.add(new VarInsnNode(Opcodes.ALOAD, 0));
        list.add(new VarInsnNode(Opcodes.ALOAD, 1));
        list.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "org/pokecentral/netfixes/coremod/handlers/ClickWindowHandler", "handleClickWindow", "(Lnet/minecraft/network/NetHandlerPlayServer;Lnet/minecraft/network/play/client/CPacketClickWindow;)Z", false));
        list.add(new JumpInsnNode(Opcodes.IFNE, jmp));
        list.add(new InsnNode(Opcodes.RETURN));
        list.add(jmp);

        md.instructions.insert(md.instructions.get(29), list);
    }
}
