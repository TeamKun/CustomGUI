package kotx.customgui.view.parsers

import kotlinx.serialization.json.*
import kotx.customgui.util.*
import kotx.customgui.view.*
import kotx.customgui.view.views.*
import java.awt.*

class TextViewParser : ViewParser<TextView> {
    override fun encode(view: TextView): JsonObject = json {
        "text" to view.text
        "color" to view.color.rgb
        "x1" to view.x1
        "y1" to view.y1
        "x2" to view.x2
        "y2" to view.y2
    }

    override fun decode(json: JsonObject): TextView = TextView(
        json.getString("text"),
        Color(json.getInt("color")),
        json.getInt("x1"),
        json.getInt("y1"),
        json.getInt("x2"),
        json.getInt("y2"),
    )
}