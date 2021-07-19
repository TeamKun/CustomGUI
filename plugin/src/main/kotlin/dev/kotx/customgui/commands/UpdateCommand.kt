package dev.kotx.customgui.commands

import dev.kotx.flylib.command.Command


class UpdateCommand : Command("update") {
    init {
        description("指定したGUIを現在作業中のGUIで更新します。現在存在しないGUIを指定することは出来ません。")
    }
}