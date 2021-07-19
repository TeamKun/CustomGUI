package dev.kotx.customgui.commands

import dev.kotx.flylib.command.Command

class CopyCommand : Command("copy") {
    init {
        description("指定したGUIを自分の物として別の名前でコピーします。")
    }
}