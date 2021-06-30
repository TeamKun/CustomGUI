package kotx.customgui.view.renderers

import com.mojang.blaze3d.matrix.*
import kotx.customgui.gui.*
import kotx.customgui.view.*
import kotx.customgui.view.views.*
import java.awt.*

class ButtonViewRenderer : ViewRenderer<ButtonView> {
    override fun renderPreview(stack: MatrixStack, x1: Int, y1: Int, x2: Int, y2: Int, view: ButtonView) {
        val cmd = "/" + view.command.replaceFirst("^/".toRegex(), "")
        GUI.rect(stack, x1, y1, x2, y2, Color(0, 0, 255, 100))
        GUI.text(stack, cmd, x1, y2, Color.WHITE, true)
    }

    override fun renderFull(stack: MatrixStack, x1: Int, y1: Int, x2: Int, y2: Int, view: ButtonView) {

    }
}