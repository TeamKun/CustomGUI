package kotx.customgui.view.holders

import kotx.customgui.view.ViewHolder
import kotx.customgui.view.views.TextView

class TextViewHolder(override var index: Int, override val content: TextView) : ViewHolder {
    override var selecting: Boolean = false
    override var moving: Boolean = false
    override var scalable: Boolean = false
    override var scaling: Boolean = false
}