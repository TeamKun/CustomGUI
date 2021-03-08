package kotx.minecraft.plugins.customgui.command

import kotx.minecraft.plugins.customgui.extensions.get
import org.bukkit.command.CommandSender
import org.bukkit.plugin.java.JavaPlugin
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class CommandHandler : KoinComponent {
    private val commands = mutableListOf<Command>()
    private val plugin by inject<JavaPlugin>()

    fun register(command: Command) {
        if (commands.map { it.name }.none { it.equals(command.name, true) }
            && commands.flatMap { it.aliases }.none { s -> command.aliases.any { it.equals(s, true) } })
            commands.add(command)

        command.children.forEach {
            it.parent = command
        }
    }

    fun handle(sender: CommandSender, label: String, args: Array<out String>): Boolean {
        return commands.get(label)?.also { it.handleExecute(sender, label, args) } != null
    }

    fun handleTabComplete(sender: CommandSender, label: String, args: Array<out String>): List<String> {
        val command = commands.get(label) ?: return emptyList()

        return command.handleTabComplete(sender, label, args)
    }
}