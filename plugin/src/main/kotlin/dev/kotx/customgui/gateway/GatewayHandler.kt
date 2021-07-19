package dev.kotx.customgui.gateway

import kotlinx.serialization.json.JsonObject
import org.bukkit.entity.Player

interface GatewayHandler {
    val opCode: OpCode

    fun handle(player: Player, json: JsonObject)
}