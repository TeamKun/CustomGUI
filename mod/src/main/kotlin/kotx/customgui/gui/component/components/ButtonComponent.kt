package kotx.customgui.gui.component.components

import com.mojang.blaze3d.matrix.MatrixStack
import kotx.customgui.drawStringCentered
import kotx.customgui.fillAbsolute
import kotx.customgui.gui.GuiDesignerScreen
import kotx.customgui.gui.component.IComponent
import kotx.customgui.gui.component.components.ButtonComponent.Step.*
import kotx.customgui.gui.component.screen.CommandInputScreen
import kotx.customgui.xCenter
import kotx.customgui.yCenter
import net.minecraft.client.Minecraft
import java.awt.Color

object ButtonComponent : IComponent {
    override val text: String = "ボタン"
    var step = SELECT_START
    private var startPos: Pair<Int, Int>? = null
    private var endPos: Pair<Int, Int>? = null

    override fun init() {
        step = SELECT_START
        startPos = null
        endPos = null
    }

    override fun render(stack: MatrixStack, mouseX: Int, mouseY: Int, width: Int, height: Int) {
        if (GuiDesignerScreen.isInRange(mouseX, mouseY))
            fillAbsolute(
                stack,
                mouseX - 1, mouseY - 1, mouseX + 1, mouseY + 1, when (step) {
                    SELECT_START -> Color.RED
                    SELECT_END -> Color.BLUE
                    SET_COMMAND -> Color(0, 0, 0, 0)
                }
            )

        val currentStatusLabel = when (step) {
            SELECT_START -> "ボタンの始点をクリック"
            SELECT_END -> "ボタンの終点をクリック"
            SET_COMMAND -> ""
        }

        Minecraft.getInstance().fontRenderer.drawStringCentered(stack, currentStatusLabel, width / 2, 60, Color.WHITE)

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
            SELECT_START -> {
                step = SELECT_END
                startPos = mouseX - xCenter to mouseY - yCenter
            }

            SELECT_END -> {
                step = SET_COMMAND
                endPos = mouseX - xCenter to mouseY - yCenter

                val startX = startPos!!.first
                val startY = startPos!!.second
                val endX = endPos!!.first
                val endY = endPos!!.second

                Minecraft.getInstance().displayGuiScreen(
                    CommandInputScreen(
                        startX,
                        startY,
                        endX,
                        endY
                    )
                )
            }
        }
    }

    override fun onKeyPress(keyCode: Int, scanCode: Int, modifiers: Int) {

    }

    enum class Step {
        SELECT_START, SELECT_END, SET_COMMAND
    }
}