package kotx.customgui.view.parsers

import kotlinx.serialization.json.*
import kotx.customgui.util.*
import kotx.customgui.view.*
import kotx.customgui.view.views.*

class RectViewParser : ViewParser<RectView> {
    override fun encode(view: RectView): JsonObject = json { }

    override fun decode(json: JsonObject): RectView = RectView(
        json.getInt("x1"),
        json.getInt("y1"),
        json.getInt("x2"),
        json.getInt("y2"),
    )
}