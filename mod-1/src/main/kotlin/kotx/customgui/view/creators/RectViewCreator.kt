package kotx.customgui.view.creators

import com.mojang.blaze3d.matrix.MatrixStack
import kotx.customgui.gui.guis.editor.EditorGUI
import kotx.customgui.view.ViewCreator
import kotx.customgui.view.ViewType
import kotx.customgui.view.holders.RectViewHolder
import kotx.customgui.view.views.RectView
import java.awt.Color

class RectViewCreator : ViewCreator<RectView>() {
    override val type: ViewType = ViewType.RECT
    override val points: Int = 2

    var initView: RectViewHolder? = null

    override fun initialize() {
        buttonCenter("作成", width / 2, 100) {
            if (initView != null) EditorGUI.holders.removeIf { it.index == initView!!.index }
            build(RectView(Color.WHITE))
        }
    }

    override fun draw(stack: MatrixStack, mouseX: Int, mouseY: Int) {
        rect(stack, 0, 0, width, height, Color(0, 0, 0, 100))
    }
}