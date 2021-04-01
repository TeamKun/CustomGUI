package kotx.minecraft.plugins.customgui.command

import kotx.minecraft.libs.flylib.command.Command
import kotx.minecraft.libs.flylib.command.CommandConsumer
import kotx.minecraft.libs.flylib.command.internal.Permission

class CustomGUICommand : Command("customgui") {
    override val description: String = "CustomGUIを制御するコマンド"
    override val permission: Permission = Permission.EVERYONE

    override val children: List<Command> = listOf(
        AddCommand(),
        CopyCommand(),
        ListCommand(),
        OverlayCommand(),
        RemoveCommand(),
        UpdateCommand(),
        UseCommand()
    )

    override fun CommandConsumer.execute() {
        sendHelp()
    }
}