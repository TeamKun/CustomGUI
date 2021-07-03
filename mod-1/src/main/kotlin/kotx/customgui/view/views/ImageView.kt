package kotx.customgui.view.views

import kotx.customgui.view.*
import kotx.customgui.view.renderers.*

class ImageView(
    val id: String,
    val url: String,
    override val x1: Int,
    override val y1: Int,
    override val x2: Int,
    override val y2: Int
) : View {
    var isLoading = false
    var isLoaded = false
    override val renderer = ImageViewRenderer()
}