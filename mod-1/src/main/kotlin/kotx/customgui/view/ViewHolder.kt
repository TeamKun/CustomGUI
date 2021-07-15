package kotx.customgui.view

interface ViewHolder {
    var index: Int
    val content: View
    var moving: Boolean
    var selecting: Boolean
    var scalable: Boolean
    var scaling: Boolean
}