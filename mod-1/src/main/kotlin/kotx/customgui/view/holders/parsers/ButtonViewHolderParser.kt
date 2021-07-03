package kotx.customgui.view.holders.parsers

import kotlinx.serialization.json.*
import kotx.customgui.util.*
import kotx.customgui.view.*
import kotx.customgui.view.holders.*
import kotx.customgui.view.parsers.*
import kotx.customgui.view.views.*

class ButtonViewHolderParser : ViewHolderParser<ButtonViewHolder> {
    override val type: ViewType = ViewType.BUTTON
    override val viewParser: ViewParser<ButtonView> = ButtonViewParser()

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