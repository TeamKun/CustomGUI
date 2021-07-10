package kotx.customgui.gui.guis.editor

import com.mojang.blaze3d.matrix.MatrixStack
import kotx.customgui.gui.GUI
import kotx.customgui.gui.MouseButton
import kotx.customgui.gui.MouseButton.LEFT
import kotx.customgui.gui.MouseButton.RIGHT
import kotx.customgui.view.View
import kotx.customgui.view.ViewHolder
import kotx.customgui.view.creators.ImageViewCreator
import kotx.customgui.view.creators.RectViewCreator
import kotx.customgui.view.creators.TextViewCreator
import kotx.customgui.view.holders.ButtonViewHolder
import kotx.customgui.view.holders.ImageViewHolder
import kotx.customgui.view.holders.RectViewHolder
import kotx.customgui.view.holders.TextViewHolder
import kotx.customgui.view.renderers.ButtonViewRenderer
import kotx.customgui.view.renderers.ImageViewRenderer
import kotx.customgui.view.renderers.RectViewRenderer
import kotx.customgui.view.renderers.TextViewRenderer
import org.lwjgl.glfw.GLFW
import java.awt.Color
import kotlin.math.max
import kotlin.math.min

object EditorGUI : GUI() {
    val holders = mutableListOf<ViewHolder>()
    val creators = listOf(
            TextViewCreator(),
            RectViewCreator(),
            ImageViewCreator()
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
    var creatorLastLocation: Pair<Int, Int>? = null

    var dragLastLocation: Pair<Int, Int>? = null

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

            val color = when {
                it.moving -> 255
                it.content.isHovering(mouseX, mouseY) -> 200
                it.selecting -> 100
                else -> 0
            }

            rect(stack, x1, y1, x2, y1 + 1, Color(color, 0, 0))
            rect(stack, x1, y1, x1 + 1, y2, Color(color, 0, 0))
            rect(stack, x1, y2, x2, y2 - 1, Color(color, 0, 0))
            rect(stack, x2, y1, x2 - 1, y2, Color(color, 0, 0))

            if (it.selecting && !it.moving) {
                //center
                rectCenter(stack, (x1 + x2) / 2, (y1 + y2) / 2, 2, 2, Color(color, 0, 0))
                //corner
                val leftTop = object {
                    val x1 = x1 - 1
                    val y1 = y1 - 1
                    val x2 = x1 + 2
                    val y2 = y1 + 2
                    val validator: (Pair<Int, Int>) -> Boolean = { it.first in x1..x2 && it.second in y1..y2 }
                }
                val leftBottom = object {
                    val x1 = x1 - 1
                    val y1 = y2 - 2
                    val x2 = x1 + 2
                    val y2 = y2 + 1
                    val validator: (Pair<Int, Int>) -> Boolean = { it.first in x1..x2 && it.second in y1..y2 }
                }
                val rightTop = object {
                    val x1 = x2 - 2
                    val y1 = y1 - 1
                    val x2 = x2 + 1
                    val y2 = y1 + 2
                    val validator: (Pair<Int, Int>) -> Boolean = { it.first in x1..x2 && it.second in y1..y2 }
                }
                val rightBottom = object {
                    val x1 = x2 - 2
                    val y1 = y2 - 2
                    val x2 = x2 + 1
                    val y2 = y2 + 1
                    val validator: (Pair<Int, Int>) -> Boolean = { it.first in x1..x2 && it.second in y1..y2 }
                }
                rect(stack, leftTop.x1, leftTop.y1, leftTop.x2, leftTop.y2, if (leftTop.validator(mouseX to mouseY)) Color.RED else Color(color, 0, 0))
                rect(stack, leftBottom.x1, leftBottom.y1, leftBottom.x2, leftBottom.y2, if (leftBottom.validator(mouseX to mouseY)) Color.RED else Color(color, 0, 0))
                rect(stack, rightTop.x1, rightTop.y1, rightTop.x2, rightTop.y2, if (rightTop.validator(mouseX to mouseY)) Color.RED else Color(color, 0, 0))
                rect(stack, rightBottom.x1, rightBottom.y1, rightBottom.x2, rightBottom.y2, if (rightBottom.validator(mouseX to mouseY)) Color.RED else Color(color, 0, 0))
            }
        }

