package kotx.minecraft.plugins.customgui

import kotx.ktools.*
import kotx.minecraft.libs.flylib.*
import kotx.minecraft.libs.flylib.command.internal.*
import kotx.minecraft.plugins.customgui.command.*
import kotx.minecraft.plugins.customgui.extensions.*
import org.bukkit.plugin.java.*
import java.nio.file.*

class CustomGUIPlugin : JavaPlugin() {
    override fun onEnable() {

        flyLib {
            command {
                register(CustomGUICommand())

                defaultConfiguration {
                    permission(Permission.EVERYONE)
                }
            }
        }

        setupListeners()
        setupMessengers()
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