package kotx.customgui.gui.guis

import com.mojang.blaze3d.matrix.*
import kotx.customgui.gui.*
import kotx.customgui.view.*

object EditorGUI : GUI() {
    val holders = mutableListOf<ViewHolder>()
    override fun draw(stack: MatrixStack, mouseX: Int, mouseY: Int) {
        holders.sortedBy { it.index }.forEach {
            it.view.renderer.renderPreview(
                it.view.x1,
                it.view.y1,
                it.view.x2,
                it.view.y2,
                it.view
            )
        }
    }
}