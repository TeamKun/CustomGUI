package dev.kotx.customgui.gateway.handlers

import dev.kotx.customgui.gateway.GatewayHandler
import dev.kotx.customgui.gateway.OpCode
import kotlinx.serialization.json.JsonObject
import org.bukkit.entity.Player
import java.io.File

class SaveGUIHandler : GatewayHandler {
    override val opCode: OpCode = OpCode.SAVE_GUI
    private val workspaceDirectory = File("./plugins/CustomGUI/workspaces/")

    override fun handle(player: Player, json: JsonObject) {
        val playerWorkspaceFile = File(workspaceDirectory, "${player.uniqueId}.json")
        if (!workspaceDirectory.exists())
            workspaceDirectory.mkdirs()

        if (playerWorkspaceFile.exists())
            playerWorkspaceFile.delete()

        playerWorkspaceFile.createNewFile()

        playerWorkspaceFile.writeText(json.toString())
    }
}