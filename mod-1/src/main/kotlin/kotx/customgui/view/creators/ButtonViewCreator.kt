package kotx.customgui.view.creators

import kotx.customgui.view.ViewCreator
import kotx.customgui.view.ViewType
import kotx.customgui.view.holders.ButtonViewHolder
import kotx.customgui.view.views.ButtonView

class ButtonViewCreator : ViewCreator<ButtonView, ButtonViewHolder>() {
    override val type: ViewType = ViewType.BUTTON
    override val points: Int = 2
    override var initView: ButtonViewHolder? = null

    override fun initialize() {

    }
}