package kotx.customgui.view.holders

import kotx.customgui.view.*
import kotx.customgui.view.views.*

class ImageViewHolder(override val index: Int, override val content: ImageView) : ViewHolder {
    override var selecting: Boolean = false
    override var moving: Boolean = false

    override fun copy(): ViewHolder = ImageViewHolder(index, content.copy() as ImageView)
}