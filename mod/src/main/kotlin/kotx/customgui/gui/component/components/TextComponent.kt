package kotx.customgui.gui.component.components

import com.mojang.blaze3d.matrix.MatrixStack
import kotx.customgui.drawStringCentered
import kotx.customgui.fillAbsolute
import kotx.customgui.gui.GuiDesignerScreen
import kotx.customgui.gui.component.IComponent
import kotx.customgui.gui.component.screen.TextInputScreen
import kotx.customgui.xCenter
import kotx.customgui.yCenter
import net.minecraft.client.Minecraft
import java.awt.Color

object TextComponent : IComponent {
    override val text: String = "テキスト"

    override fun init() {

    }

    override fun render(stack: MatrixStack, mouseX: Int, mouseY: Int, width: Int, height: Int) {
        if (GuiDesignerScreen.isInRange(mouseX, mouseY))
            fillAbsolute(
                stack, mouseX - 1, mouseY - 1, mouseX + 1, mouseY + 1, Color.RED
            )

        Minecraft.getInstance().font.drawStringCentered(stack, "テキストを配置する場所をクリック", width / 2, 60, Color.WHITE)
    }

    override fun onMouseMove(mouseX: Int, mouseY: Int) {

    }

    override fun onMouseClick(mouseX: Int, mouseY: Int, button: Int) {
        Minecraft.getInstance().setScreen(
            TextInputScreen(
                mouseX - xCenter,
                mouseY - yCenter,
            )
        )
    }

    override fun onKeyPress(keyCode: Int, scanCode: Int, modifiers: Int) {

    }
}