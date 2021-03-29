package kotx.minecraft.plugins.customgui.command.commands

import kotx.minecraft.plugins.customgui.command.Command
import kotx.minecraft.plugins.customgui.command.CommandConsumer
import kotx.minecraft.plugins.customgui.directory.Directories
import kotx.minecraft.plugins.customgui.extensions.send
import kotx.minecraft.plugins.customgui.extensions.sendHelp
import net.md_5.bungee.api.ChatColor
import java.util.*


class ListCommand : Command("list") {
    override val requireOp: Boolean = false
    override val description: String = "GUI一覧を表示します。"
    override val usages: List<String> = listOf(
        "customgui list"
    )
    override val examples: List<String> = listOf(
        "customgui list"
    )

    override suspend fun CommandConsumer.execute() {
        if (args.isNotEmpty()) {
            sendHelp()
            return
        }

        player!!.send {
            append("GUI一覧: (${Directories.guis.files.size})")
            Directories.guis.files.filter { it.isFile }.forEach {
                append(it.nameWithoutExtension).color(ChatColor.GREEN).bold(true)
                append(" (${it.parentFile.name.run { plugin.server.getPlayer(UUID.fromString(this))?.displayName ?: "Unknown" }})").color(
                    ChatColor.DARK_GREEN
                ).bold(false)
                append(", ").color(ChatColor.GRAY)
            }
        }
    }
}