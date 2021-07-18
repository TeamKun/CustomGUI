package kotx.customgui.view.holders.parsers

import kotlinx.serialization.json.JsonObject
import kotx.customgui.util.getInt
import kotx.customgui.util.getObject
import kotx.customgui.util.json
import kotx.customgui.view.ViewHolderParser
import kotx.customgui.view.ViewType
import kotx.customgui.view.holders.TextViewHolder
import kotx.customgui.view.parsers.TextViewParser

class TextViewHolderParser : ViewHolderParser<TextViewHolder> {
    override val type: ViewType = ViewType.TEXT
    override val viewParser = TextViewParser()

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