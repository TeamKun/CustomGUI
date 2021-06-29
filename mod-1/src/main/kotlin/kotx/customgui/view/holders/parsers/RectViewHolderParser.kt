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
    override fun encode(view: RectViewHolder): JsonObject = json { }

    override fun decode(json: JsonObject): RectViewHolder = RectViewHolder(
        json.getInt("index"),
        ViewType.get(json.getInt("type")),
        viewParser.decode(json.getObject("view"))
    )
}