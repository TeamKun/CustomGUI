package kotx.customgui.gateway.handlers

import kotlinx.serialization.json.JsonObject
import kotx.customgui.CustomGUIMod
import kotx.customgui.gateway.GatewayHandler
import kotx.customgui.gateway.OpCode

class SaveGUIHandler : GatewayHandler {
    override val opCode: OpCode = OpCode.SAVE_GUI

    override fun handle(json: JsonObject) {
        CustomGUIMod.LOGGER.debug("The GUI has been saved successfully.")
    }
}