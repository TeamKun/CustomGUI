package kotx.customgui.gui.guis

import com.mojang.blaze3d.matrix.*
import kotx.customgui.gui.*
import kotx.customgui.view.*
import kotx.customgui.view.renderers.*
import kotx.customgui.view.views.*
import java.awt.*

object EditorGUI : GUI() {
    val holders = mutableListOf<ViewHolder>()

    private const val editorWidth = 360
    private const val editorHeight = 203

    override fun initialize() {
        button("Text", 10, 10)
    }

    override fun draw(stack: MatrixStack, mouseX: Int, mouseY: Int) {
        //Dimmed background
        rect(stack, 0, 0, width, height, Color(0, 0, 0, 100))
        //Editor background
        rectCenter(stack, width / 2, height / 2, editorWidth, editorHeight, Color(12, 12, 12))


        holders.sortedBy { it.index }.forEach {
            val renderer = it.content.renderer
            when (it.type) {
                ViewType.TEXT -> (renderer as TextViewRenderer).renderPreview(stack, it.content.x1, it.content.y1, it.content.x2, it.content.y2, it.content as TextView)
                ViewType.RECT -> (renderer as RectViewRenderer).renderPreview(stack, it.content.x1, it.content.y1, it.content.x2, it.content.y2, it.content as RectView)
                ViewType.BUTTON -> (renderer as ButtonViewRenderer).renderPreview(stack, it.content.x1, it.content.y1, it.content.x2, it.content.y2, it.content as ButtonView)
            }
        }
    }
}