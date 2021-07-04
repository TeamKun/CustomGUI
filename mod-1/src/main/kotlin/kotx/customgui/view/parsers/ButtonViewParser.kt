package kotx.customgui.view.parsers

import kotlinx.serialization.json.*
import kotx.customgui.util.*
import kotx.customgui.view.*
import kotx.customgui.view.views.*

class ButtonViewParser : ViewParser<ButtonView> {
    override fun encode(view: ButtonView): JsonObject = json {
        "command" to view.command
        "x1" to view.x1
        "y1" to view.y1
        "x2" to view.x2
        "y2" to view.y2
    }

    override fun decode(json: JsonObject): ButtonView = ButtonView(
        json.getString("command"),
    ).apply {
        x1 = json.getInt("x1")
        y1 = json.getInt("y1")
        x2 = json.getInt("x2")
        y2 = json.getInt("y2")
    }
}