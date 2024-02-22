package moe.nea.kowalski.init;

import net.minecraft.launchwrapper.IClassTransformer;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;

import java.util.ListIterator;

public class EventClassTransformer implements IClassTransformer {
    @Override
    public byte[] transform(String name, String transformedName, byte[] basicClass) {
        if (!"net.minecraftforge.fml.common.eventhandler.Event".equals(name))
            return basicClass;
        ClassNode classNode = new ClassNode();
        ClassReader classReader = new ClassReader(basicClass);
        classReader.accept(classNode, 0);
        for (MethodNode method : classNode.methods) {
            if (method.name.equals("setCanceled")) {
                transformCancelMethod(method);
            }
        }
        ClassWriter writer = new ClassWriter(0);
        classNode.accept(writer);
        return writer.toByteArray();
    }

    private void transformCancelMethod(MethodNode method) {
        ListIterator<AbstractInsnNode> iterator =
                method.instructions.iterator();
        while (iterator.hasNext()) {
            AbstractInsnNode insn = iterator.next();
            if (insn.getOpcode() != Opcodes.PUTFIELD) {
                continue;
            }
            method.instructions.insert(insn, buildCallKowalski());
        }
    }

    private InsnList buildCallKowalski() {
        InsnList insnList = new InsnList();
        insnList.add(new VarInsnNode(Opcodes.ALOAD, 0));
        insnList.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "moe/nea/kowalski/Kowalski", "eventIsBeingCancelled", "(Lnet/minecraftforge/fml/common/eventhandler/Event;)V", false));
        return insnList;
    }
}
