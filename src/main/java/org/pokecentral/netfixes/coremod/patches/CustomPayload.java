package org.pokecentral.netfixes.coremod.patches;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;
import org.pokecentral.netfixes.coremod.SimpleMethodTransformer;

/**
 * Fixes several packet exploits in Forge 1.12.2 that crashes the server when a spams certain, modified versions of
 * {@link net.minecraft.network.play.client.CPacketCustomPayload} packets to the server. This causes several exceptions
 * that can be spammed in order to crash the server.
 * <p>
 * To circumvent the issue, the exception messages are throttled, and more descriptive.
 * <p>
 * The only thing this does, is omit error messages to avoid console spam. Invalid packets are not rerouted, and
 * exceptions are caught only after they were thrown.
 */
public class CustomPayload extends SimpleMethodTransformer {

    public static final String CLASS_NAME = "net.minecraft.network.NetHandlerPlayServer";
    public static final String METHOD_NAME = "func_147349_a";
    public static final String METHOD_DESC = "(Lnet/minecraft/network/play/client/CPacketCustomPayload;)V";

    public CustomPayload() {
        super(CLASS_NAME, METHOD_NAME, METHOD_DESC);
    }

    private InsnList generateReportInsnList(String message, int register) {
        InsnList list = new InsnList();

        list.add(new VarInsnNode(Opcodes.ALOAD, 0));
        list.add(new LdcInsnNode(message));
        list.add(new VarInsnNode(Opcodes.ALOAD, register));
        list.add(new MethodInsnNode(Opcodes.INVOKESTATIC,
                "org/pokecentral/netfixes/ExceptionReporter",
                "reportException",
                "(Lnet/minecraft/network/NetHandlerPlayServer;Ljava/lang/String;Ljava/lang/Exception;)V",
                false)
        );

        return list;
    }

    @Override
    public void transformMethod(ClassNode cn, MethodNode md) {
        // Since we are removing and inserting instructions by offset, start at the end of the function. Then move
        // to the top. This avoids messing up the instruction indices.

        InsnList insns = md.instructions;

        // 3. MC|PickItem - TODO This can be improved by (also?) adding a range check of 0-36 in the packet parameter
        AbstractInsnNode position = insns.get(1480);
        insns.remove(insns.get(1484));
        insns.remove(insns.get(1483));
        insns.remove(insns.get(1482));
        insns.remove(insns.get(1481));
        insns.insert(position, generateReportInsnList("MC|PickItem - Couldn't pick item", 4));


        // 2. MC|BSIGN
        position = insns.get(273);
        insns.remove(insns.get(277));
        insns.remove(insns.get(276));
        insns.remove(insns.get(275));
        insns.remove(insns.get(274));
        insns.insert(position, generateReportInsnList("MC|BSign - Couldn't sign book", 4));

        // 1. MC|BEdit
        position = insns.get(97);
        insns.remove(insns.get(101));
        insns.remove(insns.get(100));
        insns.remove(insns.get(99));
        insns.remove(insns.get(98));
        insns.insert(position, generateReportInsnList("MC|BEdit - Couldn't handle book info", 4));
    }
}
