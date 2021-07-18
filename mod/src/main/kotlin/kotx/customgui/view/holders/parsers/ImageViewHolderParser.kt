package kotx.customgui.view.holders.parsers

import kotlinx.serialization.json.JsonObject
import kotx.customgui.util.getInt
import kotx.customgui.util.getObject
import kotx.customgui.util.json
import kotx.customgui.view.ViewHolderParser
import kotx.customgui.view.ViewType
import kotx.customgui.view.holders.ImageViewHolder
import kotx.customgui.view.parsers.ImageViewParser

class ImageViewHolderParser : ViewHolderParser<ImageViewHolder> {
    override val type: ViewType = ViewType.IMAGE
    override val viewParser = ImageViewParser()

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