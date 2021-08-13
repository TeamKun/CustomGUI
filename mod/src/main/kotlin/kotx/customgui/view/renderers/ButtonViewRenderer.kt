package kotx.customgui.view.renderers

import com.mojang.blaze3d.matrix.MatrixStack
import kotx.customgui.gui.GUI
import kotx.customgui.gui.MouseButton
import kotx.customgui.view.ViewRenderer
import kotx.customgui.view.views.ButtonView
import java.awt.Color

class ButtonViewRenderer : ViewRenderer<ButtonView> {
    override fun renderPreview(
        stack: MatrixStack,
        x1: Int,
        y1: Int,
        x2: Int,
        y2: Int,
        opacity: Double,
        view: ButtonView
    ) {
        val cmd = view.command
        GUI.rect(stack, x1, y1, x2, y2, Color(0, 0, 255, 100))
        GUI.text(stack, cmd, x1, y2, Color.WHITE, true)
    }

    override fun renderFull(stack: MatrixStack, x1: Int, y1: Int, x2: Int, y2: Int, opacity: Double, view: ButtonView) {

    }

    override fun handleClick(button: MouseButton, x: Int, y: Int) {

    }
}