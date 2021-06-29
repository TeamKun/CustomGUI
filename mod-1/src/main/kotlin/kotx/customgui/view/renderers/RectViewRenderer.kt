package kotx.customgui.view.renderers

import com.mojang.blaze3d.matrix.*
import kotx.customgui.gui.*
import kotx.customgui.view.*
import kotx.customgui.view.views.*

class RectViewRenderer : ViewRenderer<RectView> {
    override fun renderPreview(stack: MatrixStack, x1: Int, y1: Int, x2: Int, y2: Int, view: RectView) {
        GUI.rect(stack, x1, y1, x2, y2, view.color)
    }

    override fun renderFull(stack: MatrixStack, x1: Int, y1: Int, x2: Int, y2: Int, view: RectView) {
        GUI.rect(stack, x1, y1, x2, y2, view.color)
    }
}