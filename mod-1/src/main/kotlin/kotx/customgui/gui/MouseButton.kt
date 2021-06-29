package kotx.customgui.gui

enum class MouseButton(val button: Int) {
    LEFT(1),
    RIGHT(2),
    MIDDLE(3),
    UNKNOWN(-1);

    companion object {
        fun get(button: Int) = values().find { it.button == button } ?: UNKNOWN
    }
}