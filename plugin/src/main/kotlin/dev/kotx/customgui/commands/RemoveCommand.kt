package dev.kotx.customgui.commands

import dev.kotx.flylib.command.Command

class RemoveCommand : Command("remove") {
    init {
        description("指定したGUIを削除します。自分以外のGUIを削除するには権限が必要です。")
    }
}