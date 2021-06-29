package kotx.customgui.view

interface ViewRenderer {
    fun renderPreview(x1: Int, y1: Int, x2: Int, y2: Int, view: View)
    fun renderFull(x1: Int, y1: Int, x2: Int, y2: Int, view: View)
}