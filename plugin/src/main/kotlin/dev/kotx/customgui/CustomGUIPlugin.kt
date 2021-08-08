package dev.kotx.customgui

import dev.kotx.customgui.commands.*
import dev.kotx.customgui.gateway.GatewayClient
import dev.kotx.flylib.command.Command
import dev.kotx.flylib.command.Permission
import dev.kotx.flylib.flyLib
import io.papermc.paper.event.player.AsyncChatEvent
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.plugin.java.JavaPlugin
import org.koin.core.context.startKoin
import org.koin.dsl.module
import java.io.File

class CustomGUIPlugin : JavaPlugin() {
    override fun onEnable() {
        flyLib {
            command(CustomGUICommand())
            defaultPermission(Permission.EVERYONE)
        }

        val gatewayClient = GatewayClient(this)

        startKoin {
            modules(module {
                single { gatewayClient }
            })
        }

        server.messenger.registerOutgoingPluginChannel(this, "customgui:messenger")
        server.messenger.registerIncomingPluginChannel(this, "customgui:messenger", gatewayClient)

        server.pluginManager.registerEvents(CustomGUIListener, this)

        Files.init()
    }

    override fun onDisable() {
        server.messenger.unregisterOutgoingPluginChannel(this)
        server.messenger.unregisterIncomingPluginChannel(this)
    }
}

object Constants {
    val workspaceDirectory = File("./plugins/CustomGUI/workspaces/")
}

object CustomGUIListener : Listener {
    private var chatCallbacks = mutableMapOf<Player, (AsyncChatEvent) -> Unit>()

    fun waitForChat(player: Player, callback: (AsyncChatEvent) -> Unit) {
        chatCallbacks[player] = callback
    }

    @EventHandler
    fun onChat(event: AsyncChatEvent) {
        chatCallbacks[event.player]?.invoke(event)
        chatCallbacks.remove(event.player)
    }
}

class CustomGUICommand : Command("customgui") {
    init {
        children(
            AddCommand(),
            UpdateCommand(),
            RemoveCommand(),
            CopyCommand(),
            UseCommand(),
            ListCommand(),
            OverlayCommand(),
            ShowCommand()
        )
    }
}