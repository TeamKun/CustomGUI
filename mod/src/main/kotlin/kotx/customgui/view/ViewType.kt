package kotx.customgui.view

enum class ViewType(
    val value: Int
) {
    TEXT(1),
    BUTTON(2),
    RECT(3),
    IMAGE(4),
    UNKNOWN(-1);

    fun capitalizedName() = name.lowercase().replaceFirstChar { it.uppercase() }

    companion object {
        fun get(type: Int) = values().find { it.value == type } ?: UNKNOWN
    }
}