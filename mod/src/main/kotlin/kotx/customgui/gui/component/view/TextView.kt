package kotx.customgui.gui.component.view

import com.google.gson.Gson
import com.mojang.blaze3d.matrix.MatrixStack
import kotx.customgui.drawString
import kotx.customgui.gui.View
import kotx.customgui.xCenter
import kotx.customgui.yCenter
import net.minecraft.client.Minecraft
import java.awt.Color

class TextView : View {
    var text: String = ""
    override val type: String = "text"
    override var startX: Int = 0
    override var startY: Int = 0
    override var endX: Int = 0
    override var endY: Int = 0
    override val canResize = false

    override fun init() {

    }

    override fun renderPreview(stack: MatrixStack, mouseX: Int, mouseY: Int) {
        val stX = xCenter + startX
        val stY = yCenter + startY

        Minecraft.getInstance().font.drawString(stack, text, stX, stY, Color.WHITE, true)
    }

    override fun renderPage(stack: MatrixStack, scaleW: Float, scaleH: Float, opacity: Float) {
        val stX = xCenter + (startX * scaleW).toInt()
        val stY = yCenter + (startY * scaleH).toInt()

        Minecraft.getInstance().font.drawString(
            stack,
            text,
            stX,
            stY,
            Color(255, 255, 255, (opacity * 255).toInt()),
            true
        )
    }

    override fun onClick(mouseX: Int, mouseY: Int, button: Int) {

    }

    override fun parseToJson(): String = Gson().toJson(this)
}