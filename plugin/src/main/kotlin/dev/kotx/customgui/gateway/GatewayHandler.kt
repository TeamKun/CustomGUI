package dev.kotx.customgui.gateway

import kotlinx.serialization.json.JsonObject

interface GatewayHandler {
    val opCode: OpCode

    fun handle(json: JsonObject)
}