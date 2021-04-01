package kotx.minecraft.plugins.customgui.command

import kotx.minecraft.libs.flylib.command.Command
import kotx.minecraft.libs.flylib.command.CommandConsumer
import kotx.minecraft.libs.flylib.command.internal.Permission
import kotx.minecraft.libs.flylib.command.internal.Usage
import kotx.minecraft.plugins.customgui.directory.Directories
import kotx.minecraft.plugins.customgui.extensions.EventWaiter
import kotx.minecraft.plugins.customgui.extensions.send
import net.md_5.bungee.api.ChatColor
import org.bukkit.entity.Player
import org.bukkit.event.player.AsyncPlayerChatEvent
import java.nio.file.Paths

class AddCommand : Command("add") {
    override val description: String = "現在のワークスペースをファイルに保存します。"
    override val usages: List<Usage> = listOf(
        Usage(
            "add <fileName>"
        )
    )
    override val examples: List<String> = listOf(
        "customgui add TestGUI"
    )

    override val permission: Permission = Permission.EVERYONE

    override fun CommandConsumer.execute() {
        if (args.isEmpty()) {
            sendHelp()
            return
        }

        val fileName = args.first().replace("[/\\\\.]".toRegex(), "")
        if (fileName.length > 32) {
            player.send {
                append("[CustomGUI] ").color(ChatColor.LIGHT_PURPLE).bold(true)
                append("ファイル名は32文字を超えてはいけません。").color(ChatColor.RED)
            }
            return
        }

        val duplicatedFile = Directories.guis.files.find { fileName == it.nameWithoutExtension }
        if (duplicatedFile != null) {
            if (duplicatedFile.parentFile.name != player.uniqueId.toString()) {
                player.send {
                    append("[CustomGUI] ").color(ChatColor.LIGHT_PURPLE).bold(true)
                    append(fileName).color(ChatColor.GRAY).reset()
                    append("は既に存在しています。他のユーザーのファイルは上書きできません。").color(ChatColor.RED).reset()
                }
                return
            }

            player.send {
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