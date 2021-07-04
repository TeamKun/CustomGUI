package kotx.customgui.view.views

import kotx.customgui.util.*
import kotx.customgui.view.*
import kotx.customgui.view.renderers.*

class ButtonView(
    val command: String,
) : View {
    override val renderer = ButtonViewRenderer()

    override var x1: Int = 0
    override var y1: Int = 0
    override var x2: Int = 0
    override var y2: Int = 0

    override fun onClick() {
        val cmd = command.replaceFirst("^/".toRegex(), "")
        mc.player?.sendChatMessage("/$cmd")
    }
}