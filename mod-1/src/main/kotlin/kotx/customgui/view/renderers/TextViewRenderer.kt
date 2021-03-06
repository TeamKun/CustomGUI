package kotx.customgui.view.renderers

import com.mojang.blaze3d.matrix.*
import kotx.customgui.gui.*
import kotx.customgui.view.*
import kotx.customgui.view.views.*

class TextViewRenderer : ViewRenderer<TextView> {
    override fun renderPreview(stack: MatrixStack, x1: Int, y1: Int, x2: Int, y2: Int, view: TextView) {
        GUI.text(stack, view.text, x1, y1, view.color)
    }

    override fun renderFull(stack: MatrixStack, x1: Int, y1: Int, x2: Int, y2: Int, view: TextView) {
        GUI.text(stack, view.text, x1, y1, view.color)
    }
}