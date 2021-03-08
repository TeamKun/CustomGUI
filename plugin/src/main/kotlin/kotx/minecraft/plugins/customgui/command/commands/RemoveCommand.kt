package kotx.minecraft.plugins.customgui.command.commands

import kotx.minecraft.plugins.customgui.command.Command
import kotx.minecraft.plugins.customgui.command.CommandConsumer
import kotx.minecraft.plugins.customgui.directory.Directories
import kotx.minecraft.plugins.customgui.extensions.send
import kotx.minecraft.plugins.customgui.extensions.sendHelp
import net.md_5.bungee.api.ChatColor
import java.util.*


class RemoveCommand : Command("remove") {
    override val requireOp: Boolean = false
    override val description: String = "指定したファイルを削除します。(自身のGUIのみ)"
    override val usages: List<String> = listOf(
        "customgui remove <file>"
    )
    override val examples: List<String> = listOf(
        "customgui remove TestGUI"
    )

    override suspend fun CommandConsumer.execute() {
        if (args.isEmpty()) {
            sendHelp()
            return
        }

        val fileName = args.first().replace("[/\\\\.]".toRegex(), "")
        val target = Directories.guis.files.find {
            fileName == it.nameWithoutExtension
        }

        if (target == null) {
            player!!.send {
                append("[CustomGUI] ").color(ChatColor.LIGHT_PURPLE).bold(true)
                append(fileName).color(ChatColor.RED).bold(true)
                append("は見つかりませんでした。").bold(false)
            }
            return
        }


        if (target.parentFile.name != player!!.uniqueId.toString() && !player.isOp) {
            val owner = plugin.server.getPlayer(UUID.fromString(target.parentFile.name))
            player.send {
                append("[CustomGUI] ").color(ChatColor.LIGHT_PURPLE).bold(true)
                append(fileName).color(ChatColor.RED).bold(true)
                if (owner != null)
                    append("は${owner.displayName}が作成したものです。").bold(false)
                else
                    append("は他のプレイヤーが作成したものです。").bold(false)

                append("自分自身以外のGUIは削除出来ません。")
            }
            return
        }

        target.delete()
        player.send {
            append("[CustomGUI] ").color(ChatColor.LIGHT_PURPLE).bold(true)
            append(target.nameWithoutExtension).color(ChatColor.GREEN).bold(true)
            append("を削除しました。").bold(false)
        }
    }

    override fun CommandConsumer.tabComplete() = (if (player!!.isOp) Directories.guis.files else Directories.guis.files.filter { it.name == player.uniqueId.toString() }).map { it.nameWithoutExtension }
}