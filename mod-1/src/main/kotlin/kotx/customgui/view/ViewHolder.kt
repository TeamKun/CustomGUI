package kotx.customgui.view

interface ViewHolder {
    val index: Int
    val content: View
    var moving: Boolean
    var selecting: Boolean

    fun copy(newIndex: Int): ViewHolder
}