package kotx.customgui.view.holders

import kotx.customgui.view.ViewHolder
import kotx.customgui.view.views.RectView

class RectViewHolder(override var index: Int, override val content: RectView) : ViewHolder {
    override var selecting: Boolean = false
    override var moving: Boolean = false
    override var scalable: Boolean = true
    override var scaling: Boolean = false
}