package kotx.customgui

import net.minecraft.client.Minecraft
import net.minecraft.client.gui.AbstractGui
import net.minecraft.client.gui.FontRenderer
import java.awt.Color

fun FontRenderer.drawStringCentered(text: String, x: Int, y: Int, color: Color, shadow: Boolean = false) {
    val w = getStringWidth(text)
    val h = getWordWrappedHeight(text, Int.MAX_VALUE)
    val xPos = (x - w / 2).toFloat()
    val yPos = (y - h / 2).toFloat()

    if (shadow)
        drawStringWithShadow(text, xPos, yPos, color.rgb)
    else
        drawString(text, xPos, yPos, color.rgb)
}

fun FontRenderer.drawString(text: String, x: Int, y: Int, color: Color, shadow: Boolean = false) {
    if (shadow)
        drawStringWithShadow(text, x.toFloat(), y.toFloat(), color.rgb)
    else
        drawString(text, x.toFloat(), y.toFloat(), color.rgb)
}

fun fill(x: Int, y: Int, width: Int, height: Int, color: Color) {
    AbstractGui.fill(x, y, x + width, y + height, color.rgb)
}

fun fillCentered(x: Int, y: Int, width: Int, height: Int, color: Color) {
    val w = width / 2
    val h = height / 2
    AbstractGui.fill(x - w, y - h, x + w, y + h, color.rgb)
}

fun fillAbsolute(x1: Int, y1: Int, x2: Int, y2: Int, color: Color) {
    AbstractGui.fill(x1, y1, x2, y2, color.rgb)
}

val scaledWidth: Int
    get() = Minecraft.getInstance().mainWindow.scaledWidth

val scaledHeight: Int
    get() = Minecraft.getInstance().mainWindow.scaledHeight

val xCenter: Int
    get() = scaledWidth / 2

val yCenter: Int
    get() = scaledHeight / 2