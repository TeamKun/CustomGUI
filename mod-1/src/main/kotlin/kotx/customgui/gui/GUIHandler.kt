package kotx.customgui.gui

import com.mojang.blaze3d.matrix.MatrixStack
import kotx.customgui.util.component
import kotx.customgui.util.fontRenderer
import net.minecraft.client.gui.screen.Screen
import net.minecraft.client.gui.widget.TextFieldWidget
import net.minecraft.client.gui.widget.Widget
import net.minecraft.client.gui.widget.button.Button

class GUIHandler(private val gui: GUI) : Screen("GuiHandler".component()) {

    override fun init() {
        gui.initialize()
        super.init()
    }

    override fun render(matrixStack: MatrixStack, mouseX: Int, mouseY: Int, partialTicks: Float) {
        gui.draw(matrixStack, mouseX, mouseY)

        super.render(matrixStack, mouseX, mouseY, partialTicks)
    }

    private val pressingButtons = mutableListOf<MouseButton>()

    var lastX: Int? = null
    var lastY: Int? = null

    override fun mouseMoved(mouseX: Double, mouseY: Double) {
        val x = mouseX.toInt()
        val y = mouseY.toInt()

        gui.onMouseMove(x, y)

        val diffX = lastX?.let { x - it }
        val diffY = lastY?.let { y - it }

        pressingButtons.forEach {
            gui.onMouseDrag(it, x, y)

            if (diffX != null && diffY != null)
                gui.onMouseDragDiff(it, diffX, diffY)
        }

        lastX = x
        lastY = y

        super.mouseMoved(mouseX, mouseY)
    }

    override fun mouseClicked(mouseX: Double, mouseY: Double, button: Int): Boolean {
        val mouseButton = MouseButton.get(button)
        pressingButtons.add(mouseButton)

        gui.onMousePress(mouseButton, mouseX.toInt(), mouseY.toInt())

        return super.mouseClicked(mouseX, mouseY, button)
    }

    override fun mouseReleased(mouseX: Double, mouseY: Double, button: Int): Boolean {
        pressingButtons.removeIf { it.button == button }

        gui.onMouseRelease(MouseButton.get(button), mouseX.toInt(), mouseY.toInt())

        return super.mouseReleased(mouseX, mouseY, button)
    }

    override fun keyPressed(keyCode: Int, scanCode: Int, modifiers: Int): Boolean {
        return if (gui.onKeyPress(keyCode, modifiers)) super.keyPressed(keyCode, scanCode, modifiers) else false
    }

    override fun keyReleased(keyCode: Int, scanCode: Int, modifiers: Int): Boolean {
        return if (gui.onKeyRelease(keyCode, modifiers)) super.keyReleased(keyCode, scanCode, modifiers) else false
    }

    override fun onClose() {
        gui.onClose()

        super.onClose()
    }

    fun button(text: String, x: Int, y: Int, width: Int, height: Int, onClick: Button.() -> Unit) = Button(
        x, y, width, height, text.component()
    ) {
        it.onClick()
    }.also { addButton(it) }

    fun textField(title: String, x: Int, y: Int, width: Int, height: Int) =
        TextFieldWidget(fontRenderer, x, y, width, height, title.component()).also { addButton(it) }

    val buttons: List<Widget>
        get() = buttons.toList()
}