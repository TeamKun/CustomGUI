package kotx.customgui.view.holders

import kotx.customgui.view.*
import kotx.customgui.view.views.*

class TextViewHolder(override val index: Int, override val content: TextView) : ViewHolder {
    override var selecting: Boolean = false
    override var moving: Boolean = false

    override fun copy(newIndex: Int): ViewHolder = TextViewHolder(newIndex, content.copy() as TextView)
}