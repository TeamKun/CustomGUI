package kotx.customgui.view.views

import kotx.customgui.view.View
import kotx.customgui.view.renderers.RectViewRenderer
import java.awt.Color

class RectView(
    val color: Color,
) : View {
    override val renderer = RectViewRenderer()

    override var x1: Int = 0
    override var y1: Int = 0
    override var x2: Int = 0
    override var y2: Int = 0

    override fun copy(): View = RectView(color).also {
        it.x1 = x1
        it.y1 = y1
        it.x2 = x2
        it.y2 = y2
    }
}