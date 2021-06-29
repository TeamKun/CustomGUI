package kotx.customgui.view

import com.mojang.blaze3d.matrix.*

interface ViewRenderer {
    fun renderPreview(stack: MatrixStack, x1: Int, y1: Int, x2: Int, y2: Int, view: View)
    fun renderFull(stack: MatrixStack, x1: Int, y1: Int, x2: Int, y2: Int, view: View)
}