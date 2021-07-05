package kotx.customgui.view

import kotx.customgui.gui.*
import kotx.customgui.gui.guis.editor.*
import kotx.customgui.view.holders.*
import kotx.customgui.view.views.*
import kotlin.math.*

abstract class ViewCreator<T : View> : GUI() {
    abstract val type: ViewType
    abstract val points: Int

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

        if (holder != null)
            EditorGUI.holders.add(holder)

        EditorGUI.selectingCreator = -1
        EditorGUI.lastLocation = null

        display(EditorGUI)
    }
}