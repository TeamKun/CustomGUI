package kotx.customgui.view.holders.parsers

import kotlinx.serialization.json.*
import kotx.customgui.util.*
import kotx.customgui.view.*
import kotx.customgui.view.holders.*
import kotx.customgui.view.parsers.*
import kotx.customgui.view.views.*

class TextViewHolderParser : ViewHolderParser<TextViewHolder> {
    override val type: ViewType = ViewType.TEXT
    override val viewParser: ViewParser<TextView> = TextViewParser()

    override fun encode(holder: TextViewHolder): JsonObject = json {
        "type" to type.value
        "index" to holder.index
        "content" to viewParser.encode(holder.content)
    }

    override fun decode(json: JsonObject): TextViewHolder = TextViewHolder(
        json.getInt("index"),
        viewParser.decode(json.getObject("content"))
    )
}