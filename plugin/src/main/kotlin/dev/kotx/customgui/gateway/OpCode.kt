package dev.kotx.customgui.gateway

enum class OpCode(val value: Int) {
    SAVE_GUI(1),
    LOAD_GUI(2),
    SHOW_GUI(3),
    UNKNOWN(-1);

    companion object {
        fun get(opCode: Int) = values().find { it.value == opCode } ?: UNKNOWN
    }
}