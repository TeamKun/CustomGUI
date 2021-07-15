package kotx.customgui.gui.guis.editor

import kotx.customgui.gui.GUI
import kotx.customgui.view.ViewHolder
import kotx.customgui.view.creators.ImageViewCreator
import kotx.customgui.view.creators.RectViewCreator
import kotx.customgui.view.creators.TextViewCreator

object NewEditorGUI : GUI() {
    val holders = mutableListOf<ViewHolder>()
    val creators = listOf(
        TextViewCreator(),
        RectViewCreator(),
        ImageViewCreator()
    )

    const val editorWidth = 360
    const val editorHeight = 203

    val editorX1: Int
        get() = (width / 2) - (editorWidth / 2)

    val editorY1: Int
        get() = (height / 2) - (editorHeight / 2)

    val editorX2: Int
        get() = (width / 2) + (editorWidth / 2)

    val editorY2: Int
        get() = (height / 2) + (editorHeight / 2)

    override fun initialize() {
        val space = width / (creators.size + 1)
        creators.forEachIndexed { index, creator ->
            buttonCenter(
                creator.type.capitalizedName(),
                space * (index + 1),
                35
            ) {
                creators.forEach { it.creating = false }
                creator.creating = true
            }
        }
    }
}