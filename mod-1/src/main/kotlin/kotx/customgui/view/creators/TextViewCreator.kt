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
        textFieldCenter("テキスト", width / 2, 50, 100, fontRenderer.FONT_HEIGHT + 11)
        buttonCenter("作成", width / 2, 100)
    }

    override fun draw(stack: MatrixStack, mouseX: Int, mouseY: Int) {
        rect(stack, 0, 0, width, height, Color(0, 0, 0, 100))
    }
}