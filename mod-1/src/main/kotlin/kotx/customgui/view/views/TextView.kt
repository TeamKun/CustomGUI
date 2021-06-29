package kotx.customgui.view.views

import com.mojang.blaze3d.matrix.*
import kotx.customgui.gui.*
import kotx.customgui.view.*
import java.awt.*

class TextView(
    val text: String,
    val color: Color,
    x1: Int,
    y1: Int,
    x2: Int,
    y2: Int
) : View(x1, y1, x2, y2) {
    override fun drawPreview(stack: MatrixStack) {
        GUI.string(stack, text, x1, y1, color)
    }

    override fun drawFull(stack: MatrixStack) {
        GUI.string(stack, text, x1, y1, color)
    }
}