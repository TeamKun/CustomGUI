package kotx.customgui.gui.component.view

import com.google.gson.Gson
import kotx.customgui.drawString
import kotx.customgui.fillAbsolute
import kotx.customgui.gui.View
import kotx.customgui.xCenter
import kotx.customgui.yCenter
import net.minecraft.client.Minecraft
import java.awt.Color

class RectView : View {
    var r = 0
    var g = 0
    var b = 0
    override val type: String = "rect"
    override var startX: Int = 0
    override var startY: Int = 0
    override var endX: Int = 0
    override var endY: Int = 0

    override fun init() {

    }

    override fun renderPreview(mouseX: Int, mouseY: Int) {
        val stX = xCenter + startX
        val stY = yCenter + startY
        val enX = xCenter + endX
        val enY = yCenter + endY

        fillAbsolute(stX, stY, enX, enY, Color(r, g, b))
        Minecraft.getInstance().fontRenderer.drawString("R: $r, G: $g, B: $b", stX, enY, Color.WHITE, true)
    }

    override fun renderPage(scaleW: Float, scaleH: Float, opacity: Float) {
        val stX = xCenter + (startX * scaleW).toInt()
        val stY = yCenter + (startY * scaleH).toInt()
        val enX = xCenter + (endX * scaleW).toInt()
        val enY = yCenter + (endY * scaleH).toInt()

        fillAbsolute(stX, stY, enX, enY, Color(r, g, b, (opacity * 255).toInt()))
    }

    override fun onClick(mouseX: Int, mouseY: Int, button: Int) {

    }

    override fun parseToJson(): String = Gson().toJson(this)
}