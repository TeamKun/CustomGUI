package kotx.customgui.view

import com.mojang.blaze3d.matrix.MatrixStack
import kotx.customgui.gui.MouseButton

interface ViewRenderer<T : View> {
    fun renderPreview(stack: MatrixStack, x1: Int, y1: Int, x2: Int, y2: Int, opacity: Double, view: T)
    fun renderFull(stack: MatrixStack, x1: Int, y1: Int, x2: Int, y2: Int, opacity: Double, view: T)

    fun handleClick(button: MouseButton, x: Int, y: Int) {}
}