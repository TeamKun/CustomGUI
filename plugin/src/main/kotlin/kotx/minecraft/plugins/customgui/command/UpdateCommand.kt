package kotx.minecraft.plugins.customgui.command

import kotx.minecraft.libs.flylib.command.Command
import kotx.minecraft.libs.flylib.command.CommandContext
import kotx.minecraft.libs.flylib.command.internal.Argument
import kotx.minecraft.libs.flylib.command.internal.Usage
import kotx.minecraft.plugins.customgui.directory.Directories
import org.bukkit.entity.Player
import java.nio.file.Paths
import java.util.*

class UpdateCommand : Command("update") {
    override val description: String = "addとは違い、指定ファイルを現在のワークスペースで明示的に更新します。"
    override val usages: List<Usage> = listOf(
        Usage(
            Argument.Text("target_file") {
                (if (player!!.isOp) Directories.guis.files else Directories.guis.files.filter { it.parentFile.name == player!!.uniqueId.toString() }).map { it.nameWithoutExtension }
            }
        ) {
            val fileName = args.first().replace("[/\\\\.]".toRegex(), "")
            if (fileName.length > 32) {
                sendErrorMessage("ファイル名は32文字を超えてはいけません。")
                return@Usage
            }

            val duplicatedFile = Directories.guis.files.find { fileName == it.nameWithoutExtension }
            if (duplicatedFile == null) {
                sendErrorMessage("${fileName}は見つかりませんでした。")
                return@Usage
            }

            if (duplicatedFile.parentFile.name != player!!.uniqueId.toString()) {
                val owner = plugin.server.getOfflinePlayer(UUID.fromString(duplicatedFile.parentFile.name))
                sendErrorMessage("${fileName}は${owner.name}が制作した物です。他ユーザーのファイルは上書きできません。")
                return@Usage
            }

            saveGui(player!!, fileName)
        }
    )
    override val examples: List<String> = listOf(
        "customgui update TestGUI"
    )

    private fun CommandContext.saveGui(player: Player, fileName: String) {
        val playerWorkspace =
            Paths.get("plugins", "CustomGUI", "workspaces", player.uniqueId.toString() + ".json").toFile()
        if (!playerWorkspace.exists()) {
            playerWorkspace.parentFile.mkdirs()
            playerWorkspace.createNewFile()
        }

        Directories.guis.write("${player.uniqueId}/$fileName.json", playerWorkspace.readText())

        sendSuccessMessage("${fileName}を保存しました。")
    }
}