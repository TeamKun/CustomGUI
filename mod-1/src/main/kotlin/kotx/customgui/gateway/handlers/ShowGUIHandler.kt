package kotx.customgui.gateway.handlers

import kotlinx.serialization.json.*
import kotx.customgui.gateway.*

class ShowGUIHandler : GatewayHandler {
    override val opCode: OpCode = OpCode.SHOW_GUI

    override fun handle(json: JsonObject) {

    }
}