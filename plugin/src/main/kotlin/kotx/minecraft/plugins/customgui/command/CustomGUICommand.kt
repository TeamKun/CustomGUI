package kotx.minecraft.plugins.customgui.command

import kotx.minecraft.libs.flylib.command.*
import kotx.minecraft.libs.flylib.command.internal.*

class CustomGUICommand : Command("customgui") {
    override val description: String = "CustomGUIを制御するコマンド"
    override val permission: Permission = Permission.EVERYONE

    override val children: List<Command> = listOf(
        AddCommand(),
        CopyCommand(),
        ListCommand(),
        OverlayCommand(),
        ShowCommand(),
        RemoveCommand(),
        UpdateCommand(),
        UseCommand()
    )

    override fun CommandContext.execute() {
        sendHelp()
    }
}