package dev.kotx.customgui.commands

import dev.kotx.flylib.command.Command

class AddCommand : Command("add") {
    init {
        description("現在作業中のGUIを名前をつけて保存します。")
    }
}