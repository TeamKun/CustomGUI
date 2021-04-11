package kotx.minecraft.plugins.customgui.command

import kotx.ktools.asPacket
import kotx.ktools.toJson
import kotx.minecraft.libs.flylib.command.Command
import kotx.minecraft.libs.flylib.command.CommandContext
import kotx.minecraft.libs.flylib.command.internal.Usage
import kotx.minecraft.plugins.customgui.directory.Directories
import java.nio.file.Paths

class UseCommand : Command("use") {
    override val description: String = "指定したファイルをワークスペースに読み込みます。(現在のワークスペースは破棄されます。)"
    override val usages: List<Usage> = listOf(
        Usage("use <targetFile>")
    )
    override val examples: List<String> = listOf(
        "customgui use TestGUI"
    )


    override fun CommandContext.execute() {
        if (args.isEmpty()) {
            sendHelp()
            return
        }

        val fileName = args.first().replace("[/\\\\.]".toRegex(), "")
        val gui = Directories.guis.files.find { it.nameWithoutExtension == fileName }
        if (gui == null) {
            sendErrorMessage("${fileName}は存在しません。")
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

        sendSuccessMessage("${fileName}を現在のワークスペースに読み込みました。")
    }

    override fun CommandContext.tabComplete() =
        if (args.size == 1) Directories.guis.files.map { it.nameWithoutExtension } else emptyList()
}