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

    override fun encode(view: TextViewHolder): JsonObject = json { }

    override fun decode(json: JsonObject): TextViewHolder = TextViewHolder(
        json.getInt("index"),
        ViewType.get(json.getInt("type")),
        viewParser.decode(json.getObject("view"))
    )
}