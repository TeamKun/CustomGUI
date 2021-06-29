package kotx.customgui.view.holder

enum class ViewType(val value: Int) {
    TEXT(1),
    BUTTON(2),
    RECT(3),
    IMAGE(4),
    UNKNOWN(-1);

    companion object {
        fun get(type: Int) = values().find { it.value == type } ?: UNKNOWN
    }
}