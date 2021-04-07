package kotx.customgui.gui.component.components

import com.mojang.blaze3d.matrix.MatrixStack
import kotx.customgui.drawStringCentered
import kotx.customgui.fillAbsolute
import kotx.customgui.gui.GuiDesignerScreen
import kotx.customgui.gui.component.IComponent
import kotx.customgui.gui.component.screen.ColorInputScreen
import kotx.customgui.xCenter
import kotx.customgui.yCenter
import net.minecraft.client.Minecraft
import java.awt.Color

object RectComponent : IComponent {
    override val text: String = "図形"
    var step = Step.SELECT_START
    private var startPos: Pair<Int, Int>? = null
    private var endPos: Pair<Int, Int>? = null

    override fun init() {
        step = Step.SELECT_START
        startPos = null
        endPos = null
    }

    override fun render(stack: MatrixStack, mouseX: Int, mouseY: Int, width: Int, height: Int) {
        if (GuiDesignerScreen.isInRange(mouseX, mouseY))
            fillAbsolute(
                stack,
                mouseX - 1, mouseY - 1, mouseX + 1, mouseY + 1, when (step) {
                    Step.SELECT_START -> Color.RED
                    Step.SELECT_END -> Color.BLUE
                    Step.SET_COLOR -> Color(0, 0, 0, 0)
                }
            )

        val currentStatusLabel = when (step) {
            Step.SELECT_START -> "図形の始点をクリック"
            Step.SELECT_END -> "図形の終点をクリック"
            Step.SET_COLOR -> ""
        }

        Minecraft.getInstance().font.drawStringCentered(currentStatusLabel, width / 2, 60, Color.WHITE)

        if (startPos != null) {
            val startX = xCenter + startPos!!.first
            val startY = yCenter + startPos!!.second
            val endX = xCenter + (endPos?.first ?: (mouseX - xCenter))
            val endY = yCenter + (endPos?.second ?: (mouseY - yCenter))

            fillAbsolute(stack, startX, startY, endX, endY, Color(255, 0, 0, 150))
        }
    }

    override fun onMouseMove(mouseX: Int, mouseY: Int) {

    }

    override fun onMouseClick(mouseX: Int, mouseY: Int, button: Int) {
        when (step) {
            Step.SELECT_START -> {
                step = Step.SELECT_END
                startPos = mouseX - xCenter to mouseY - yCenter
            }

            Step.SELECT_END -> {
                step = Step.SET_COLOR
                endPos = mouseX - xCenter to mouseY - yCenter

                val startX = startPos!!.first
                val startY = startPos!!.second
                val endX = endPos!!.first
                val endY = endPos!!.second

                Minecraft.getInstance().screen = ColorInputScreen(
                    startX,
                    startY,
                    endX,
                    endY
                )
            }
        }
    }

    override fun onKeyPress(keyCode: Int, scanCode: Int, modifiers: Int) {

    }

    enum class Step {
        SELECT_START, SELECT_END, SET_COLOR
    }
}