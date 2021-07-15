package kotx.customgui.view.parsers

import kotlinx.serialization.json.JsonObject
import kotx.customgui.util.getInt
import kotx.customgui.util.json
import kotx.customgui.view.ViewParser
import kotx.customgui.view.views.RectView
import java.awt.Color

class RectViewParser : ViewParser<RectView> {
    override fun encode(view: RectView): JsonObject = json {
        "color" to view.color.rgb
        "x1" to view.x1
        "y1" to view.y1
        "x2" to view.x2
        "y2" to view.y2
    }

    override fun decode(json: JsonObject): RectView = RectView(
        Color(json.getInt("color")),
    ).apply {
        x1 = json.getInt("x1")
        y1 = json.getInt("y1")
        x2 = json.getInt("x2")
        y2 = json.getInt("y2")
    }
}