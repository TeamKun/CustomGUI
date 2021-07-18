package kotx.customgui.view

import kotlinx.serialization.json.JsonObject
import kotx.customgui.util.getInt
import kotx.customgui.view.holders.ButtonViewHolder
import kotx.customgui.view.holders.ImageViewHolder
import kotx.customgui.view.holders.RectViewHolder
import kotx.customgui.view.holders.TextViewHolder
import kotx.customgui.view.holders.parsers.ButtonViewHolderParser
import kotx.customgui.view.holders.parsers.ImageViewHolderParser
import kotx.customgui.view.holders.parsers.RectViewHolderParser
import kotx.customgui.view.holders.parsers.TextViewHolderParser

class ViewHandler {
    private val parsers = listOf(
        TextViewHolderParser(),
        RectViewHolderParser(),
        ImageViewHolderParser(),
        ButtonViewHolderParser(),
    )

    fun getParser(type: ViewType) = parsers.find { it.type == type }!!
    fun getParserByHolder(holder: ViewHolder) = getParser(
        when (holder) {
            is TextViewHolder -> ViewType.TEXT
            is RectViewHolder -> ViewType.RECT
            is ImageViewHolder -> ViewType.IMAGE
            is ButtonViewHolder -> ViewType.BUTTON
            else -> null
        }!!
    )

    fun encode(holder: ViewHolder): JsonObject {
        val parser = getParserByHolder(holder)
        return when (parser) {
            is TextViewHolderParser -> parser.encode(holder as TextViewHolder)
            is RectViewHolderParser -> parser.encode(holder as RectViewHolder)
            is ImageViewHolderParser -> parser.encode(holder as ImageViewHolder)
            is ButtonViewHolderParser -> parser.encode(holder as ButtonViewHolder)
            else -> null
        }!!
    }

    fun decode(json: JsonObject): ViewHolder {
        val type = ViewType.get(json.getInt("type"))
        val parser = getParser(type)
        return parser.decode(json)
    }
}