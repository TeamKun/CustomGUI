package dev.kotx.customgui

import dev.kotx.customgui.gateway.GatewayClient
import dev.kotx.flylib.command.Command
import dev.kotx.flylib.flyLib
import org.bukkit.plugin.java.JavaPlugin

class CustomGUIPlugin : JavaPlugin() {
    override fun onEnable() {
        flyLib {
            command(CustomGUICommand())
        }

        server.messenger.registerOutgoingPluginChannel(this, "customgui:messenger")
        server.messenger.registerIncomingPluginChannel(this, "customgui:messenger", GatewayClient(this))
    }

    override fun onDisable() {
        server.messenger.unregisterOutgoingPluginChannel(this)
        server.messenger.unregisterIncomingPluginChannel(this)
    }
}

class CustomGUICommand : Command("customgui") {
    init {

    }
}