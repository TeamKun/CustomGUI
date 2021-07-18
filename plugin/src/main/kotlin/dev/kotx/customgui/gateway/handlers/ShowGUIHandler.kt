package dev.kotx.customgui.gateway.handlers

import dev.kotx.customgui.gateway.GatewayHandler
import dev.kotx.customgui.gateway.OpCode
import kotlinx.serialization.json.JsonObject

class ShowGUIHandler : GatewayHandler {
    override val opCode: OpCode = OpCode.SHOW_GUI

    override fun handle(json: JsonObject) {

    }
}