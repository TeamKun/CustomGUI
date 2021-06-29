package kotx.customgui.gateway.handlers

import kotlinx.serialization.json.*
import kotx.customgui.*
import kotx.customgui.gateway.*
import kotx.customgui.gui.guis.*
import kotx.customgui.util.*

class LoadGUIHandler : GatewayHandler {
    override val opCode: OpCode = OpCode.LOAD_GUI

    override fun handle(json: JsonObject) {
        EditorGUI.holders.clear()
        json.getObjectArray("guis").mapNotNull { CustomGUIMod.getParser(json.getInt("type"))?.decode(json) }.also { EditorGUI.holders.addAll(it) }
    }
}
