package kotx.minecraft.plugins.customgui.command.commands

import kotx.ktools.asPacket
import kotx.ktools.toJson
import kotx.minecraft.plugins.customgui.command.Command
import kotx.minecraft.plugins.customgui.command.CommandConsumer
import kotx.minecraft.plugins.customgui.directory.Directories
import kotx.minecraft.plugins.customgui.extensions.send
import kotx.minecraft.plugins.customgui.extensions.sendHelp
import kotx.minecraft.plugins.customgui.extensions.suggestEntities
import net.md_5.bungee.api.ChatColor
import org.bukkit.Bukkit
import org.bukkit.entity.Player

class ShowCommand : Command("show") {
    override val requireOp: Boolean = false
    override val description: String = "指定したGUIを表示します。 (編集のみ)"
    override val usages: List<String> = listOf(
        "customgui show <file> [user]",
        "customgui show <file> <fadein> <stay> <fadeout>",
        "customgui show <file> <user> <fadein> <stay> <fadeout>",
    )
    override val examples: List<String> = listOf(
        "customgui show TestGUI",
        "customgui show TestGUI Kotlinx"
    )

    override suspend fun CommandConsumer.execute() {
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
            player!!.send {
                append("[CustomGUI] ").color(ChatColor.LIGHT_PURPLE).bold(true)
                append(fileName).color(ChatColor.RED).bold(true)
                append("は見つかりませんでした。").bold(false)
            }
            return
        }

        val targetPlayers = when (args.size) {
            1, 4 -> listOf(player!!)
            else -> Bukkit.selectEntities(sender, args[1]).filterIsInstance<Player>()
        }

        if (targetPlayers.isEmpty()) {
            player!!.send {
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
        targetPlayers.forEach { _ ->
            player?.sendPluginMessage(plugin, "customgui:workspace", object {
                val op = 2
                val data = guiData
                val fadeIn = fadeInTime
                val stay = stayTime
                val fadeOut = fadeOutTime
            }.toJson().asPacket())
        }

        player!!.send {
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
        args.size == 2 && player!!.isOp -> suggestEntities(args[1], plugin)
        else -> emptyList()
    }
}