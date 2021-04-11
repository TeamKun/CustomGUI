package kotx.minecraft.plugins.customgui

import kotx.ktools.*
import kotx.minecraft.libs.flylib.command.complete.providers.BasicCompletionContributor
import kotx.minecraft.libs.flylib.command.complete.providers.ChildrenCompletionContributor
import kotx.minecraft.libs.flylib.command.complete.providers.OptionCompletionContributor
import kotx.minecraft.libs.flylib.command.complete.providers.UsageCompletionContributor
import kotx.minecraft.libs.flylib.injectFlyLib
import kotx.minecraft.plugins.customgui.command.CustomGUICommand
import kotx.minecraft.plugins.customgui.extensions.EventWaiter
import org.bukkit.plugin.java.JavaPlugin
import java.nio.file.Paths

class PluginEntry : JavaPlugin() {
    companion object {
        private const val NAME = "Custom GUI"
        private const val VERSION = "0.1"
    }

    val flyLib = injectFlyLib {
        commandHandler {
            registerCommand(CustomGUICommand())

            completionContributors = listOf(
                ChildrenCompletionContributor(),
                UsageCompletionContributor(usageReplacements = mapOf(
                    { arg: String ->
                        arg.startsWith("op:")
                    } to {
                        if (sender.isOp)
                            listOf(it.replaceFirst("op:", ""))
                        else
                            emptyList()
                    }
                )),
                OptionCompletionContributor(),
                BasicCompletionContributor()
            )
        }
    }

    override fun onEnable() {
        setupListeners()
        setupMessengers()
        logger.info("$NAME v$VERSION started!")
    }

    override fun onDisable() {
        logger.info("Bye!")
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
}