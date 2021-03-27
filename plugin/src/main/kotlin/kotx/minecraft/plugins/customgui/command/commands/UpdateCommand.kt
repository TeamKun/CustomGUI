package kotx.minecraft.plugins.customgui.command.commands

import kotx.minecraft.plugins.customgui.command.Command
import kotx.minecraft.plugins.customgui.command.CommandConsumer
import kotx.minecraft.plugins.customgui.directory.Directories
import kotx.minecraft.plugins.customgui.extensions.send
import kotx.minecraft.plugins.customgui.extensions.sendHelp
import net.md_5.bungee.api.ChatColor
import org.bukkit.entity.Player
import java.nio.file.Paths

class UpdateCommand : Command("update") {
    override val requireOp: Boolean = false
    override val description: String = "addとは違い、指定ファイルを現在のワークスペースで明示的に更新します。"
    override val usages: List<String> = listOf(
        "customgui update <targetFile>"
    )
    override val examples: List<String> = listOf(
        "customgui update TestGUI"
    )

    override suspend fun CommandConsumer.execute() {
        if (args.isEmpty()) {
            sendHelp()
            return
        }

        val fileName = args.first().replace("[/\\\\.]".toRegex(), "")
        if (fileName.length > 32) {
            player!!.send {
                append("[CustomGUI] ").color(ChatColor.LIGHT_PURPLE).bold(true)
                append("ファイル名は32文字を超えてはいけません。").color(ChatColor.RED)
            }
            return
        }

        val duplicatedFile = Directories.guis.files.find { fileName == it.nameWithoutExtension }
        if (duplicatedFile == null) {
            player!!.send {
                append("[CustomGUI] ").color(ChatColor.LIGHT_PURPLE).bold(true)
                append(fileName).color(ChatColor.RED).bold(true)
                append("は見つかりませんでした。").bold(false)
            }
            return
        }

        if (duplicatedFile.parentFile.name != player!!.uniqueId.toString()) {
            player.send {
                append("[CustomGUI] ").color(ChatColor.LIGHT_PURPLE).bold(true)
                append(fileName).color(ChatColor.GRAY).reset()
                append("は既に存在しています。他のユーザーのファイルは上書きできません。").color(ChatColor.RED).reset()
            }
            return
        }

        saveGui(player, fileName)
    }

    private fun CommandConsumer.saveGui(player: Player, fileName: String) {
        val playerWorkspace =
            Paths.get("plugins", "CustomGUI", "workspaces", player.uniqueId.toString() + ".json").toFile()
        if (!playerWorkspace.exists()) {
            playerWorkspace.parentFile.mkdirs()
            playerWorkspace.createNewFile()
        }

        Directories.guis.write("${player.uniqueId}/$fileName.json", playerWorkspace.readText())

        player.send {
            append("[CustomGUI] ").color(ChatColor.LIGHT_PURPLE).bold(true)
            append(fileName).color(ChatColor.GREEN).bold(true)
            append("を保存しました。").bold(false)
        }
    }
}