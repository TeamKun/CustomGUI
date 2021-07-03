package kotx.customgui.view.holders.parsers

import kotlinx.serialization.json.*
import kotx.customgui.util.*
import kotx.customgui.view.*
import kotx.customgui.view.holders.*
import kotx.customgui.view.parsers.*
import kotx.customgui.view.views.*

class RectViewHolderParser : ViewHolderParser<RectViewHolder> {
    override val type: ViewType = ViewType.RECT
    override val viewParser: ViewParser<RectView> = RectViewParser()

    override fun encode(holder: RectViewHolder): JsonObject = json {
        "type" to type.value
        "index" to holder.index
        "content" to viewParser.encode(holder.content)
    }

    override fun decode(json: JsonObject): RectViewHolder = RectViewHolder(
        json.getInt("index"),
        viewParser.decode(json.getObject("content"))
    )
}