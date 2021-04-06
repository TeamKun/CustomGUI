package kotx.customgui.gui

import com.mojang.blaze3d.matrix.MatrixStack

interface View {
    val type: String
    var startX: Int
    var startY: Int
    var endX: Int
    var endY: Int
    val width: Int
        get() = endX - startX
    val height: Int
        get() = endY - startY
    val canResize: Boolean

    fun init()
    fun renderPreview(stack: MatrixStack, mouseX: Int, mouseY: Int)
    fun renderPage(stack: MatrixStack, scaleW: Float, scaleH: Float, opacity: Float)
    fun onClick(mouseX: Int, mouseY: Int, button: Int)
    fun parseToJson(): String
}