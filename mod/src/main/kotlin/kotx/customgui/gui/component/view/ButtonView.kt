package kotx.customgui.gui.component.view

import com.google.gson.Gson
import com.mojang.blaze3d.matrix.MatrixStack
import kotx.customgui.drawString
import kotx.customgui.fillAbsolute
import kotx.customgui.gui.View
import kotx.customgui.xCenter
import kotx.customgui.yCenter
import net.minecraft.client.Minecraft
import java.awt.Color

class ButtonView : View {
    var command: String = ""
    override val type: String = "button"
    override var startX: Int = 0
    override var startY: Int = 0
    override var endX: Int = 0
    override var endY: Int = 0
    override val canResize = true

    override fun init() {

    }

    override fun renderPreview(stack: MatrixStack, mouseX: Int, mouseY: Int) {
        val stX = xCenter + startX
        val stY = yCenter + startY
        val enX = xCenter + endX
        val enY = yCenter + endY

        fillAbsolute(stack, stX, stY, enX, enY, Color(0, 0, 255, 100))
        Minecraft.getInstance().fontRenderer.drawString(
            stack,
            command,
            xCenter + startX,
            yCenter + endY,
            Color.WHITE,
            true
        )
    }

    override fun renderPage(stack: MatrixStack, scaleW: Float, scaleH: Float, opacity: Float) {

    }

    override fun onClick(mouseX: Int, mouseY: Int, button: Int) {
        val mc = Minecraft.getInstance()
        mc.player?.sendChatMessage(command)
    }

    override fun parseToJson(): String = Gson().toJson(this)
}