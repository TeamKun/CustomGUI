package kotx.customgui.view.views

import kotx.customgui.view.*
import kotx.customgui.view.renderers.*
import java.awt.*

class RectView(
    val color: Color,
) : View {
    override val renderer = RectViewRenderer()

    override var x1: Int = 0
    override var y1: Int = 0
    override var x2: Int = 0
    override var y2: Int = 0
}