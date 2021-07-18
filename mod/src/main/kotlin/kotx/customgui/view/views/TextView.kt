package kotx.customgui.view.views

import kotx.customgui.view.View
import kotx.customgui.view.renderers.TextViewRenderer
import java.awt.Color

class TextView(
    val text: String,
    val color: Color,
) : View {
    override val renderer = TextViewRenderer()

    override var x1: Int = 0
    override var y1: Int = 0
    override var x2: Int = 0
    override var y2: Int = 0

    override fun copy(): View = TextView(text, color).also {
        it.x1 = x1
        it.y1 = y1
        it.x2 = x2
        it.y2 = y2
    }
}