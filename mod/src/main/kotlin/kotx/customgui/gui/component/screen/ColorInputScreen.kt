package kotx.customgui.gui.component.screen

import kotx.customgui.*
import kotx.customgui.gui.GuiDesignerScreen
import kotx.customgui.gui.component.view.RectView
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.screen.Screen
import net.minecraft.client.gui.widget.TextFieldWidget
import net.minecraft.util.text.StringTextComponent
import net.minecraftforge.fml.client.gui.widget.ExtendedButton
import org.lwjgl.glfw.GLFW
import java.awt.Color

class ColorInputScreen(
    private val startX: Int,
    private val startY: Int,
    private val endX: Int,
    private val endY: Int
) : Screen(StringTextComponent("色を入力")) {

    private val fieldWidth = 60
    private val fieldHeight = 20
    private var r = 0
    private var g = 0
    private var b = 0

    override fun init() {

        val confirmButton = ExtendedButton(
            xCenter - 100, scaledHeight - 70, 80, 20, "確定"
        ) {
            postData()
            minecraft?.displayGuiScreen(GuiDesignerScreen)
        }
        confirmButton.active = false

        addButton(confirmButton)
        addButton(
            ExtendedButton(
                xCenter + 20, scaledHeight - 70, 80, 20, "キャンセル"
            ) {
                minecraft?.displayGuiScreen(GuiDesignerScreen)
            })

        addButton(
            TextFieldWidget(
                font,
                xCenter - fieldWidth / 2, yCenter - fieldHeight / 2 - fieldHeight - 20, fieldWidth, fieldHeight, "赤"
            ).apply {
                setMaxStringLength(3)
                setResponder {
                    confirmButton.active = it.toIntOrNull() != null && it.toIntOrNull() in 0..255
                    if (confirmButton.active)
                        r = it.toInt()
                }
            })
        addButton(TextFieldWidget(
            font,
            xCenter - fieldWidth / 2, yCenter - fieldHeight / 2, fieldWidth, fieldHeight, "緑"
        ).apply {
            setMaxStringLength(3)
            setValidator {
                it.toIntOrNull() in 0..255
            }
            setResponder {
                confirmButton.active = it.toIntOrNull() != null && it.toIntOrNull() in 0..255
                if (confirmButton.active)
                    g = it.toInt()
            }
        })
        addButton(TextFieldWidget(
            font,
            xCenter - fieldWidth / 2, yCenter - fieldHeight / 2 + fieldHeight + 20, fieldWidth, fieldHeight, "青"
        ).apply {
            setMaxStringLength(3)
            setResponder {
                confirmButton.active = it.toIntOrNull() != null && it.toIntOrNull() in 0..255
                if (confirmButton.active)
                    b = it.toInt()
            }
        })

        super.init()
    }

    override fun render(mouseX: Int, mouseY: Int, partialTicks: Float) {
        fillAbsolute(0, 0, scaledWidth, scaledHeight, Color(0, 0, 0, 100))
        Minecraft.getInstance().fontRenderer.drawString(
            "R (赤)",
            xCenter - fieldWidth / 2 - 50,
            yCenter - fieldHeight / 2 - fieldHeight - 20,
            Color.WHITE
        )
        Minecraft.getInstance().fontRenderer.drawString(
            "G (緑)",
            xCenter - fieldWidth / 2 - 50,
            yCenter - fieldHeight / 2,
            Color.WHITE
        )
        Minecraft.getInstance().fontRenderer.drawString(
            "B (青)",
            xCenter - fieldWidth / 2 - 50,
            yCenter - fieldHeight / 2 + fieldHeight + 20,
            Color.WHITE
        )
        Minecraft.getInstance().fontRenderer.drawStringCentered("コマンドを入力", width / 2, 60, Color.WHITE)
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
            RectView().apply {
                this.startX = this@ColorInputScreen.startX
                this.startY = this@ColorInputScreen.startY
                this.endX = this@ColorInputScreen.endX
                this.endY = this@ColorInputScreen.endY
                this.r = this@ColorInputScreen.r
                this.g = this@ColorInputScreen.g
                this.b = this@ColorInputScreen.b
            }.apply {
                init()
            }
        )
    }
}