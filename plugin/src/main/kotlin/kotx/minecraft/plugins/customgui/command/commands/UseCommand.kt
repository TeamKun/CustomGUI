package kotx.minecraft.plugins.customgui.command.commands

import kotx.ktools.asPacket
import kotx.ktools.toJson
import kotx.minecraft.plugins.customgui.command.Command
import kotx.minecraft.plugins.customgui.command.CommandConsumer
import kotx.minecraft.plugins.customgui.directory.Directories
import kotx.minecraft.plugins.customgui.extensions.send
import kotx.minecraft.plugins.customgui.extensions.sendHelp
import net.md_5.bungee.api.ChatColor
import java.nio.file.Paths

class UseCommand : Command("use") {
    override val requireOp: Boolean = false
    override val description: String = "指定したファイルをワークスペースに読み込みます。(現在のワークスペースは破棄されます。)"
    override val usages: List<String> = listOf(
        "customgui use <targetFile>"
    )
    override val examples: List<String> = listOf(
        "customgui use TestGUI"
    )


    override suspend fun CommandConsumer.execute() {
        if (args.isEmpty()) {
            sendHelp()
            return
        }

        val fileName = args.first().replace("[/\\\\.]".toRegex(), "")
        val gui = Directories.guis.files.find { it.nameWithoutExtension == fileName }
        if (gui == null) {
            player!!.send {
                append("[CustomGUI] ").color(ChatColor.LIGHT_PURPLE).bold(true)
                append("${fileName}は存在しません。").color(ChatColor.RED)
            }
            return
        }

        val config = Paths.get("plugins", "CustomGUI", "workspaces", "${player?.uniqueId}.json").toFile()
        config.parentFile.mkdirs()
        if (!config.exists())
            config.createNewFile()

        config.writeText(gui.readText())

        println(gui.readText())
        player?.sendPluginMessage(plugin, "customgui:workspace", object {
            val op = 0
            val data = gui.readText()
        }.toJson().asPacket())

        player?.send {
            append("[CustomGUI] ").color(ChatColor.LIGHT_PURPLE).bold(true)
            append("${fileName}を現在のワークスペースに読み込みました。").color(ChatColor.GREEN)
        }
    }

    override fun CommandConsumer.tabComplete() = if (args.size == 1) Directories.guis.files.map { it.nameWithoutExtension } else emptyList()
}