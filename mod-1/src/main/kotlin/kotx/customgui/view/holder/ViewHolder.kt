package kotx.customgui.view.holder

import kotx.customgui.view.*

abstract class ViewHolder<T : View>(
    val index: Int,
    val type: ViewType,
    val content: T
)