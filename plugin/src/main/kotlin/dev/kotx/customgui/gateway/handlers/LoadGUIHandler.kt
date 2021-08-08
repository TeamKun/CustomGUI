package dev.kotx.customgui.gateway.handlers

import dev.kotx.customgui.asJsonObject
import dev.kotx.customgui.gateway.GatewayClient
import dev.kotx.customgui.gateway.GatewayHandler
import dev.kotx.customgui.gateway.OpCode
import kotlinx.serialization.json.JsonObject
import org.bukkit.entity.Player
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.io.File

class LoadGUIHandler : GatewayHandler, KoinComponent {
    override val opCode: OpCode = OpCode.LOAD_GUI
    private val gatewayClient: GatewayClient by inject()

    private val workspaceDirectory = File("./plugins/CustomGUI/workspaces/")

    override fun handle(player: Player, json: JsonObject) {
        val playerWorkspaceFile = File(workspaceDirectory, "${player.uniqueId}.json")
        if (playerWorkspaceFile.exists())
            gatewayClient.send(player, OpCode.LOAD_GUI, playerWorkspaceFile.readText().asJsonObject())
    }
}