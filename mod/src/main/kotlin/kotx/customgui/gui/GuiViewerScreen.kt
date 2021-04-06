package kotx.customgui.gui

import com.mojang.blaze3d.matrix.MatrixStack
import kotx.customgui.scaledHeight
import kotx.customgui.scaledWidth
import kotx.customgui.xCenter
import kotx.customgui.yCenter
import net.minecraft.client.gui.screen.Screen
import net.minecraft.util.text.StringTextComponent

class GuiViewerScreen(
    private val views: MutableList<View>,
    val fadeIn: Int,
    val stay: Int,
    val fadeOut: Int
) : Screen(StringTextComponent("GUI Viewer")) {

    private var opacity = 0f

    override fun init() {
        opacity = 0f
        super.init()
    }

    override fun render(stack: MatrixStack, mouseX: Int, mouseY: Int, partialTicks: Float) {
        opacity = minOf(1f, opacity + 1f / fadeIn)
//        fillAbsolute(0, 0, scaledWidth, scaledHeight, Color(0, 0, 0, 100))
        val scaleW = scaledWidth.toFloat() / GuiDesignerScreen.guiWidth
        val scaleH = scaledHeight.toFloat() / GuiDesignerScreen.guiHeight
//        val w = GuiDesignerScreen.guiWidth * scaleW
//        val h = GuiDesignerScreen.guiWidth * scaleH
//        fillAbsolute((xCenter - w / 2).toInt(), (yCenter - h / 2).toInt(), (xCenter + w / 2).toInt(), (yCenter + h / 2).toInt(), Color(12, 12, 12, 255))

        views.forEach {
            it.renderPage(stack, scaleW, scaleH, opacity)
        }

        super.render(stack, mouseX, mouseY, partialTicks)
    }

    override fun mouseClicked(p_mouseClicked_1_: Double, p_mouseClicked_3_: Double, p_mouseClicked_5_: Int): Boolean {
        val x = p_mouseClicked_1_.toInt()
        val y = p_mouseClicked_3_.toInt()
        val scaleW = scaledWidth.toFloat() / GuiDesignerScreen.guiWidth
        val scaleH = scaledHeight.toFloat() / GuiDesignerScreen.guiHeight

        views.filter {
            val stX = xCenter + (it.startX * scaleW).toInt()
            val stY = yCenter + (it.startY * scaleH).toInt()
            val enX = xCenter + (it.endX * scaleW).toInt()
            val enY = yCenter + (it.endY * scaleH).toInt()
            x in stX..enX && y in stY..enY
        }.forEach {
            it.onClick(x, y, p_mouseClicked_5_)
        }

        return super.mouseClicked(p_mouseClicked_1_, p_mouseClicked_3_, p_mouseClicked_5_)
    }
}