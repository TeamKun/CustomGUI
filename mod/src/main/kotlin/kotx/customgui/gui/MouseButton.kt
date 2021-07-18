package kotx.customgui.gui

enum class MouseButton(val button: Int) {
    LEFT(0),
    RIGHT(1),
    MIDDLE(2),
    UNKNOWN(-1);

    companion object {
        fun get(button: Int) = values().find { it.button == button } ?: UNKNOWN
    }
}