package kotx.customgui.view.views

import kotx.customgui.util.mc
import kotx.customgui.view.View
import kotx.customgui.view.renderers.ButtonViewRenderer

class ButtonView(
    val command: String,
) : View {
    override val renderer = ButtonViewRenderer()

    override var x1: Int = 0
    override var y1: Int = 0
    override var x2: Int = 0
    override var y2: Int = 0

    override fun copy(): View = ButtonView(command).also {
        it.x1 = x1
        it.y1 = y1
        it.x2 = x2
        it.y2 = y2
    }

    override fun onClick() {
        val cmd = command.replaceFirst("^/".toRegex(), "")
        mc.player?.sendChatMessage("/$cmd")
    }
}