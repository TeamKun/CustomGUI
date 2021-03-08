package kotx.minecraft.plugins.customgui.command.commands

import kotx.minecraft.plugins.customgui.command.Command
import kotx.minecraft.plugins.customgui.command.CommandConsumer
import kotx.minecraft.plugins.customgui.extensions.sendHelp

class CustomGUICommand : Command("customgui") {
    override val playerOnly: Boolean = true
    override val children: List<Command> = listOf(AddCommand(), RemoveCommand(), ListCommand(), ShowCommand(), CopyCommand(), UseCommand())
    override val requireOp: Boolean = false
    override val description: String = "CustomGUIを制御するコマンド"

    override suspend fun CommandConsumer.execute() {
        sendHelp()
    }
}