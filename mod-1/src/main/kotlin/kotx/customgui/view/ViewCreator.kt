package kotx.customgui.view

import kotx.customgui.gui.*
import kotx.customgui.gui.guis.*
import kotx.customgui.view.holders.*
import kotx.customgui.view.views.*

abstract class ViewCreator<T : View> : GUI() {
    abstract val type: ViewType
    abstract val points: Int

    protected fun build(view: T) {
        val holder = when (view) {
            is ButtonView -> ButtonViewHolder(1, view)
            else -> null
        }


        if (holder != null)
            EditorGUI.holders.add(holder)

        display(EditorGUI)
    }
}