package kotx.minecraft.plugins.customgui.command.commands

import kotx.minecraft.plugins.customgui.command.Command
import kotx.minecraft.plugins.customgui.command.CommandConsumer

class UpdateCommand: Command("update") {
    override val requireOp: Boolean = false
    override val description: String = "現在のワークスペースをファイルに保存します。"
    override val usages: List<String> = listOf(
        "customgui add <file>"
    )
    override val examples: List<String> = listOf(
        "customgui add TestGUI"
    )
    override suspend fun CommandConsumer.execute() {

    }
}