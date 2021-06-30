package kotx.customgui.gui

import com.mojang.blaze3d.matrix.*
import kotx.customgui.util.*
import net.minecraft.client.gui.screen.*
import net.minecraft.client.gui.widget.button.Button
import java.awt.*
import kotlin.math.*

open class GUI {
    private val handler = GUIHandler(this)
    protected val width
        get() = handler.width
    protected val height
        get() = handler.height

    open fun initialize() {

    }

    open fun draw(stack: MatrixStack, mouseX: Int, mouseY: Int) {

    }

    open fun onMouseDrag(button: MouseButton, mouseX: Int, mouseY: Int) {

    }

    open fun onMouseMove(mouseX: Int, mouseY: Int) {

    }

    open fun onMousePress(button: MouseButton, mouseX: Int, mouseY: Int) {

    }

    open fun onMouseRelease(button: MouseButton, mouseX: Int, mouseY: Int) {

    }

    open fun onKeyPress(key: Int, modifiers: Int) {

    }

    open fun onKeyRelease(key: Int, modifiers: Int) {

    }

    fun button(text: String, x: Int, y: Int, width: Int, height: Int, onClick: Button.() -> Unit = {}) {
        handler.button(text, x, y, width, height, onClick)
    }

    fun button(text: String, x: Int, y: Int, onClick: Button.() -> Unit = {}) {
        val width = fontRenderer.getStringWidth(text) + 50
        val height = fontRenderer.FONT_HEIGHT + 11

        handler.button(text, x, y, width, height, onClick)
    }

    companion object {
        fun rect(stack: MatrixStack, x1: Int, y1: Int, x2: Int, y2: Int, color: Color) {
            val minX = min(x1, x2)
            val maxX = max(x1, x2)
            val minY = min(y1, y2)
            val maxY = max(y1, y2)

            Screen.fill(stack, minX, minY, maxX, maxY, color.rgb)
        }

        fun rectCenter(stack: MatrixStack, x: Int, y: Int, width: Int, height: Int, color: Color) {
            rect(stack, x - width / 2, y - height / 2, x + width / 2, y + height / 2, color)
        }

        fun text(stack: MatrixStack, text: String, x: Int, y: Int, color: Color, shadow: Boolean = false) {
            if (shadow)
                fontRenderer.drawStringWithShadow(stack, text, x.toFloat(), y.toFloat(), color.rgb)
            else
                fontRenderer.drawString(stack, text, x.toFloat(), y.toFloat(), color.rgb)
        }

        fun textCenter(stack: MatrixStack, text: String, x: Int, y: Int, color: Color) {
            val width = fontRenderer.getStringWidth(text)
            val height = fontRenderer.FONT_HEIGHT
            text(stack, text, x - width / 2, y - height / 2, color)
        }

        fun textRight(stack: MatrixStack, text: String, x: Int, y: Int, color: Color) {
            val width = fontRenderer.getStringWidth(text)
            val height = fontRenderer.FONT_HEIGHT
            text(stack, text, x - width, y - height, color)
        }

        fun textShadow(stack: MatrixStack, text: String, x: Int, y: Int, color: Color) {
            text(stack, text, x, y, color, true)
        }

        fun textShadowCenter(stack: MatrixStack, text: String, x: Int, y: Int, color: Color) {
            val width = fontRenderer.getStringWidth(text)
            val height = fontRenderer.FONT_HEIGHT
            text(stack, text, x - width / 2, y - height / 2, color, true)
        }

        fun textShadowRight(stack: MatrixStack, text: String, x: Int, y: Int, color: Color) {
            val width = fontRenderer.getStringWidth(text)
            val height = fontRenderer.FONT_HEIGHT
            text(stack, text, x - width, y - height, color, true)
        }

        fun display(gui: GUI? = null) {
            mc.displayGuiScreen(gui?.handler)
        }
    }
}