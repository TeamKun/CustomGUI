package kotx.customgui.view.holder.parsers

import kotlinx.serialization.json.*
import kotx.customgui.util.*
import kotx.customgui.view.*
import kotx.customgui.view.holder.*
import kotx.customgui.view.holder.holders.*
import kotx.customgui.view.parsers.*
import kotx.customgui.view.views.*

class RectViewHolderParser : ViewHolderParser<RectView, RectViewHolder> {
    override val type = ViewType.TEXT
    override val parser: ViewParser<RectView> = RectViewParser()

    override fun encode(holder: RectViewHolder): JsonObject = json {
        "index" to holder.index
        "type" to type.value
        "content" to parser.encode(holder.content)
    }

    override fun decode(json: JsonObject): RectViewHolder = RectViewHolder(
        json.getInt("index"),
        parser.decode(json.getObject("view"))
    )
}