package dev.kotx.customgui.gateway.handlers

import dev.kotx.customgui.gateway.GatewayHandler
import dev.kotx.customgui.gateway.OpCode
import kotlinx.serialization.json.JsonObject

class LoadGUIHandler : GatewayHandler {
    override val opCode: OpCode = OpCode.LOAD_GUI

    override fun handle(json: JsonObject) {

    }
}
