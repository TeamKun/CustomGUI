package kotx.customgui.view.creators

import com.mojang.blaze3d.matrix.MatrixStack
import kotx.customgui.gui.guis.editor.EditorGUI
import kotx.customgui.util.fontRenderer
import kotx.customgui.view.ViewCreator
import kotx.customgui.view.ViewType
import kotx.customgui.view.holders.TextViewHolder
import kotx.customgui.view.views.TextView
import net.minecraft.client.gui.widget.TextFieldWidget
import org.lwjgl.glfw.GLFW
import java.awt.Color

class TextViewCreator : ViewCreator<TextView, TextViewHolder>() {
    override val type: ViewType = ViewType.TEXT
    override val points: Int = 1

    override var initView: TextViewHolder? = null
    private lateinit var textField: TextFieldWidget

    override fun initialize() {
        textField = textFieldCenter("テキスト", width / 2, 50, 200, fontRenderer.FONT_HEIGHT + 11).apply {
            setCanLoseFocus(true)
            setMaxStringLength(1024)
        }

        if (initView != null)
            textField.text = initView!!.content.text

        val button = buttonCenter("作成", width / 2, 100) {
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

        return true
    }

    private fun handle() {
        if (initView != null) EditorGUI.holders.removeIf { it.index == initView!!.index }
        build(TextView(textField.text, Color.WHITE).apply {
            this@TextViewCreator.x2 = this@TextViewCreator.x1 + fontRenderer.getStringWidth(textField.text)
            this@TextViewCreator.y2 = this@TextViewCreator.y1 + fontRenderer.FONT_HEIGHT
        })
    }

    override fun draw(stack: MatrixStack, mouseX: Int, mouseY: Int) {
        rect(stack, 0, 0, width, height, Color(0, 0, 0, 100))
    }
}