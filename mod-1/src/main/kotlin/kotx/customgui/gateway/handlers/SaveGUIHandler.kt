package kotx.customgui.gateway.handlers

import kotlinx.serialization.json.*
import kotx.customgui.*
import kotx.customgui.gateway.*

class SaveGUIHandler : GatewayHandler {
    override val opCode: OpCode = OpCode.SAVE_GUI

    override fun handle(json: JsonObject) {
        CustomGUIMod.LOGGER.debug("The GUI has been saved successfully.")
    }
}