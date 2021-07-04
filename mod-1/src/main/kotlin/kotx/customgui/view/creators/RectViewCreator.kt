package kotx.customgui.view.creators

import com.mojang.blaze3d.matrix.*
import kotx.customgui.view.*
import kotx.customgui.view.views.*
import java.awt.*

class RectViewCreator : ViewCreator<RectView>() {
    override val type: ViewType = ViewType.RECT
    override val points: Int = 2

    override fun initialize() {
        buttonCenter("作成", width / 2, 100) {
            build(RectView(Color.WHITE))
        }
    }

    override fun draw(stack: MatrixStack, mouseX: Int, mouseY: Int) {
        rect(stack, 0, 0, width, height, Color(0, 0, 0, 100))
    }
}