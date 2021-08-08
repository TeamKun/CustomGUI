package dev.kotx.customgui.commands

import dev.kotx.flylib.command.Command
import org.koin.core.component.KoinComponent

class ShowCommand : Command("show"), KoinComponent {
    init {
        description("指定したGUIを指定したプレイヤーにクリック可能なGUI形式で表示します。自分以外に表示させるには権限が必要です。")
    }


}