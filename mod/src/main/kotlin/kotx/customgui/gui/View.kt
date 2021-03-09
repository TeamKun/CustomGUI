package kotx.customgui.gui

interface View {
    val type: String
    var startX: Int
    var startY: Int
    var endX: Int
    var endY: Int
    val width: Int
        get() = endX - startX
    val height: Int
        get() = endY - startY
    val canResize: Boolean

    fun init()
    fun renderPreview(mouseX: Int, mouseY: Int)
    fun renderPage(scaleW: Float, scaleH: Float, opacity: Float)
    fun onClick(mouseX: Int, mouseY: Int, button: Int)
    fun parseToJson(): String
}