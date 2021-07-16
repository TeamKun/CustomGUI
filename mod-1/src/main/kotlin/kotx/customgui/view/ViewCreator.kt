package kotx.customgui.view

import kotx.customgui.gui.GUI
import kotx.customgui.gui.guis.editor.EditorGUI
import kotx.customgui.view.holders.ButtonViewHolder
import kotx.customgui.view.holders.ImageViewHolder
import kotx.customgui.view.holders.RectViewHolder
import kotx.customgui.view.holders.TextViewHolder
import kotx.customgui.view.views.ButtonView
import kotx.customgui.view.views.ImageView
import kotx.customgui.view.views.RectView
import kotx.customgui.view.views.TextView
import kotlin.math.max

abstract class ViewCreator<T : View, E : ViewHolder> : GUI() {
    abstract val type: ViewType
    abstract val points: Int

    var creating = false

    abstract var initView: E?

    var x1: Int = 0
    var y1: Int = 0
    var x2: Int? = null
    var y2: Int? = null

    protected fun build(view: T) {
        val index = (EditorGUI.holders.maxByOrNull { it.index }?.index ?: 0) + 1
        val holder = when (view) {
            is TextView -> TextViewHolder(index, view)
            is RectView -> RectViewHolder(index, view)
            is ButtonView -> ButtonViewHolder(index, view)
            is ImageView -> ImageViewHolder(index, view)
            else -> null
        }

        view.x1 = x1
        view.y1 = y1
        view.x2 = x2!!
        view.y2 = y2!!

        val right = (EditorGUI.editorWidth / 2)
        val bottom = (EditorGUI.editorHeight / 2)

        when {
            view.x2 > right && view.y2 > bottom -> {
                val wRatio = view.x2 - right
                val hRatio = view.y2 - bottom
                val ratio = max(wRatio, hRatio)
                view.x2 -= ratio
                view.y2 -= ratio
            }

            view.x2 > right -> {
                val ratio = view.x2 - right
                view.x2 -= ratio
                view.y2 -= ratio
            }

            view.y2 > bottom -> {
                val ratio = view.y2 - bottom
                view.x2 -= ratio
                view.y2 -= ratio
            }
        }

        EditorGUI.holders.add(holder!!)

        EditorGUI.selectingCreator = -1
        EditorGUI.creatorLastLocation = null

        display(EditorGUI)
    }
}