package kotx.customgui.gui

import com.mojang.blaze3d.matrix.*
import kotx.customgui.util.*
import net.minecraft.client.gui.screen.*

class GUIHandler(private val gui: GUI) : Screen("GuiHandler".component()) {
    override fun render(matrixStack: MatrixStack, mouseX: Int, mouseY: Int, partialTicks: Float) {
        gui.draw(matrixStack, mouseX, mouseY)

        super.render(matrixStack, mouseX, mouseY, partialTicks)
    }

    private val pressingButtons = mutableListOf<MouseButton>()

    override fun mouseMoved(mouseX: Double, mouseY: Double) {
        val x = mouseX.toInt()
        val y = mouseY.toInt()

        gui.onMouseMove(x, y)

        pressingButtons.forEach {
            gui.onMouseDrag(it, x, y)
        }

        super.mouseMoved(mouseX, mouseY)
    }

    override fun mouseClicked(mouseX: Double, mouseY: Double, button: Int): Boolean {
        val mouseButton = MouseButton.get(button)
        pressingButtons.add(mouseButton)

        gui.onMousePress(mouseButton, mouseX.toInt(), mouseY.toInt())

        return super.mouseClicked(mouseX, mouseY, button)
    }

    override fun mouseReleased(mouseX: Double, mouseY: Double, button: Int): Boolean {
        val mouseButton = MouseButton.get(button)
        pressingButtons.removeIf { it.button == button }

        gui.onMouseRelease(mouseButton, mouseX.toInt(), mouseY.toInt())

        return super.mouseReleased(mouseX, mouseY, button)
    }

    override fun keyPressed(keyCode: Int, scanCode: Int, modifiers: Int): Boolean {
        gui.onKeyPress(keyCode, modifiers)

        return super.keyPressed(keyCode, scanCode, modifiers)
    }

    override fun keyReleased(keyCode: Int, scanCode: Int, modifiers: Int): Boolean {
        gui.onKeyRelease(keyCode, modifiers)

        return super.keyReleased(keyCode, scanCode, modifiers)
    }
}