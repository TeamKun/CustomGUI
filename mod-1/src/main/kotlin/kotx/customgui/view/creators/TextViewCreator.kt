package kotx.customgui.view.creators

import com.mojang.blaze3d.matrix.MatrixStack
import kotx.customgui.util.fontRenderer
import kotx.customgui.view.ViewCreator
import kotx.customgui.view.ViewType
import kotx.customgui.view.views.TextView
import net.minecraft.client.gui.widget.TextFieldWidget
import org.lwjgl.glfw.GLFW
import java.awt.Color

class TextViewCreator : ViewCreator<TextView>() {
    override val type: ViewType = ViewType.TEXT
    override val points: Int = 1

    private lateinit var textField: TextFieldWidget

    override fun initialize() {
        textField = textFieldCenter("テキスト", width / 2, 50, 200, fontRenderer.FONT_HEIGHT + 11).apply {
            setCanLoseFocus(true)
            setMaxStringLength(1024)
            setFocused2(true)
        }

        val button = buttonCenter("作成", width / 2, 100) {
            build(TextView(textField.text, Color.WHITE).apply {
                this@TextViewCreator.x2 = this@TextViewCreator.x1 + fontRenderer.getStringWidth(textField.text)
                this@TextViewCreator.y2 = this@TextViewCreator.y1 + fontRenderer.FONT_HEIGHT
            })
        }

        textField.setResponder {
            button.active = it.isNotBlank()
        }

        button.active = false
    }

    override fun onKeyPress(key: Int, modifiers: Int): Boolean {
        if (key == GLFW.GLFW_KEY_ENTER && textField.text.isNotBlank())
            build(TextView(textField.text, Color.WHITE).apply {
                this@TextViewCreator.x2 = this@TextViewCreator.x1 + fontRenderer.getStringWidth(textField.text)
                this@TextViewCreator.y2 = this@TextViewCreator.y1 + fontRenderer.FONT_HEIGHT
            })

        return true
    }

    override fun draw(stack: MatrixStack, mouseX: Int, mouseY: Int) {
        rect(stack, 0, 0, width, height, Color(0, 0, 0, 100))
    }
}