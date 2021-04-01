package kotx.minecraft.plugins.customgui.command

import kotx.ktools.asPacket
import kotx.ktools.toJson
import kotx.minecraft.libs.flylib.command.Command
import kotx.minecraft.libs.flylib.command.CommandConsumer
import kotx.minecraft.libs.flylib.command.internal.Permission
import kotx.minecraft.libs.flylib.command.internal.Usage
import kotx.minecraft.libs.flylib.send
import kotx.minecraft.plugins.customgui.directory.Directories
import kotx.minecraft.plugins.customgui.extensions.suggestEntities
import net.md_5.bungee.api.ChatColor
import org.bukkit.Bukkit
import org.bukkit.entity.Player

class OverlayCommand : Command("overlay") {
    override val description: String = "指定したGUIを表示します。 (編集のみ)"
    override val usages: List<Usage> = listOf(
        Usage("show <file> [user]"),
        Usage("show <file> <fadein> <stay> <fadeout>"),
        Usage("show <file> <user> <fadein> <stay> <fadeout>"),
    )
    override val examples: List<String> = listOf(
        "customgui show TestGUI",
        "customgui show TestGUI Kotlinx"
    )
    override val permission: Permission = Permission.EVERYONE

    override fun CommandConsumer.execute() {
        if (args.isEmpty()) {
            sendHelp()
            return
        }

        if (args.size > 1 && !player!!.isOp) {
            player.send {
                append("[CustomGUI] ").color(ChatColor.LIGHT_PURPLE).bold(true)
                append("管理者以外は対象を指定出来ません。").color(ChatColor.RED).bold(false)
            }

            return
        }

        val fileName = args.first().replace("[/\\\\.]".toRegex(), "")
        val targetGui = Directories.guis.files.find {
            fileName == it.nameWithoutExtension
        }

        if (targetGui == null) {
            player.send {
                append("[CustomGUI] ").color(ChatColor.LIGHT_PURPLE).bold(true)
                append(fileName).color(ChatColor.RED).bold(true)
                append("は見つかりませんでした。").bold(false)
            }
            return
        }

        val targetPlayers = when (args.size) {
            1, 4 -> listOf(player)
            else -> Bukkit.selectEntities(player, args[1]).filterIsInstance<Player>()
        }

        if (targetPlayers.isEmpty()) {
            player.send {
                append("[CustomGUI] ").color(ChatColor.LIGHT_PURPLE).bold(true)
                append("誰も見せる人が見つかりませんでした。").bold(false)
            }
            return
        }

        val fadeInTime = when (args.size) {
            in 0..2 -> 0
            4 -> args[1].toIntOrNull() ?: 0
            5 -> args[2].toIntOrNull() ?: 0

            else -> 0
        }

        val stayTime = when (args.size) {
            in 0..2 -> Int.MAX_VALUE
            4 -> args[2].toIntOrNull() ?: 100
            5 -> args[3].toIntOrNull() ?: 100

            else -> 0
        }

        val fadeOutTime = when (args.size) {
            in 0..2 -> Int.MAX_VALUE
            4 -> args[3].toIntOrNull() ?: 100
            5 -> args[4].toIntOrNull() ?: 100

            else -> 0
        }

        val guiData = targetGui.readText()
        targetPlayers.forEach { it ->
            it.sendPluginMessage(plugin, "customgui:workspace", object {
                val op = 2
                val data = guiData
                val fadeIn = fadeInTime
                val stay = stayTime
                val fadeOut = fadeOutTime
            }.toJson().asPacket())
        }

        player.send {
            append("[CustomGUI] ").color(ChatColor.LIGHT_PURPLE).bold(true)
            append(fileName).color(ChatColor.RED).bold(true)
            append("を").bold(false).color(ChatColor.GRAY)
            targetPlayers.forEach {
                append(it.displayName).color(ChatColor.GREEN).bold(true)
                append(" ").color(ChatColor.GRAY)
            }
            append("に表示しました。")
        }
    }

    override fun CommandConsumer.tabComplete() = when {
        args.size == 1 -> Directories.guis.files.filter { it.isFile }.map { it.nameWithoutExtension }
        args.size == 2 && player.isOp -> suggestEntities(args[1], plugin)

        else -> emptyList()
    }
}