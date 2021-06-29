package kotx.customgui.gui.guis

import com.mojang.blaze3d.matrix.*
import kotx.customgui.gui.*
import kotx.customgui.view.*
import kotx.customgui.view.renderers.*
import kotx.customgui.view.views.*

object EditorGUI : GUI() {
    val holders = mutableListOf<ViewHolder>()
    override fun draw(stack: MatrixStack, mouseX: Int, mouseY: Int) {
        holders.sortedBy { it.index }.forEach {
            val renderer = it.view.renderer
            when (it.type) {
                ViewType.TEXT -> (renderer as TextViewRenderer).renderPreview(stack, it.view.x1, it.view.y1, it.view.x2, it.view.y2, it.view as TextView)
                ViewType.RECT -> (renderer as RectViewRenderer).renderPreview(stack, it.view.x1, it.view.y1, it.view.x2, it.view.y2, it.view as RectView)
            }
        }
    }
}