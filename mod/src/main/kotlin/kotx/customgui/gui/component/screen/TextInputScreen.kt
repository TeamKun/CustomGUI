package kotx.customgui.gui.component.screen

import kotx.customgui.*
import kotx.customgui.gui.GuiDesignerScreen
import kotx.customgui.gui.component.view.TextView
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.screen.Screen
import net.minecraft.client.gui.widget.TextFieldWidget
import net.minecraft.util.text.StringTextComponent
import net.minecraftforge.fml.client.gui.widget.ExtendedButton
import org.lwjgl.glfw.GLFW
import java.awt.Color

class TextInputScreen(
    private val startX: Int,
    private val startY: Int
) : Screen(StringTextComponent("テキストを入力")) {

    private var text = ""
    private var widget: TextFieldWidget? = null
    private val fieldWidth = 320
    private val fieldHeight = 20

    override fun init() {
        text = ""
        widget = TextFieldWidget(
            font,
            xCenter - fieldWidth / 2, yCenter - fieldHeight / 2, fieldWidth, fieldHeight, "テキストを入力"
        ).apply {
            text = ""

            setEnableBackgroundDrawing(true)
            setCanLoseFocus(true)
            setMaxStringLength(1024)

            setResponder {
                this@TextInputScreen.text = it
            }
        }

        addButton(widget!!)
        addButton(ExtendedButton(
            xCenter - 100, scaledHeight - 70, 80, 20, "確定"
        ) {
            postData()
            minecraft?.displayGuiScreen(GuiDesignerScreen)
        })
        addButton(ExtendedButton(
            xCenter + 20, scaledHeight - 70, 80, 20, "キャンセル"
        ) {
            minecraft?.displayGuiScreen(GuiDesignerScreen)
        })
        super.init()
    }

    override fun render(mouseX: Int, mouseY: Int, partialTicks: Float) {
        fillAbsolute(0, 0, scaledWidth, scaledHeight, Color(0, 0, 0, 100))
        Minecraft.getInstance().fontRenderer.drawStringCentered("テキストを入力", width / 2, 60, Color.WHITE)
        super.render(mouseX, mouseY, partialTicks)
    }

    override fun keyPressed(p_keyPressed_1_: Int, p_keyPressed_2_: Int, p_keyPressed_3_: Int): Boolean {
        if (p_keyPressed_1_ == GLFW.GLFW_KEY_ESCAPE) {
            minecraft?.displayGuiScreen(GuiDesignerScreen)
            return false
        }

        if (p_keyPressed_1_ == GLFW.GLFW_KEY_ENTER) {
            postData()
            minecraft?.displayGuiScreen(GuiDesignerScreen)
            return false
        }

        return super.keyPressed(p_keyPressed_1_, p_keyPressed_2_, p_keyPressed_3_)
    }

    private fun postData() {
        GuiDesignerScreen.views.add(
            TextView().apply {
                this.startX = this@TextInputScreen.startX
                this.startY = this@TextInputScreen.startY
                this.endX = this.startX + this@TextInputScreen.text.let { font.getStringWidth(it) }
                this.endY = this.startY + this@TextInputScreen.text.let { font.getWordWrappedHeight(it, Int.MAX_VALUE) }
                this.text = this@TextInputScreen.text
            }.apply {
                init()
            }
        )
    }
}