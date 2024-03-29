package kotx.customgui.gateway.handlers

import kotlinx.serialization.json.JsonObject
import kotx.customgui.CustomGUIMod
import kotx.customgui.gateway.GatewayHandler
import kotx.customgui.gateway.OpCode
import kotx.customgui.gui.guis.EditorGUI
import kotx.customgui.util.getInt
import kotx.customgui.util.getObjectArray
import kotx.customgui.view.ViewType

class LoadGUIHandler : GatewayHandler {
    override val opCode: OpCode = OpCode.LOAD_GUI

    override fun handle(json: JsonObject) {
        val guis = json.getObjectArray("views").map {
            val type = ViewType.get(it.getInt("type"))
            val parser = CustomGUIMod.viewHandler.getParser(type)
            parser.decode(it)
        }

        EditorGUI.holders.clear()
        EditorGUI.holders.addAll(guis)
    }
}
