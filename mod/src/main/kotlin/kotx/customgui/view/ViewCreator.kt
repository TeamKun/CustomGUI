package kotx.customgui.view

import kotx.customgui.gui.GUI
import kotx.customgui.gui.guis.EditorGUI

import kotx.customgui.view.holders.ButtonViewHolder
import kotx.customgui.view.holders.ImageViewHolder
import kotx.customgui.view.holders.RectViewHolder
import kotx.customgui.view.holders.TextViewHolder
import kotx.customgui.view.views.ButtonView
import kotx.customgui.view.views.ImageView
import kotx.customgui.view.views.RectView
import kotx.customgui.view.views.TextView
import org.lwjgl.glfw.GLFW
import kotlin.math.max
import kotlin.math.min

abstract class ViewCreator<T : View, E : ViewHolder> : GUI() {
    abstract val type: ViewType
    abstract val points: Int

    var creating = false

    abstract var initView: E?

    var x1: Int = 0
    var y1: Int = 0
    var x2: Int? = null
    var y2: Int? = null

    override fun onKeyPress(key: Int, modifiers: Int): Boolean {
        return if (key == GLFW.GLFW_KEY_ESCAPE) {
            display(EditorGUI)
            false
        } else {
            true
        }
    }

    protected fun build(view: T) {
        val index = (EditorGUI.holders.maxByOrNull { it.index }?.index ?: 0) + 1
        val holder = when (view) {
            is TextView -> TextViewHolder(index, view)
            is RectView -> RectViewHolder(index, view)
            is ButtonView -> ButtonViewHolder(index, view)
            is ImageView -> ImageViewHolder(index, view)
            else -> null
        }

        val actX1 = min(x1, x2!!)
        val actY1 = min(y1, y2!!)
        val actX2 = max(x1, x2!!)
        val actY2 = max(y1, y2!!)

        view.x1 = actX1
        view.y1 = actY1
        view.x2 = actX2
        view.y2 = actY2

        val right = (EditorGUI.editorWidth / 2)
        val bottom = (EditorGUI.editorHeight / 2)

        when {
            view.x2 > right && view.y2 > bottom -> {
                val xRa = right.toDouble() / view.x2.toDouble()
                val yRa = bottom.toDouble() / view.y2.toDouble()
                val ra = min(xRa, yRa)
                view.x2 = (view.x2.toDouble() * ra).toInt()
                view.y2 = (view.y2.toDouble() * ra).toInt()
            }

            view.x2 > right -> {
                val ra = right.toDouble() / view.x2.toDouble()
                view.x2 = (view.x2.toDouble() * ra).toInt()
                view.y2 = (view.y2.toDouble() * ra).toInt()
            }

            view.y2 > bottom -> {
                val ra = bottom.toDouble() / view.y2.toDouble()
                view.x2 = (view.x2.toDouble() * ra).toInt()
                view.y2 = (view.y2.toDouble() * ra).toInt()
            }
        }

        EditorGUI.holders.add(holder!!)

        EditorGUI.selectingCreator = -1
        EditorGUI.creatorLastLocation = null

        display(EditorGUI)
    }
}