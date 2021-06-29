package kotx.customgui.gui

import com.mojang.blaze3d.matrix.*
import net.minecraft.client.*
import net.minecraft.client.gui.screen.*
import java.awt.*
import kotlin.math.*

open class GUI {
    private val handler = GUIHandler(this)

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

    companion object {
        fun rect(stack: MatrixStack, x1: Int, y1: Int, x2: Int, y2: Int, color: Color) {
            val minX = min(x1, x2)
            val maxX = min(x1, x2)
            val minY = min(y1, y2)
            val maxY = min(y1, y2)

            Screen.fill(stack, minX, minY, maxX, maxY, color.rgb)
        }

        fun text(stack: MatrixStack, text: String, x: Int, y: Int, color: Color) {
            Minecraft.getInstance().fontRenderer.drawString(stack, text, x.toFloat(), y.toFloat(), color.rgb)
        }

        fun display(gui: GUI? = null) {
            Minecraft.getInstance().displayGuiScreen(gui?.handler)
        }
    }
}