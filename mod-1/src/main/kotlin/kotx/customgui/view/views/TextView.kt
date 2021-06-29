package kotx.customgui.view.views

import kotx.customgui.view.*
import kotx.customgui.view.renderers.*
import java.awt.*

class TextView(
    val text: String,
    val color: Color,
    override val x1: Int,
    override val y1: Int,
    override val x2: Int,
    override val y2: Int,
) : View {
    override val renderer = TextViewRenderer()
}