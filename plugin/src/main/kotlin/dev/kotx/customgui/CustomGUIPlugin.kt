package dev.kotx.customgui

import dev.kotx.customgui.commands.AddCommand
import dev.kotx.customgui.commands.CopyCommand
import dev.kotx.customgui.commands.ListCommand
import dev.kotx.customgui.commands.OverlayCommand
import dev.kotx.customgui.commands.RemoveCommand
import dev.kotx.customgui.commands.ShowCommand
import dev.kotx.customgui.commands.UpdateCommand
import dev.kotx.customgui.commands.UseCommand
import dev.kotx.customgui.gateway.GatewayClient
import dev.kotx.flylib.command.Command
import dev.kotx.flylib.command.Permission
import dev.kotx.flylib.flyLib
import io.papermc.paper.event.player.AsyncChatEvent
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.plugin.java.JavaPlugin

class CustomGUIPlugin : JavaPlugin() {
    override fun onEnable() {
        flyLib {
            command(CustomGUICommand())
            defaultPermission(Permission.EVERYONE)
        }

        server.messenger.registerOutgoingPluginChannel(this, "customgui:messenger")
        server.messenger.registerIncomingPluginChannel(this, "customgui:messenger", GatewayClient(this))

        server.pluginManager.registerEvents(CustomGUIListener, this)

        Files.init(this)
    }

    override fun onDisable() {
        server.messenger.unregisterOutgoingPluginChannel(this)
        server.messenger.unregisterIncomingPluginChannel(this)
    }
}

object CustomGUIListener : Listener {
    private var callbacks = mutableMapOf<Player, (AsyncChatEvent) -> Unit>()

    fun wait(player: Player, callback: (AsyncChatEvent) -> Unit) {
        callbacks[player] = callback
    }

    @EventHandler
    fun onChat(event: AsyncChatEvent) {
        callbacks[event.player]?.invoke(event)
        callbacks.remove(event.player)
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