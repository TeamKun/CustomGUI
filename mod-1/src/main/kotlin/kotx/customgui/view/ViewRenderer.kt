package kotx.customgui.view

import com.mojang.blaze3d.matrix.*

interface ViewRenderer<T : View> {
    fun renderPreview(stack: MatrixStack, x1: Int, y1: Int, x2: Int, y2: Int, view: T)
    fun renderFull(stack: MatrixStack, x1: Int, y1: Int, x2: Int, y2: Int, view: T)
}