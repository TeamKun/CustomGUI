package kotx.customgui.gateway.handlers

import kotlinx.serialization.json.JsonObject
import kotx.customgui.gateway.GatewayHandler
import kotx.customgui.gateway.OpCode

class ShowGUIHandler : GatewayHandler {
    override val opCode: OpCode = OpCode.SHOW_GUI

    override fun handle(json: JsonObject) {

    }
}