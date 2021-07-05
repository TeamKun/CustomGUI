package kotx.customgui.view

import kotx.customgui.gui.*
import kotx.customgui.gui.guis.editor.*
import kotx.customgui.view.holders.*
import kotx.customgui.view.views.*

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
        x2?.also { view.x2 = it }
        y2?.also { view.y2 = it }

        val left = -(EditorGUI.editorWidth / 2)
        val right = (EditorGUI.editorWidth / 2)
        val top = -(EditorGUI.editorHeight / 2)
        val bottom = (EditorGUI.editorHeight / 2)

        if (view.x1 < left) {
            val diff = left - view.x1
            view.x1 += diff
            view.x2 += diff

            if (view.x2 > right)
                view.x2 = right
        } else if (view.x2 > right) {
            val diff = view.x2 - right
            view.x1 -= diff
            view.x2 -= diff

            if (view.x1 < left)
                view.x1 = left
        }

        if (view.y1 < top) {
            val diff = top - view.y1
            view.y1 += diff
            view.y2 += diff

            if (view.y2 > bottom)
                view.y2 = bottom
        } else if (view.y2 > bottom) {
            val diff = view.y2 - bottom
            view.y1 -= diff
            view.y2 -= diff

            if (view.y1 < top)
                view.y1 = top
        }

        if (holder != null)
            EditorGUI.holders.add(holder)

        EditorGUI.selectingCreator = -1
        EditorGUI.lastLocation = null

        display(EditorGUI)
    }
}