package kotx.customgui.view.views

import kotx.customgui.util.*
import kotx.customgui.view.*
import kotx.customgui.view.renderers.*

class ButtonView(
    val command: String,
    override val x1: Int,
    override val y1: Int,
    override val x2: Int,
    override val y2: Int,
) : View {
    override val renderer = ButtonViewRenderer()

    override fun onClick() {
        val cmd = command.replaceFirst("^/".toRegex(), "")
        mc.player?.sendChatMessage("/$cmd")
    }
}