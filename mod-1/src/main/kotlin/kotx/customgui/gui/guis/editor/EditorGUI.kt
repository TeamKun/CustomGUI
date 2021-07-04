package kotx.customgui.gui.guis.editor

import com.mojang.blaze3d.matrix.*
import kotx.customgui.gui.*
import kotx.customgui.view.*
import kotx.customgui.view.creators.*
import kotx.customgui.view.holders.*
import kotx.customgui.view.renderers.*
import org.lwjgl.glfw.*
import java.awt.*

object EditorGUI : GUI() {
    val holders = mutableListOf<ViewHolder>()
    val creators = listOf<ViewCreator<out View>>(
        TextViewCreator()
    )

    private const val editorWidth = 360
    private const val editorHeight = 203

    var selectingCreator: Int = -1

    var lastLocation: Pair<Int, Int>? = null

    override fun initialize() {
        val w = width / (creators.size + 1)
        creators.forEachIndexed { i, creator ->
            val x = w * (i + 1)
            buttonCenter(
                creator.type.capitalizedName(),
                x,
                50
            ) {
                selectingCreator = i
            }
        }
    }

    override fun draw(stack: MatrixStack, mouseX: Int, mouseY: Int) {
        //Dimmed background
        rect(stack, 0, 0, width, height, Color(0, 0, 0, 100))
        //Editor background
        rectCenter(stack, width / 2, height / 2, editorWidth, editorHeight, Color(12, 12, 12))

        //info
        if (creators.getOrNull(selectingCreator) != null) {
            val creator = creators[selectingCreator]
            val text = when {
                creator.points == 1 -> "${creator.type.capitalizedName()}を置く場所をクリックしてください。"
                creator.points == 2 && lastLocation == null -> "${creator.type.capitalizedName()}の始点をクリックしてください。"
                creator.points == 2 && lastLocation != null -> "${creator.type.capitalizedName()}の終点をクリックしてください。"
                else -> ""
            }

            textCenter(stack, text, width / 2, 50, Color.WHITE)

            if (lastLocation == null) {
                rect(stack, mouseX - 1, mouseY - 1, mouseX + 1, mouseY + 1, Color.GREEN)
            } else {
                rect(stack, lastLocation!!.first, lastLocation!!.second, mouseX, mouseY, Color.YELLOW)
                rect(stack, mouseX - 1, mouseY - 1, mouseX + 1, mouseY + 1, Color.RED)
            }
        }

        //components
        holders.sortedBy { it.index }.forEach {
            val renderer = it.content.renderer
            when (it) {
                is TextViewHolder -> (renderer as TextViewRenderer).renderPreview(stack, it.content.x1, it.content.y1, it.content.x2, it.content.y2, it.content)
                is RectViewHolder -> (renderer as RectViewRenderer).renderPreview(stack, it.content.x1, it.content.y1, it.content.x2, it.content.y2, it.content)
                is ButtonViewHolder -> (renderer as ButtonViewRenderer).renderPreview(stack, it.content.x1, it.content.y1, it.content.x2, it.content.y2, it.content)
                is ImageViewHolder -> (renderer as ImageViewRenderer).renderPreview(stack, it.content.x1, it.content.y1, it.content.x2, it.content.y2, it.content)
            }

            if (it.selecting) {
                rect(stack, it.content.x1, it.content.y1, it.content.x2, it.content.y1 + 1, Color(100, 0, 0))
                rect(stack, it.content.x1, it.content.y1, it.content.x1 + 1, it.content.y2, Color(100, 0, 0))
                rect(stack, it.content.x1, it.content.y2, it.content.x2, it.content.y2 - 1, Color(100, 0, 0))
                rect(stack, it.content.x2, it.content.y1, it.content.x2 - 1, it.content.y2, Color(100, 0, 0))
            }

            if (it.content.isHovering(mouseX, mouseY)) {
                rect(stack, it.content.x1, it.content.y1, it.content.x2, it.content.y1 + 1, Color(200, 0, 0))
                rect(stack, it.content.x1, it.content.y1, it.content.x1 + 1, it.content.y2, Color(200, 0, 0))
                rect(stack, it.content.x1, it.content.y2, it.content.x2, it.content.y2 - 1, Color(200, 0, 0))
                rect(stack, it.content.x2, it.content.y1, it.content.x2 - 1, it.content.y2, Color(200, 0, 0))
            }

            if (it.moving) {
                rect(stack, it.content.x1, it.content.y1, it.content.x2, it.content.y1 + 1, Color(255, 0, 0))
                rect(stack, it.content.x1, it.content.y1, it.content.x1 + 1, it.content.y2, Color(255, 0, 0))
                rect(stack, it.content.x1, it.content.y2, it.content.x2, it.content.y2 - 1, Color(255, 0, 0))
                rect(stack, it.content.x2, it.content.y1, it.content.x2 - 1, it.content.y2, Color(255, 0, 0))
            }
        }
    }

    override fun onMousePress(button: MouseButton, mouseX: Int, mouseY: Int) {
        if (!isInEditor(mouseX, mouseY)) return

        if (creators.getOrNull(selectingCreator) != null) {
            val creator = creators[selectingCreator]
            when {
                creator.points == 1 -> {
                    creator.x1 = mouseX
                    creator.y1 = mouseY

                    display(creator)
                }

                creator.points == 2 && lastLocation != null -> {
                    creator.x1 = lastLocation!!.first
                    creator.y1 = lastLocation!!.second
                    creator.x2 = mouseX
                    creator.y2 = mouseY

                    display(creator)
                }

                else -> {
                    lastLocation = mouseX to mouseY
                }
            }
        }

        holders.sortedByDescending { it.index }.firstOrNull { it.content.isHovering(mouseX, mouseY) }?.apply {
            selecting = true
            moving = true
        } ?: holders.forEach {
            it.selecting = false
            it.moving = false
        }
    }

    override fun onMouseDragDiff(button: MouseButton, x: Int, y: Int) {
        if (button != MouseButton.LEFT) return

        val view = holders.find { it.moving }?.content ?: return

        view.x1 += x
        view.y1 += y
        view.x2 += x
        view.y2 += y
    }

    override fun onMouseRelease(button: MouseButton, mouseX: Int, mouseY: Int) {
        holders.find { it.moving }?.moving = false
    }

    override fun onKeyPress(key: Int, modifiers: Int) {
        if (key != GLFW.GLFW_KEY_DELETE) return

        holders.removeIf { it.selecting }
    }

    override fun onClose() {
        holders.forEach {
            it.moving = false
            it.selecting = false
        }
    }

    private fun isInEditor(mouseX: Int, mouseY: Int) =
        mouseX in (width / 2 - editorWidth / 2)..(width / 2 + editorWidth / 2)
                && mouseY in (height / 2 - editorHeight / 2)..(height / 2 + editorHeight / 2)

    private fun View.isHovering(mouseX: Int, mouseY: Int) = mouseX in x1..x2 && mouseY in y1..y2
}