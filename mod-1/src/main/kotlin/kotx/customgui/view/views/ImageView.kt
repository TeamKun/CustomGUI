package kotx.customgui.view.views

import kotx.customgui.view.*
import kotx.customgui.view.renderers.*

class ImageView(
    val id: String,
    val url: String,
) : View {
    var isLoading = false
    var isLoaded = false

    override val renderer = ImageViewRenderer()

    override var x1: Int = 0
    override var y1: Int = 0
    override var x2: Int = 0
    override var y2: Int = 0

    override fun copy(): View = ImageView(id, url).also {
        it.x1 = x1
        it.y1 = y1
        it.x2 = x2
        it.y2 = y2
    }
}