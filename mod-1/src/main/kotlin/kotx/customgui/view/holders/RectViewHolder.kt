package kotx.customgui.view.holders

import kotx.customgui.view.*
import kotx.customgui.view.views.*

class RectViewHolder(override val index: Int, override val content: RectView) : ViewHolder {
    override var selecting: Boolean = false
    override var moving: Boolean = false
}