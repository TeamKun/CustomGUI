package kotx.minecraft.plugins.customgui

import kotx.ktools.*
import kotx.minecraft.plugins.customgui.command.CommandHandler
import kotx.minecraft.plugins.customgui.command.commands.CustomGUICommand
import kotx.minecraft.plugins.customgui.extensions.EventWaiter
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.plugin.java.JavaPlugin
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.context.startKoin
import org.koin.dsl.module
import java.nio.file.Paths

class PluginEntry : JavaPlugin(), KoinComponent {
    private val handler by inject<CommandHandler>()

    companion object {
        private const val NAME = "Custom GUI"
        private const val VERSION = "0.1"
    }

    override fun onEnable() {
        setupKoin()
        setupCommands()
        setupListeners()
        setupMessengers()
        logger.info("$NAME v$VERSION started!")
    }

    override fun onDisable() {
        logger.info("Bye!")
    }

    private fun setupKoin() {
        startKoin {
            modules(module {
                single<JavaPlugin> { this@PluginEntry }
                single { server }
                single { CommandHandler() }
            })
            printLogger()
        }
    }

    private fun setupCommands() {
        handler.register(CustomGUICommand())
    }

    private fun setupListeners() {
        server.pluginManager.registerEvents(EventWaiter, this)
    }

    private fun setupMessengers() {
        server.messenger.registerOutgoingPluginChannel(this, "customgui:workspace")
        server.messenger.registerIncomingPluginChannel(this, "customgui:workspace") { channel, player, message ->
            try {
                val msg = message.drop(1).dropLast(1).toByteArray().toString(Charsets.UTF_8)
                val json = msg.asJsonObject()
                val config = Paths.get("plugins", "CustomGUI", "workspaces", "${player.uniqueId}.json").toFile()
                config.parentFile.mkdirs()
                when (json.getIntOrNull("op")) {
                    0 -> {
                        player.sendPluginMessage(this, "customgui:workspace", object {
                            val op = 0
                            val data = if (config.exists())
                                config.readText()
                            else
                                "[]"
                        }.toJson().asPacket())
                    }

                    1 -> {
                        if (!config.exists())
                            config.createNewFile()
                        val data = json.getStringArray("data").map { it.asJsonObject() }
                        config.writeText(data.toString())
                        player.sendPluginMessage(this, "customgui:workspace", object {
                            val op = 1
                            val data = "Saved your json"
                        }.toJson().asPacket())
                    }

                    else -> {
                        player.sendPluginMessage(this, "customgui:workspace", object {
                            val op = -1
                            val data = "Unknown op"
                        }.toJson().asPacket())
                    }
                }
            } catch (e: Exception) {
            }
        }
    }

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        return if (handler.handle(sender, label, args)) true
        else super.onCommand(sender, command, label, args)
    }

    override fun onTabComplete(sender: CommandSender, command: Command, alias: String, args: Array<out String>): MutableList<String> {
        return handler.handleTabComplete(sender, alias, args).toMutableList()
    }
}