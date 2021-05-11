package kotx.customgui.gui.component.screen

import com.mojang.blaze3d.matrix.MatrixStack
import kotx.customgui.*
import kotx.customgui.gui.GuiDesignerScreen
import kotx.customgui.gui.component.view.ButtonView
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.screen.Screen
import net.minecraft.client.gui.widget.TextFieldWidget
import net.minecraft.util.text.StringTextComponent
import net.minecraftforge.fml.client.gui.widget.ExtendedButton
import org.lwjgl.glfw.GLFW
import java.awt.Color

class CommandInputScreen(
    private val startX: Int,
    private val startY: Int,
    private val endX: Int,
    private val endY: Int
) : Screen(StringTextComponent("コマンドを入力")) {

    private var command = ""
    private var widget: TextFieldWidget? = null
    private val fieldWidth = 320
    private val fieldHeight = 20

    override fun init() {
        command = ""
        widget = TextFieldWidget(
            font,
            xCenter - fieldWidth / 2, yCenter - fieldHeight / 2, fieldWidth, fieldHeight, StringTextComponent("コマンドを入力")
        ).apply {
            this.text = ""

            setCanLoseFocus(true)
            setMaxStringLength(1024)

            setResponder {
                command = it
            }
        }

        addButton(widget!!)
        addButton(ExtendedButton(
            xCenter - 100, scaledHeight - 70, 80, 20, StringTextComponent("確定")
        ) {
            postData()
            minecraft?.displayGuiScreen(GuiDesignerScreen)
        })
        addButton(ExtendedButton(
            xCenter + 20, scaledHeight - 70, 80, 20, StringTextComponent("キャンセル")
        ) {
            minecraft?.displayGuiScreen(GuiDesignerScreen)
        })
        super.init()
    }

    override fun render(stack: MatrixStack, mouseX: Int, mouseY: Int, partialTicks: Float) {
        fillAbsolute(stack, 0, 0, scaledWidth, scaledHeight, Color(0, 0, 0, 100))
        Minecraft.getInstance().fontRenderer.drawStringCentered(stack, "コマンドを入力", width / 2, 60, Color.WHITE)
        super.render(stack, mouseX, mouseY, partialTicks)
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
            ButtonView().apply {
                this.startX = this@CommandInputScreen.startX
                this.startY = this@CommandInputScreen.startY
                this.endX = this@CommandInputScreen.endX
                this.endY = this@CommandInputScreen.endY
                this.command = this@CommandInputScreen.command.run { if (startsWith("/")) this else "/$this" }
            }.apply {
                init()
            }
        )
    }
}