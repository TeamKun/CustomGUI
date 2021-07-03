package kotx.customgui.view.parsers

import kotlinx.serialization.json.*
import kotx.customgui.util.*
import kotx.customgui.view.*
import kotx.customgui.view.views.*

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
        json.getInt("x1"),
        json.getInt("y1"),
        json.getInt("x2"),
        json.getInt("y2"),
    )
}