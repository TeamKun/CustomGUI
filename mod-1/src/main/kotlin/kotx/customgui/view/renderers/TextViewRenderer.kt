package kotx.customgui.view.renderers

import com.mojang.blaze3d.matrix.MatrixStack
import kotx.customgui.gui.GUI
import kotx.customgui.util.fontRenderer
import kotx.customgui.view.ViewRenderer
import kotx.customgui.view.views.TextView
import kotlin.math.abs

class TextViewRenderer : ViewRenderer<TextView> {
    override fun renderPreview(stack: MatrixStack, x1: Int, y1: Int, x2: Int, y2: Int, view: TextView) {
        stack.push()
        val widthRatio = abs(x2 - x1).toFloat() / fontRenderer.getStringWidth(view.text).toFloat()
        val heightRatio = abs(y2 - y1).toFloat() / fontRenderer.FONT_HEIGHT.toFloat()
        stack.translate(x1.toDouble(), y1.toDouble(), 0.0)
        stack.scale(widthRatio, heightRatio, 1f)
        GUI.text(stack, view.text, 0, 0, view.color)
        stack.pop()
    }

    override fun renderFull(stack: MatrixStack, x1: Int, y1: Int, x2: Int, y2: Int, view: TextView) {
        stack.push()
        val widthRatio = abs(x2 - x1).toFloat() / fontRenderer.getStringWidth(view.text).toFloat()
        val heightRatio = abs(y2 - y1).toFloat() / fontRenderer.FONT_HEIGHT.toFloat()
        stack.translate(x1.toDouble(), y1.toDouble(), 0.0)
        stack.scale(widthRatio, heightRatio, 1f)
        GUI.text(stack, view.text, 0, 0, view.color)
        stack.pop()
    }
}