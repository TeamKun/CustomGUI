package dev.kotx.customgui.commands

import dev.kotx.flylib.command.Command

class UseCommand : Command("use") {
    init {
        description("指定したGUIを自分の作業スペースに読み込みます。誰のGUIで合っても読み込むことが出来ます。")
    }
}