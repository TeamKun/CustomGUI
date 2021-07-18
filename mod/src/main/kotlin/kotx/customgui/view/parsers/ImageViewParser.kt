package kotx.customgui.view.parsers

import kotlinx.serialization.json.JsonObject
import kotx.customgui.util.getInt
import kotx.customgui.util.getString
import kotx.customgui.util.json
import kotx.customgui.view.ViewParser
import kotx.customgui.view.views.ImageView

class ImageViewParser : ViewParser<ImageView> {
    override fun encode(view: ImageView) = json {
        "id" to view.id
        "url" to view.url
        "x1" to view.x1
        "y1" to view.y1
        "x2" to view.x2
        "y2" to view.y2
    }

    override fun decode(json: JsonObject) = ImageView(
        json.getString("id"),
        json.getString("url"),
    ).apply {
        x1 = json.getInt("x1")
        y1 = json.getInt("y1")
        x2 = json.getInt("x2")
        y2 = json.getInt("y2")
    }
}