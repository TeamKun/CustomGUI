package kotx.customgui.view.views

import kotx.customgui.view.*
import kotx.customgui.view.renderers.*
import java.awt.*

class TextView(
    val text: String,
    val color: Color,
) : View {
    override val renderer = TextViewRenderer()

    override var x1: Int = 0
    override var y1: Int = 0
    override var x2: Int = 0
    override var y2: Int = 0
}