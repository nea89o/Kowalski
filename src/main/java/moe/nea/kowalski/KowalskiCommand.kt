package moe.nea.kowalski

import net.minecraft.client.Minecraft
import net.minecraft.command.CommandBase
import net.minecraft.command.ICommandSender
import net.minecraft.util.BlockPos
import net.minecraft.util.ChatComponentText

object KowalskiCommand : CommandBase() {
    override fun getCommandName(): String {
        return "kowalski"
    }

    override fun canCommandSenderUseCommand(sender: ICommandSender?): Boolean {
        return true
    }

    override fun getCommandUsage(sender: ICommandSender?): String {
        return ""
    }


    override fun addTabCompletionOptions(
        sender: ICommandSender?,
        args: Array<out String>,
        pos: BlockPos?
    ): List<String> {
        return when (args.size) {
            1 -> getListOfStringsMatchingLastWord(args, "whocancels")
            2 -> when (args[0]) {
                "whocancels" -> getListOfStringsMatchingLastWord(args, "ClientChatReceivedEvent")
                else -> emptyList()
            }
            else -> emptyList()
        }
    }

    fun chat(text: String) {
        Minecraft.getMinecraft().ingameGUI.chatGUI.printChatMessage(ChatComponentText("§e[§fKW§e]§r $text"))
    }

    override fun processCommand(sender: ICommandSender, args: Array<out String>) {
        when (args.firstOrNull()) {
            "whocancels" -> {
                if (args.size != 2) {
                    chat("§cPlease provide an event to listen to: §bwhocancels <eventname>")
                    return
                }
                val eventName = args[1]
                if (eventName in Kowalski.watchedEvents) {
                    Kowalski.watchedEvents.remove(eventName)
                    chat("§fRemoved §c$eventName §ffrom watched events")
                } else {
                    Kowalski.watchedEvents.add(eventName)
                    chat("§fAdded §b$eventName §fto watched events")
                }
            }
        }
    }

}
