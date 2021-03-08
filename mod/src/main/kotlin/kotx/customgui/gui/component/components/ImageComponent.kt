package kotx.customgui.gui.component.components

import kotx.customgui.drawStringCentered
import kotx.customgui.fillAbsolute
import kotx.customgui.gui.GuiDesignerScreen
import kotx.customgui.gui.component.IComponent
import kotx.customgui.gui.component.screen.ImageInputScreen
import kotx.customgui.xCenter
import kotx.customgui.yCenter
import net.minecraft.client.Minecraft
import java.awt.Color

object ImageComponent : IComponent {
    override val text: String = "画像"

    override fun init() {

    }

    override fun render(mouseX: Int, mouseY: Int, width: Int, height: Int) {
        if (GuiDesignerScreen.isInRange(mouseX, mouseY))
            fillAbsolute(
                mouseX - 1, mouseY - 1, mouseX + 1, mouseY + 1, Color.RED
            )

        Minecraft.getInstance().fontRenderer.drawStringCentered("画像を配置する場所をクリック", width / 2, 60, Color.WHITE)
    }

    override fun onMouseMove(mouseX: Int, mouseY: Int) {

    }

    override fun onMouseClick(mouseX: Int, mouseY: Int, button: Int) {
        Minecraft.getInstance().displayGuiScreen(
            ImageInputScreen(
                mouseX - xCenter,
                mouseY - yCenter,
            )
        )
    }

    override fun onKeyPress(keyCode: Int, scanCode: Int, modifiers: Int) {

    }
}