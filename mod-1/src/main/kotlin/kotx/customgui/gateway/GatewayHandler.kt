package kotx.customgui.gateway

import kotlinx.serialization.json.*

interface GatewayHandler {
    val opCode: OpCode

    fun handle(json: JsonObject)
}