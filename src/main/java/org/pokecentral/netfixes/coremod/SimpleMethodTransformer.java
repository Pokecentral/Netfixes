package org.pokecentral.netfixes.coremod;

import net.minecraft.launchwrapper.IClassTransformer;
import net.minecraftforge.fml.common.asm.transformers.deobf.FMLDeobfuscatingRemapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

public abstract class SimpleMethodTransformer implements IClassTransformer {

    private static final Logger logger = LogManager.getLogger("Netfixes");

    protected final String owner;
    protected final String name;
    protected final String desc;

    public SimpleMethodTransformer(String owner, String name, String desc) {
        this.owner = owner;
        this.name = name;
        this.desc = desc;

        logger.info("Creating method transformer for " + owner + ", " + name + ", " + desc);
    }

    public abstract void transformMethod(ClassNode cn, MethodNode md);

    @Override
    public byte[] transform(String name, String transformedName, byte[] basicClass) {
        if (!transformedName.equals(this.owner))
            return basicClass;

        ClassNode cn = new ClassNode();
        new ClassReader(basicClass).accept(cn, 0);

        boolean found = false;
        for (MethodNode node : cn.methods) {
            String md = FMLDeobfuscatingRemapper.INSTANCE.mapMethodDesc(node.desc);
            String mn = FMLDeobfuscatingRemapper.INSTANCE.mapMethodName(name, node.name, node.desc);

            if (mn.equals(this.name) && md.equals(this.desc)) {
                transformMethod(cn, node);
                found = true;
            }
        }

        if (found) {
            logger.info("Patched " + this.owner + "#" + this.name + " " + this.desc);
        } else {
            logger.warn("Couldn't patch " + this.owner + "#" + this.name + " " + this.desc);
        }

        ClassWriter cw = new ClassWriter(0);
        cn.accept(cw);

        return cw.toByteArray();
    }
}
