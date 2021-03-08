package kotx.minecraft.plugins.customgui.command.commands

import kotx.minecraft.plugins.customgui.command.Command
import kotx.minecraft.plugins.customgui.command.CommandConsumer
import kotx.minecraft.plugins.customgui.directory.Directories
import kotx.minecraft.plugins.customgui.extensions.EventWaiter
import kotx.minecraft.plugins.customgui.extensions.send
import kotx.minecraft.plugins.customgui.extensions.sendHelp
import net.md_5.bungee.api.ChatColor
import org.bukkit.entity.Player
import org.bukkit.event.player.AsyncPlayerChatEvent
import java.nio.file.Paths

class AddCommand : Command("add") {
    override val requireOp: Boolean = false
    override val description: String = "現在のワークスペースをファイルに保存します。"
    override val usages: List<String> = listOf(
        "customgui add <file>"
    )
    override val examples: List<String> = listOf(
        "customgui add TestGUI"
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

        if (Directories.guis.files.any { fileName == it.nameWithoutExtension }) {
            player!!.send {
                append("[CustomGUI] ").color(ChatColor.LIGHT_PURPLE).bold(true)
                append(fileName).color(ChatColor.GRAY).bold(true)
                append("は既に存在しています。上書きしますか？").bold(false)
                append("\n").reset()
                append("以下のどちらかをチャットに入力").color(ChatColor.GRAY).bold(true)
                append(" (デフォルト: No)").bold(false)
                append("\n").reset()
                append("Yes ").color(ChatColor.GREEN).bold(true).append("(Y)").color(ChatColor.DARK_GREEN).bold(false)
                append("\n").reset()
                append("No ").color(ChatColor.RED).bold(true).append("(n)").color(ChatColor.DARK_RED).bold(false)
            }

            EventWaiter.register(AsyncPlayerChatEvent::class.java) {
                if (it.player.uniqueId != player.uniqueId) return@register false
                it.isCancelled = true
                when (it.message.toLowerCase()) {
                    "yes", "y" -> {
                        saveGui(player, fileName)
                    }

                    else -> {
                        player.send {
                            append("[CustomGUI] ").color(ChatColor.LIGHT_PURPLE).bold(true)
                            append("キャンセルしました。").color(ChatColor.YELLOW).reset()
                        }
                    }
                }
                true
            }
            return
        }

        saveGui(player!!, fileName)
    }

    private fun CommandConsumer.saveGui(player: Player, fileName: String) {
        val playerWorkspace = Paths.get("plugins", "CustomGUI", "workspaces", player.uniqueId.toString() + ".json").toFile()
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