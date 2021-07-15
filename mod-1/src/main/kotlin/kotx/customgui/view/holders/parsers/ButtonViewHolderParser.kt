package kotx.customgui.view.holders.parsers

import kotlinx.serialization.json.JsonObject
import kotx.customgui.util.getInt
import kotx.customgui.util.getObject
import kotx.customgui.util.json
import kotx.customgui.view.ViewHolderParser
import kotx.customgui.view.ViewType
import kotx.customgui.view.holders.ButtonViewHolder
import kotx.customgui.view.parsers.ButtonViewParser

class ButtonViewHolderParser : ViewHolderParser<ButtonViewHolder> {
    override val type: ViewType = ViewType.BUTTON
    override val viewParser = ButtonViewParser()

    override fun encode(holder: ButtonViewHolder): JsonObject = json {
        "type" to type.value
        "index" to holder.index
        "content" to viewParser.encode(holder.content)
    }

    override fun decode(json: JsonObject): ButtonViewHolder = ButtonViewHolder(
        json.getInt("index"),
        viewParser.decode(json.getObject("content"))
    )
}