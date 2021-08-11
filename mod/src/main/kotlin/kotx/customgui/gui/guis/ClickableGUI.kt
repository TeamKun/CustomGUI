package kotx.customgui.gui.guis

import com.mojang.blaze3d.matrix.MatrixStack
import kotx.customgui.gui.GUI
import kotx.customgui.gui.MouseButton
import kotx.customgui.view.View
import kotx.customgui.view.ViewHolder
import kotx.customgui.view.holders.ButtonViewHolder
import kotx.customgui.view.holders.ImageViewHolder
import kotx.customgui.view.holders.RectViewHolder
import kotx.customgui.view.holders.TextViewHolder
import kotx.customgui.view.renderers.ButtonViewRenderer
import kotx.customgui.view.renderers.ImageViewRenderer
import kotx.customgui.view.renderers.RectViewRenderer
import kotx.customgui.view.renderers.TextViewRenderer

object ClickableGUI : GUI() {
    val holders = mutableListOf<ViewHolder>()
    var opacity = 0.0

    override fun draw(stack: MatrixStack, mouseX: Int, mouseY: Int) {
        if (opacity <= 0.0) return

        holders.sortedBy { it.index }.forEach {
            val renderer = it.content.renderer

            val x1 = width / 2 + it.content.x1
            val y1 = height / 2 + it.content.y1
            val x2 = width / 2 + it.content.x2
            val y2 = height / 2 + it.content.y2

            when (it) {
                is TextViewHolder -> (renderer as TextViewRenderer).renderFull(stack, x1, y1, x2, y2, it.content)
                is RectViewHolder -> (renderer as RectViewRenderer).renderFull(stack, x1, y1, x2, y2, it.content)
                is ButtonViewHolder -> (renderer as ButtonViewRenderer).renderFull(stack, x1, y1, x2, y2, it.content)
                is ImageViewHolder -> (renderer as ImageViewRenderer).renderFull(stack, x1, y1, x2, y2, it.content)
            }
        }
    }

    override fun onMousePress(button: MouseButton, mouseX: Int, mouseY: Int) {
        holders.filter { it.content.isHovering(mouseX, mouseY) }.forEach {
            it.content.onClick()
        }
    }

    private fun View.isHovering(mouseX: Int, mouseY: Int): Boolean {
        return mouseX in (x1 + this@ClickableGUI.width / 2)..(x2 + this@ClickableGUI.width / 2)
                && mouseY in (y1 + this@ClickableGUI.height / 2)..(y2 + this@ClickableGUI.height / 2)
    }
}