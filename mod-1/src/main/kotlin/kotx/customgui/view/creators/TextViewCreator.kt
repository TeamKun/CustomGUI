package kotx.customgui.view.creators

import com.mojang.blaze3d.matrix.*
import kotx.customgui.util.*
import kotx.customgui.view.*
import kotx.customgui.view.views.*
import java.awt.*

class TextViewCreator : ViewCreator<TextView>() {
    override val type: ViewType = ViewType.TEXT
    override val points: Int = 1

    override fun initialize() {
        val textField = textFieldCenter("テキスト", width / 2, 50, 200, fontRenderer.FONT_HEIGHT + 11).apply {
            setCanLoseFocus(true)
            setMaxStringLength(1024)
            setFocused2(true)
        }

        val button = buttonCenter("作成", width / 2, 100) {
            build(TextView(textField.text, Color.WHITE).apply {
                this.x2 = this@TextViewCreator.x1 + fontRenderer.getStringWidth(textField.text)
                this.y2 = this@TextViewCreator.y1 + fontRenderer.FONT_HEIGHT
            })
        }

        textField.setResponder {
            button.active = it.isNotBlank()
        }

        button.active = false
    }

    override fun draw(stack: MatrixStack, mouseX: Int, mouseY: Int) {
        rect(stack, 0, 0, width, height, Color(0, 0, 0, 100))
    }
}