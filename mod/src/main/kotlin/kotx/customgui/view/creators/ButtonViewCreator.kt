package kotx.customgui.view.creators

import com.mojang.blaze3d.matrix.MatrixStack
import kotx.customgui.gui.guis.EditorGUI

import kotx.customgui.util.fontRenderer
import kotx.customgui.view.ViewCreator
import kotx.customgui.view.ViewType
import kotx.customgui.view.holders.ButtonViewHolder
import kotx.customgui.view.views.ButtonView
import net.minecraft.client.gui.widget.TextFieldWidget
import org.lwjgl.glfw.GLFW
import java.awt.Color

class ButtonViewCreator : ViewCreator<ButtonView, ButtonViewHolder>() {
    override val type: ViewType = ViewType.BUTTON
    override val points: Int = 2

    override var initView: ButtonViewHolder? = null
    private lateinit var textField: TextFieldWidget

    override fun initialize() {
        textField = textFieldCenter("コマンド", width / 2, 70, 200, fontRenderer.FONT_HEIGHT + 11).apply {
            setCanLoseFocus(true)
            setMaxStringLength(1024)
        }

        if (initView != null)
            textField.text = initView!!.content.command

        val button = buttonCenter("作成", width / 2, 110) {
            handle()
        }

        textField.setResponder {
            button.active = it.isNotBlank()
        }

        button.active = false

        handler.setFocusedDefault(textField)
    }

    override fun onKeyPress(key: Int, modifiers: Int): Boolean {
        if (key == GLFW.GLFW_KEY_ENTER && textField.text.isNotBlank())
            handle()

        return super.onKeyPress(key, modifiers)
    }

    private fun handle() {
        if (initView != null) EditorGUI.holders.removeIf { it.index == initView!!.index }
        build(ButtonView(textField.text))
    }

    override fun draw(stack: MatrixStack, mouseX: Int, mouseY: Int) {
        rect(stack, 0, 0, width, height, Color(0, 0, 0, 100))
        textCenter(stack, "送信するメッセージ・コマンドを入力", width / 2, 30, Color.WHITE)
    }
}