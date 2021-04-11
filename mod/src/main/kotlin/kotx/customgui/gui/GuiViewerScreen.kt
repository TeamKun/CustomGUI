package kotx.customgui.gui

import com.mojang.blaze3d.matrix.MatrixStack
import kotx.customgui.scaledHeight
import kotx.customgui.scaledWidth
import kotx.customgui.xCenter
import kotx.customgui.yCenter
import net.minecraft.client.gui.screen.Screen
import net.minecraft.util.text.StringTextComponent
import kotlin.math.max
import kotlin.math.min

object GuiViewerScreen : Screen(StringTextComponent("GUI Viewer")) {

    var opacity = 1.0
        set(value) {
            field = max(0.0, min(1.0, value))
        }
    var views = mutableListOf<View>()

    override fun render(stack: MatrixStack, mouseX: Int, mouseY: Int, partialTicks: Float) {
        val scaleW = scaledWidth.toFloat() / GuiDesignerScreen.guiWidth
        val scaleH = scaledHeight.toFloat() / GuiDesignerScreen.guiHeight

        views.forEach {
            it.renderPage(stack, scaleW, scaleH, opacity.toFloat())
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