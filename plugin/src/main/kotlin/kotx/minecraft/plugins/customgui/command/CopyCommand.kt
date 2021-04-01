package kotx.minecraft.plugins.customgui.command

import kotx.minecraft.libs.flylib.command.Command
import kotx.minecraft.libs.flylib.command.CommandConsumer
import kotx.minecraft.libs.flylib.command.internal.Permission
import kotx.minecraft.libs.flylib.command.internal.Usage
import kotx.minecraft.libs.flylib.send
import kotx.minecraft.plugins.customgui.directory.Directories
import net.md_5.bungee.api.ChatColor


class CopyCommand : Command("copy") {
    override val description: String = "指定したファイルを自分の物としてコピーします。"
    override val usages: List<Usage> = listOf(
        Usage(
            "copy <from> <to>"
        )
    )
    override val examples: List<String> = listOf(
        "customgui copy TestGUI CopiedTestGUI"
    )

    override val permission: Permission = Permission.EVERYONE

    override fun CommandConsumer.execute() {
        if (args.size != 2) {
            sendHelp()
            return
        }

        val fromFileName = args.first().replace("[/\\\\.]".toRegex(), "")
        val toFileName = args[1].replace("[/\\\\.]".toRegex(), "")
        val fromFile = Directories.guis.files.find { fromFileName == it.nameWithoutExtension }

        if (fromFile == null) {
            player.send {
                append("[CustomGUI] ").color(ChatColor.LIGHT_PURPLE).bold(true)
                append(toFileName).color(ChatColor.RED).bold(true)
                append("は見つかりませんでした。").bold(false)
            }
            return
        }

        if (Directories.guis.files.any { toFileName == it.nameWithoutExtension }) {
            player.send {
                append("[CustomGUI] ").color(ChatColor.LIGHT_PURPLE).bold(true)
                append(toFileName).color(ChatColor.RED).bold(true)
                append("は既に存在しています。").bold(false)
            }
            return
        }

        Directories.guis.write("${player.uniqueId}/$toFileName.json", fromFile.readText())
        player.send {
            append("[CustomGUI] ").color(ChatColor.LIGHT_PURPLE).bold(true)
            append(fromFileName).color(ChatColor.GREEN).bold(true)
            append("を").bold(false)
            append(toFileName).color(ChatColor.GREEN).bold(true)
            append("にコピーしました。").bold(false)
        }
    }

    override fun CommandConsumer.tabComplete() =
        if (args.size == 1) Directories.guis.files.map { it.nameWithoutExtension } else emptyList()
}