package kotx.customgui.view.holders.parsers

import kotlinx.serialization.json.*
import kotx.customgui.util.*
import kotx.customgui.view.*
import kotx.customgui.view.holders.*
import kotx.customgui.view.parsers.*
import kotx.customgui.view.views.*

class ImageViewHolderParser : ViewHolderParser<ImageViewHolder> {
    override val type: ViewType = ViewType.IMAGE
    override val viewParser: ViewParser<ImageView> = ImageViewParser()

    override fun encode(holder: ImageViewHolder): JsonObject = json {
        "type" to type.value
        "index" to holder.index
        "content" to viewParser.encode(holder.content)
    }

    override fun decode(json: JsonObject): ImageViewHolder = ImageViewHolder(
        json.getInt("index"),
        viewParser.decode(json.getObject("content"))
    )
}