package kotx.customgui.view.holders.parsers

import kotlinx.serialization.json.JsonObject
import kotx.customgui.util.getInt
import kotx.customgui.util.getObject
import kotx.customgui.util.json
import kotx.customgui.view.ViewHolderParser
import kotx.customgui.view.ViewType
import kotx.customgui.view.holders.RectViewHolder
import kotx.customgui.view.parsers.RectViewParser

class RectViewHolderParser : ViewHolderParser<RectViewHolder> {
    override val type: ViewType = ViewType.RECT
    override val viewParser = RectViewParser()

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