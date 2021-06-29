package kotx.customgui.view

import kotx.customgui.view.holders.parsers.*

class ViewHandler {
    private val parsers = listOf(
        TextViewHolderParser(),
        RectViewHolderParser(),
    )

    fun getParser(type: ViewType) = parsers.find { it.type == type }!!
}