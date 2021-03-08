package kotx.customgui.gui.component

import kotx.customgui.gui.GuiDesignerScreen

interface IComponent {
    val text: String

    fun init()
    fun render(mouseX: Int, mouseY: Int, width: Int, height: Int)
    fun onMouseMove(mouseX: Int, mouseY: Int)
    fun onMouseClick(mouseX: Int, mouseY: Int, button: Int)
    fun onKeyPress(keyCode: Int, scanCode: Int, modifiers: Int)

    fun close() {
        GuiDesignerScreen.editModeIndex = -1
    }
}