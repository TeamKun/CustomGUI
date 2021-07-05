package kotx.customgui.gui.guis.editor

import com.mojang.blaze3d.matrix.*
import kotx.customgui.gui.*
import kotx.customgui.gui.MouseButton.*
import kotx.customgui.view.*
import kotx.customgui.view.creators.*
import kotx.customgui.view.holders.*
import kotx.customgui.view.renderers.*
import org.lwjgl.glfw.*
import java.awt.*
import kotlin.math.*

object EditorGUI : GUI() {
    val holders = mutableListOf<ViewHolder>()
    val creators = listOf(
        TextViewCreator(),
        RectViewCreator(),
    )

    const val editorWidth = 360
    const val editorHeight = 203

    val left: Int
        get() = (width / 2) - (editorWidth / 2)

    val right: Int
        get() = (width / 2) + (editorWidth / 2)

    val top: Int
        get() = (height / 2) - (editorHeight / 2)

    val bottom: Int
        get() = (height / 2) + (editorHeight / 2)

    var selectingCreator: Int = -1

    var lastLocation: Pair<Int, Int>? = null

    override fun initialize() {
        val w = width / (creators.size + 1)
        creators.forEachIndexed { i, creator ->
            val x = w * (i + 1)
            buttonCenter(
                creator.type.capitalizedName(),
                x,
                35
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

        //components
        holders.sortedBy { it.index }.forEach {
            val renderer = it.content.renderer

            val x1 = width / 2 + it.content.x1
            val y1 = height / 2 + it.content.y1
            val x2 = width / 2 + it.content.x2
            val y2 = height / 2 + it.content.y2

            when (it) {
                is TextViewHolder -> (renderer as TextViewRenderer).renderPreview(stack, x1, y1, x2, y2, it.content)
                is RectViewHolder -> (renderer as RectViewRenderer).renderPreview(stack, x1, y1, x2, y2, it.content)
                is ButtonViewHolder -> (renderer as ButtonViewRenderer).renderPreview(stack, x1, y1, x2, y2, it.content)
                is ImageViewHolder -> (renderer as ImageViewRenderer).renderPreview(stack, x1, y1, x2, y2, it.content)
            }

            if (it.selecting) {
                rect(stack, x1, y1, x2, y1 + 1, Color(100, 0, 0))
                rect(stack, x1, y1, x1 + 1, y2, Color(100, 0, 0))
                rect(stack, x1, y2, x2, y2 - 1, Color(100, 0, 0))
                rect(stack, x2, y1, x2 - 1, y2, Color(100, 0, 0))
            }

            if (it.content.isHovering(mouseX, mouseY)) {
                rect(stack, x1, y1, x2, y1 + 1, Color(200, 0, 0))
                rect(stack, x1, y1, x1 + 1, y2, Color(200, 0, 0))
                rect(stack, x1, y2, x2, y2 - 1, Color(200, 0, 0))
                rect(stack, x2, y1, x2 - 1, y2, Color(200, 0, 0))
            }

            if (it.moving) {
                rect(stack, x1, y1, x2, y1 + 1, Color(255, 0, 0))
                rect(stack, x1, y1, x1 + 1, y2, Color(255, 0, 0))
                rect(stack, x1, y2, x2, y2 - 1, Color(255, 0, 0))
                rect(stack, x2, y1, x2 - 1, y2, Color(255, 0, 0))
            }
        }

        //info
        if (creators.getOrNull(selectingCreator) != null) {
            rect(stack, 0, 0, width, height, Color(0, 0, 0, 70))
            val creator = creators[selectingCreator]
            val text = when {
                creator.points == 1 -> "${creator.type.capitalizedName()}を置く場所をクリックしてください。"
                creator.points == 2 && lastLocation == null -> "${creator.type.capitalizedName()}の始点をクリックしてください。"
                creator.points == 2 && lastLocation != null -> "${creator.type.capitalizedName()}の終点をクリックしてください。"
                else -> ""
            }

            textCenter(stack, text, width / 2, 55, Color.WHITE)

            if (lastLocation == null) {
                if (isInEditor(mouseX, mouseY))
                    rect(stack, mouseX - 1, mouseY - 1, mouseX + 1, mouseY + 1, Color.GREEN)
            } else {
                if (isInEditor(mouseX, mouseY))
                    rect(stack, mouseX - 1, mouseY - 1, mouseX + 1, mouseY + 1, Color.RED)

                rect(stack, lastLocation!!.first, lastLocation!!.second, max(left, min(right, mouseX)), max(top, min(bottom, mouseY)), Color.YELLOW)
            }
        }
    }

    override fun onMousePress(button: MouseButton, mouseX: Int, mouseY: Int) {
        when (button) {
            RIGHT -> {
                lastLocation = null
                selectingCreator = -1
            }

            LEFT -> {
                if (buttons.any { it.isHovered }) return
                if (!isInEditor(mouseX, mouseY)) return

                if (creators.getOrNull(selectingCreator) != null) {
                    val creator = creators[selectingCreator]
                    when {
                        creator.points == 1 -> {
                            creator.x1 = mouseX - width / 2
                            creator.y1 = mouseY - height / 2

                            display(creator)
                        }

                        creator.points == 2 && lastLocation != null -> {
                            creator.x1 = lastLocation!!.first - width / 2
                            creator.y1 = lastLocation!!.second - height / 2
                            creator.x2 = mouseX - width / 2
                            creator.y2 = mouseY - height / 2

                            display(creator)
                        }

                        else -> {
                            lastLocation = mouseX to mouseY
                        }
                    }
                } else {
                    holders.sortedByDescending { it.index }.firstOrNull { it.content.isHovering(mouseX, mouseY) }?.apply {
                        selecting = true
                        moving = true
                    } ?: holders.forEach {
                        it.selecting = false
                        it.moving = false
                    }
                }
            }
        }
    }

    override fun onMouseDragDiff(button: MouseButton, x: Int, y: Int) {
        if (button != LEFT) return

        val view = holders.find { it.moving }?.content ?: return

        val nextX1 = view.x1 + x
        val nextY1 = view.y1 + y
        val nextX2 = view.x2 + x
        val nextY2 = view.y2 + y

        if (isInEditor(nextX1 + width / 2, nextY1 + height / 2) && isInEditor(nextX2 + width / 2, nextY2 + height / 2)) {
            view.x1 = nextX1
            view.y1 = nextY1
            view.x2 = nextX2
            view.y2 = nextY2
        }
    }

    override fun onMouseRelease(button: MouseButton, mouseX: Int, mouseY: Int) {
        holders.find { it.moving }?.moving = false
    }

    override fun onKeyPress(key: Int, modifiers: Int): Boolean {
        when {
            key == GLFW.GLFW_KEY_DELETE -> holders.removeIf { it.selecting }
            key == GLFW.GLFW_KEY_ESCAPE && selectingCreator != -1 -> {
                selectingCreator = -1
                return false
            }
            key == GLFW.GLFW_KEY_C && modifiers == GLFW.GLFW_MOD_CONTROL -> {
                holders.removeIf { it.selecting }
            }

            key == GLFW.GLFW_KEY_X && modifiers == GLFW.GLFW_MOD_CONTROL -> {
                holders.removeIf { it.selecting }
            }

            key == GLFW.GLFW_KEY_V && modifiers == GLFW.GLFW_MOD_CONTROL -> {
                holders.removeIf { it.selecting }
            }
        }

        return true
    }

    override fun onClose() {
        lastLocation = null
        selectingCreator = -1
        holders.forEach {
            it.moving = false
            it.selecting = false
        }
    }

    private fun isInEditor(mouseX: Int, mouseY: Int) =
        mouseX in left..right && mouseY in top..bottom

    private fun View.isHovering(mouseX: Int, mouseY: Int): Boolean {
        return mouseX in (x1 + this@EditorGUI.width / 2)..(x2 + this@EditorGUI.width / 2)
                && mouseY in (y1 + this@EditorGUI.height / 2)..(y2 + this@EditorGUI.height / 2)
    }
}