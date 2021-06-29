package kotx.customgui.view.holder.parsers

import kotlinx.serialization.json.*
import kotx.customgui.util.*
import kotx.customgui.view.*
import kotx.customgui.view.holder.*
import kotx.customgui.view.holder.holders.*
import kotx.customgui.view.parsers.*
import kotx.customgui.view.views.*

class TextViewHolderParser : ViewHolderParser<TextView, TextViewHolder> {
    override val type = ViewType.TEXT
    override val parser: ViewParser<TextView> = TextViewParser()

    override fun encode(holder: TextViewHolder): JsonObject = json {
        "index" to holder.index
        "type" to type.value
        "content" to parser.encode(holder.content)
    }

    override fun decode(json: JsonObject): TextViewHolder = TextViewHolder(
        json.getInt("index"),
        parser.decode(json.getObject("view"))
    )
}