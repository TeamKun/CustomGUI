package kotx.minecraft.plugins.customgui.command

import kotx.minecraft.libs.flylib.command.Command
import kotx.minecraft.libs.flylib.command.CommandContext
import kotx.minecraft.libs.flylib.command.internal.Permission
import kotx.minecraft.libs.flylib.command.internal.Usage
import kotx.minecraft.plugins.customgui.directory.Directories
import org.bukkit.entity.Player
import java.nio.file.Paths
import java.util.*

class UpdateCommand : Command("update") {
    override val description: String = "addとは違い、指定ファイルを現在のワークスペースで明示的に更新します。"
    override val usages: List<Usage> = listOf(
        Usage("update <targetFile>")
    )
    override val examples: List<String> = listOf(
        "customgui update TestGUI"
    )
    override val permission: Permission = Permission.EVERYONE

    override fun CommandContext.execute() {
        if (args.isEmpty()) {
            sendHelp()
            return
        }

        val fileName = args.first().replace("[/\\\\.]".toRegex(), "")
        if (fileName.length > 32) {
            sendErrorMessage("ファイル名は32文字を超えてはいけません。")
            return
        }

        val duplicatedFile = Directories.guis.files.find { fileName == it.nameWithoutExtension }
        if (duplicatedFile == null) {
            sendErrorMessage("${fileName}は見つかりませんでした。")
            return
        }

        if (duplicatedFile.parentFile.name != player!!.uniqueId.toString()) {
            val owner = plugin.server.getOfflinePlayer(UUID.fromString(duplicatedFile.parentFile.name))
            sendErrorMessage("${fileName}は${owner.name}が制作した物です。他ユーザーのファイルは上書きできません。")
            return
        }

        saveGui(player!!, fileName)
    }

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

    override fun CommandContext.tabComplete() =
        if (args.size == 1)
            (if (player!!.isOp) Directories.guis.files else Directories.guis.files.filter { it.parentFile.name == player!!.uniqueId.toString() }).map { it.nameWithoutExtension }
                .also { println(it) }
        else
            emptyList()
}