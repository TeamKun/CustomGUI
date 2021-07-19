package dev.kotx.customgui.commands

import dev.kotx.flylib.command.Command

class ListCommand : Command("list") {
    init {
        description("全てのGUIの名前と作成者を表示します。")
    }
}