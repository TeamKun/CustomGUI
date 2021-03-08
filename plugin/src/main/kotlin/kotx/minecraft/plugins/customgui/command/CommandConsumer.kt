package kotx.minecraft.plugins.customgui.command

import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class CommandConsumer(
    val sender: CommandSender,
    val player: Player?,
    val message: String,
    val command: Command,
    val args: Array<out String>
)