        //info
        if (creators.getOrNull(selectingCreator) != null) {
            rect(stack, 0, 0, width, height, Color(0, 0, 0, 70))
            val creator = creators[selectingCreator]
            val text = when {
                creator.points == 1 -> "${creator.type.capitalizedName()}を置く場所をクリックしてください。"
                creator.points == 2 && creatorLastLocation == null -> "${creator.type.capitalizedName()}の始点をクリックしてください。"
                creator.points == 2 && creatorLastLocation != null -> "${creator.type.capitalizedName()}の終点をクリックしてください。"
                else -> ""
            }

            textCenter(stack, text, width / 2, 55, Color.WHITE)

            if (creatorLastLocation == null) {
                if (isInEditor(mouseX, mouseY))
                    rect(stack, mouseX - 1, mouseY - 1, mouseX + 1, mouseY + 1, Color.GREEN)
            } else {
                if (isInEditor(mouseX, mouseY)) {
                    rect(stack, creatorLastLocation!!.first, creatorLastLocation!!.second, max(left, min(right, mouseX)), max(top, min(bottom, mouseY)), Color(255, 0, 0, 100))
                }
            }
        }

        if (dragLastLocation != null && creators.getOrNull(selectingCreator) == null) {
            rect(
                    stack,
                    dragLastLocation!!.first,
                    dragLastLocation!!.second,
                    max(left, min(right, mouseX)),
                    max(top, min(bottom, mouseY)),
                    Color(255, 0, 0, 100)
            )
        }
    }

    override fun onMousePress(button: MouseButton, mouseX: Int, mouseY: Int) {
        when (button) {
            RIGHT -> {
                dragLastLocation = null
                creatorLastLocation = null
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

                        creator.points == 2 && creatorLastLocation != null -> {
                            creator.x1 = creatorLastLocation!!.first - width / 2
                            creator.y1 = creatorLastLocation!!.second - height / 2
                            creator.x2 = mouseX - width / 2
                            creator.y2 = mouseY - height / 2

                            display(creator)
                        }

                        else -> {
                            creatorLastLocation = mouseX to mouseY
                        }
                    }
                } else {
                    holders.forEach {
                        it.selecting = false
                        it.moving = false
                    }

                    val holder = holders.sortedByDescending { it.index }.firstOrNull { it.content.isHovering(mouseX, mouseY) }

                    if (holder != null) {
                        holder.apply {
                            selecting = true
                            moving = true
                        }
                    } else {
                        dragLastLocation = mouseX to mouseY
                    }
                }
            }
        }
    }

    override fun onMouseDragDiff(button: MouseButton, x: Int, y: Int) {
        if (button != LEFT) return

        val view = holders.find { it.moving }?.content

        if (view == null) {
            //select items
        } else {
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
    }

    override fun onMouseRelease(button: MouseButton, mouseX: Int, mouseY: Int) {
        holders.find { it.moving }?.moving = false
        dragLastLocation = null
    }

    private var clipboard: ViewHolder? = null
    override fun onKeyPress(key: Int, modifiers: Int): Boolean {
        when {
            key == GLFW.GLFW_KEY_DELETE -> holders.removeIf { it.selecting }
            key == GLFW.GLFW_KEY_ESCAPE && selectingCreator != -1 -> {
                selectingCreator = -1
                return false
            }
            key == GLFW.GLFW_KEY_ESCAPE && (holders.any { it.selecting } || holders.any { it.moving }) -> {
                holders.forEach {
                    it.selecting = false
                    it.moving = false
                }
                return false
            }
            key == GLFW.GLFW_KEY_C && modifiers == GLFW.GLFW_MOD_CONTROL -> {
                holders.find { it.selecting }?.also { clipboard = it.copy(holders.maxOf { it.index } + 1) }
            }

            key == GLFW.GLFW_KEY_X && modifiers == GLFW.GLFW_MOD_CONTROL -> {
                holders.find { it.selecting }?.also { clipboard = it.copy(holders.maxOf { it.index } + 1) }
                holders.removeIf { it.selecting }
            }

            key == GLFW.GLFW_KEY_V && modifiers == GLFW.GLFW_MOD_CONTROL -> {
                clipboard?.also { holders.add(it) }
            }
        }

        return true
    }

    override fun onClose() {
        creatorLastLocation = null
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