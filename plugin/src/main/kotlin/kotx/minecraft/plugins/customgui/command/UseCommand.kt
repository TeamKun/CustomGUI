package kotx.minecraft.plugins.customgui.command

import kotx.ktools.asPacket
import kotx.ktools.toJson
import kotx.minecraft.libs.flylib.command.Command
import kotx.minecraft.libs.flylib.command.internal.Argument
import kotx.minecraft.libs.flylib.command.internal.Usage
import kotx.minecraft.plugins.customgui.directory.Directories
import java.nio.file.Paths

class UseCommand : Command("use") {
    override val description: String = "指定したファイルをワークスペースに読み込みます。(現在のワークスペースは破棄されます。)"
    override val usages: List<Usage> = listOf(
        Usage(Argument.Text("target_file") { Directories.guis.files.map { it.nameWithoutExtension } }) {
            val fileName = args.first().replace("[/\\\\.]".toRegex(), "")
            val gui = Directories.guis.files.find { it.nameWithoutExtension == fileName }
            if (gui == null) {
                sendErrorMessage("${fileName}は存在しません。")
                return@Usage
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

            sendSuccessMessage("${fileName}を現在のワークスペースに読み込みました。")
        }
    )
    override val examples: List<String> = listOf(
        "customgui use TestGUI"
    )
}