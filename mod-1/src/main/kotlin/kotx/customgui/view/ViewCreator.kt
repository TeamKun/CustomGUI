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

        if (holder != null)
            EditorGUI.holders.add(holder)

        EditorGUI.selectingCreator = -1
        EditorGUI.lastLocation = null

        display(EditorGUI)
    }
}