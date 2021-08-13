package kotx.customgui.view.renderers

import com.mojang.blaze3d.matrix.MatrixStack
import kotx.customgui.gui.GUI
import kotx.customgui.view.ViewRenderer
import kotx.customgui.view.views.RectView
import java.awt.Color

class RectViewRenderer : ViewRenderer<RectView> {
    override fun renderPreview(
        stack: MatrixStack,
        x1: Int,
        y1: Int,
        x2: Int,
        y2: Int,
        opacity: Double,
        view: RectView
    ) {
        val factor = opacity / 255.0
        val color = Color(
            view.color.red,
            view.color.green,
            view.color.blue,
            (view.color.alpha * factor).toInt()
        )

        GUI.rect(stack, x1, y1, x2, y2, color)
    }

    override fun renderFull(stack: MatrixStack, x1: Int, y1: Int, x2: Int, y2: Int, opacity: Double, view: RectView) {
        val factor = opacity / 255.0
        val color = Color(
            view.color.red,
            view.color.green,
            view.color.blue,
            (view.color.alpha * factor).toInt()
        )

        GUI.rect(stack, x1, y1, x2, y2, color)
    }
}