package kotx.customgui.gui.component

import com.mojang.blaze3d.matrix.MatrixStack
import kotx.customgui.gui.GuiDesignerScreen

interface IComponent {
    val text: String

    fun init()
    fun render(stack: MatrixStack, mouseX: Int, mouseY: Int, width: Int, height: Int)
    fun onMouseMove(mouseX: Int, mouseY: Int)
    fun onMouseClick(mouseX: Int, mouseY: Int, button: Int)
    fun onKeyPress(keyCode: Int, scanCode: Int, modifiers: Int)

    fun close() {
        GuiDesignerScreen.editModeIndex = -1
    }
}