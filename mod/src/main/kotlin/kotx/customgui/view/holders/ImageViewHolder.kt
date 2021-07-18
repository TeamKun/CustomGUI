package kotx.customgui.view.holders

import kotx.customgui.view.ViewHolder
import kotx.customgui.view.views.ImageView

class ImageViewHolder(override var index: Int, override val content: ImageView) : ViewHolder {
    override var selecting: Boolean = false
    override var moving: Boolean = false
    override var scalable: Boolean = true
    override var scaling: Boolean = false
}