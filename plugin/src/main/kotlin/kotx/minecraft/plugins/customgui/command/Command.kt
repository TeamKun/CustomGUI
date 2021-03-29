package kotx.minecraft.plugins.customgui.command

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotx.minecraft.plugins.customgui.extensions.get
import net.md_5.bungee.api.ChatColor
import net.md_5.bungee.api.chat.BaseComponent
import net.md_5.bungee.api.chat.ComponentBuilder
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.slf4j.LoggerFactory

abstract class Command(
    val name: String
) : CoroutineScope, KoinComponent {
    override val coroutineContext = Dispatchers.Default
    protected val logger = LoggerFactory.getLogger(this::class.java)!!
    protected val plugin by inject<JavaPlugin>()

    open val description: String = "No description available"
    open val aliases = listOf<String>()
    open val usages = listOf<String>()
    open val examples = listOf<String>()
    open val requireOp = true
    open val playerOnly = true

    open val children = listOf<Command>()

    var parent: Command? = null

    protected abstract suspend fun CommandConsumer.execute()
    protected open fun CommandConsumer.tabComplete(): List<String> = when {
        args.isEmpty() || args.firstOrNull().isNullOrBlank() -> children.map { it.name }
        args.size == 1 -> children.map { it.name }.filter { it.startsWith(args.first()) }
        else -> emptyList()
    }

    fun handleExecute(sender: CommandSender, label: String, args: Array<out String>): Boolean {
        launch {
            if (!validate(sender)) return@launch
            val consumer = CommandConsumer(
                sender, sender as? Player, label + " " + args.joinToString(" "), this@Command, args
            )
            children.get(args.firstOrNull() ?: "")?.handleExecute(sender, label, args.drop(1).toTypedArray())
                ?: consumer.execute()
        }
        return true
    }

    fun handleTabComplete(sender: CommandSender, label: String, args: Array<out String>): List<String> {
        if (!validate(sender)) return emptyList()
        val consumer = CommandConsumer(
            sender, sender as? Player, label + " " + args.joinToString(" "), this@Command, args
        )
        return children.get(args.firstOrNull() ?: "")?.handleTabComplete(sender, label, args.drop(1).toTypedArray())
            ?: consumer.tabComplete()
    }

    private fun validate(sender: CommandSender): Boolean {
        if (playerOnly && sender !is Player) return false
        if (sender.isOp) return true
        return !requireOp
    }

    open fun buildHelp(): Array<BaseComponent> = ComponentBuilder().apply {
        var commandTitle = name
        var cmd: Command? = this@Command
        while (cmd?.parent != null) {
            cmd = cmd.parent
            commandTitle = "${cmd!!.name} " + commandTitle
        }

        val allUsages = usages.toMutableList()
        val allExamples = examples.toMutableList()
        var cmds = children
        while (cmds.isNotEmpty()) {
            allUsages.addAll(cmds.flatMap { it.usages })
            allExamples.addAll(cmds.flatMap { it.examples })
            cmds = cmds.flatMap { it.children }
        }

        append("===================================\n").reset().color(ChatColor.GRAY)
        append("/$commandTitle").color(ChatColor.LIGHT_PURPLE).bold(true).append(" の使い方").reset().append("\n")
        if (requireOp)
            append("※このコマンドは").color(ChatColor.RED).append("管理者").bold(true).append("のみが実行出来ます。").bold(false)
                .append("\n")
        append("===================================\n").reset().color(ChatColor.GRAY)
        append("説明: ").reset().bold(true).append(description).reset().color(ChatColor.GRAY)
        if (aliases.isNotEmpty())
            append("\n").append("エイリアス: ").reset().bold(true).append(aliases.joinToString(", ".format(ChatColor.GRAY)) {
                it.format(ChatColor.RESET)
            }).reset().color(ChatColor.GRAY).append("\n")

        if (allUsages.isNotEmpty()) {
            append("\n")
            append("使い方:").reset().bold(true).append("\n")
            append(allUsages.joinToString("\n") { "/$it" }).reset().color(ChatColor.GRAY).append("\n")
        }
        if (allExamples.isNotEmpty()) {
            append("\n")
            append("例:").reset().bold(true).append("\n")
            append(allExamples.joinToString("\n") { "/$it" }).reset().color(ChatColor.GRAY).append("\n")
        }
    }.create()
}