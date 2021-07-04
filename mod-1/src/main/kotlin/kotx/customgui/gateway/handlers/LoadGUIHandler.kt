package kotx.customgui.gateway.handlers

import kotlinx.serialization.json.*
import kotx.customgui.*
import kotx.customgui.gateway.*
import kotx.customgui.gui.guis.editor.*
import kotx.customgui.util.*
import kotx.customgui.view.*

class LoadGUIHandler : GatewayHandler {
    override val opCode: OpCode = OpCode.LOAD_GUI

    override fun handle(json: JsonObject) {
        val guis = json.getObjectArray("guis").map {
            val type = ViewType.get(it.getInt("type"))
            val parser = CustomGUIMod.viewHandler.getParser(type)
            parser.decode(it)
        }

        EditorGUI.holders.clear()
        EditorGUI.holders.addAll(guis)
    }
}
