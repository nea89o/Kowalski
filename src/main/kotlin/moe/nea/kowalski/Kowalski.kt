package moe.nea.kowalski

import net.minecraftforge.client.ClientCommandHandler
import net.minecraftforge.client.event.ClientChatReceivedEvent
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.event.FMLInitializationEvent
import net.minecraftforge.fml.common.eventhandler.Event
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent

@Mod(modid = "kowalski", useMetadata = true)
class Kowalski {

    companion object {
        val watchedEvents = mutableSetOf<String>()

        @JvmStatic
        fun eventIsBeingCancelled(event: Event) {
            if (event.javaClass.simpleName !in watchedEvents) {
                return
            }
            val interestingStack = Thread.currentThread()
                .stackTrace
                .dropWhile { it.className.startsWith("java.lang.") }
                .dropWhile { it.className.startsWith("moe.nea.kowalski.") }
                .drop(1)
            KowalskiCommand.chat(
                "§c§k!!!§c RED ALERT §k!!!§c\n" +
                        "AN EVENT HAS BEEN CANCELLED\n" +
                        "§c=================\n" +
                        "§e${interestingStack.joinToString("\n§e")}\n" +
                        "§c================="
            )
        }
    }


    @SubscribeEvent
    fun cancelMessages(event: ClientChatReceivedEvent) {
        if (event.message.unformattedText.contains("verboten"))
            event.isCanceled = true
    }

    @Mod.EventHandler
    fun init(event: FMLInitializationEvent) {
        ClientCommandHandler.instance.registerCommand(KowalskiCommand)
        MinecraftForge.EVENT_BUS.register(this)
    }


}
