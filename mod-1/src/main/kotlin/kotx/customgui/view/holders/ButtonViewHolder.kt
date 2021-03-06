package kotx.customgui.view.holders

import kotx.customgui.view.*
import kotx.customgui.view.views.*

class ButtonViewHolder(override val index: Int, override val content: ButtonView) : ViewHolder {
    override var selecting: Boolean = false
    override var moving: Boolean = false

    override fun copy(newIndex: Int): ViewHolder = ButtonViewHolder(newIndex, content.copy() as ButtonView)
}