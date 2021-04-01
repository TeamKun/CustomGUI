package kotx.minecraft.plugins.customgui.command

import kotx.minecraft.libs.flylib.command.Command
import kotx.minecraft.libs.flylib.command.CommandConsumer
import kotx.minecraft.libs.flylib.command.internal.Permission
import kotx.minecraft.libs.flylib.command.internal.Usage
import kotx.minecraft.libs.flylib.send
import kotx.minecraft.plugins.customgui.directory.Directories
import net.md_5.bungee.api.ChatColor
import java.util.*


class RemoveCommand : Command("remove") {
    override val description: String = "指定したファイルを削除します。(自身のGUIのみ)"
    override val usages: List<Usage> = listOf(
        Usage("remove <targetFile>")
    )
    override val examples: List<String> = listOf(
        "customgui remove TestGUI"
    )
    override val permission: Permission = Permission.EVERYONE

    override fun CommandConsumer.execute() {
        if (args.isEmpty()) {
            sendHelp()
            return
        }

        val fileName = args.first().replace("[/\\\\.]".toRegex(), "")
        val target = Directories.guis.files.find {
            fileName == it.nameWithoutExtension
        }

        if (target == null) {
            player.send {
                append("[CustomGUI] ").color(ChatColor.LIGHT_PURPLE).bold(true)
                append(fileName).color(ChatColor.RED).bold(true)
                append("は見つかりませんでした。").bold(false)
            }
            return
        }


        if (target.parentFile.name != player.uniqueId.toString() && !player.isOp) {
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

    override fun CommandConsumer.tabComplete() =
        if (args.size == 1)
            (if (player.isOp) Directories.guis.files else Directories.guis.files.filter { it.name == player.uniqueId.toString() }).map { it.nameWithoutExtension }
    else
        emptyList()
}