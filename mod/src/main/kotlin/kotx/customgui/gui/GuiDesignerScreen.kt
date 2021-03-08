package kotx.customgui.gui

import kotx.customgui.*
import kotx.customgui.gui.component.components.ButtonComponent
import kotx.customgui.gui.component.components.ImageComponent
import kotx.customgui.gui.component.components.RectComponent
import kotx.customgui.gui.component.components.TextComponent
import kotx.ktools.toJson
import net.minecraft.client.gui.screen.Screen
import net.minecraft.util.text.StringTextComponent
import net.minecraftforge.fml.client.gui.widget.ExtendedButton
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.lwjgl.glfw.GLFW
import java.awt.Color
import kotlin.math.max
import kotlin.math.min
import kotlin.math.round

object GuiDesignerScreen : Screen(StringTextComponent("GUI Designer")), KoinComponent {
    private val mod by inject<CustomGUIMod>()
    private val components = mutableListOf(
        ButtonComponent,
        ImageComponent,
        RectComponent,
        TextComponent
    )
    var views = mutableListOf<View>()

    const val guiWidth = 320
    const val guiHeight = 180
    var editModeIndex = -1
    private var selectedView = -1
    private var movingView = -1
    private var editMode = true


    override fun init() {
        addButton(ExtendedButton(xCenter - 100, scaledHeight - 70, 80, 20, "閉じる") {
            onClose()
            minecraft?.displayGuiScreen(null)
        })
        addButton(ExtendedButton(xCenter + 20, scaledHeight - 70, 80, 20, "編集モード") {
            editMode = !editMode
            it.message = if (editMode) "編集モード" else "プレビューモード"
        })

        editModeIndex = -1

        val displayMargin = 50
        val buttonMargin = 20
        val w = scaledWidth - displayMargin * 2
        val buttonWidth = w / components.size
        components.forEachIndexed { i, component ->
            val x = buttonWidth * i + displayMargin
            addButton(ExtendedButton(x + buttonMargin, 30, buttonWidth - (buttonMargin * 2), 20, component.text) {
                component.init()
                editModeIndex = i
            })
        }

        super.init()
    }

    override fun render(mouseX: Int, mouseY: Int, partialTicks: Float) {
        fillAbsolute(0, 0, scaledWidth, scaledHeight, Color(0, 0, 0, 100))
        fillAbsolute(xCenter - guiWidth / 2, yCenter - guiHeight / 2, xCenter + guiWidth / 2, yCenter + guiHeight / 2, Color(12, 12, 12, 255))

        if (editMode) {
            components.getOrNull(editModeIndex)?.render(mouseX, mouseY, scaledWidth, scaledHeight)
            views.forEachIndexed { i, it ->
                it.renderPreview(mouseX, mouseY)
                val startX = xCenter + it.startX
                val endX = xCenter + it.endX
                val startY = yCenter + it.startY
                val endY = yCenter + it.endY

                when {
                    i == movingView -> {
                        fillAbsolute(startX, startY, endX, startY + 1, Color(120, 0, 0))
                        fillAbsolute(startX, startY, startX + 1, endY, Color(120, 0, 0))
                        fillAbsolute(endX - 1, startY, endX, endY, Color(120, 0, 0))
                        fillAbsolute(startX, endY - 1, endX, endY, Color(120, 0, 0))

                        fillAbsolute(startX - 1, startY - 1, startX + 2, startY + 2, Color.RED)
                        fillAbsolute(endX - 1, startY - 1, endX + 2, startY + 2, Color.RED)
                        fillAbsolute(startX - 1, endY - 1, startX + 2, endY + 2, Color.RED)
                        fillAbsolute(endX - 1, endY - 1, endX + 2, endY + 2, Color.RED)
                    }
                    i == selectedView -> {
                        fillAbsolute(startX, startY, endX, startY + 1, Color.GRAY)
                        fillAbsolute(startX, startY, startX + 1, endY, Color.GRAY)
                        fillAbsolute(endX - 1, startY, endX, endY, Color.GRAY)
                        fillAbsolute(startX, endY - 1, endX, endY, Color.GRAY)

                        fillAbsolute(startX - 1, startY - 1, startX + 2, startY + 2, Color.WHITE)
                        fillAbsolute(endX - 1, startY - 1, endX + 2, startY + 2, Color.WHITE)
                        fillAbsolute(startX - 1, endY - 1, startX + 2, endY + 2, Color.WHITE)
                        fillAbsolute(endX - 1, endY - 1, endX + 2, endY + 2, Color.WHITE)
                    }
                    mouseX in startX..endX && mouseY in startY..endY -> {
                        fillAbsolute(startX, startY, endX, startY + 1, Color.DARK_GRAY)
                        fillAbsolute(startX, startY, startX + 1, endY, Color.DARK_GRAY)
                        fillAbsolute(endX - 1, startY, endX, endY, Color.DARK_GRAY)
                        fillAbsolute(startX, endY - 1, endX, endY, Color.DARK_GRAY)

                        fillAbsolute(startX - 1, startY - 1, startX + 2, startY + 2, Color.GRAY)
                        fillAbsolute(endX - 1, startY - 1, endX + 2, startY + 2, Color.GRAY)
                        fillAbsolute(startX - 1, endY - 1, startX + 2, endY + 2, Color.GRAY)
                        fillAbsolute(endX - 1, endY - 1, endX + 2, endY + 2, Color.GRAY)
                    }
                }
            }
        } else views.forEach { it.renderPage(1f, 1f, 1f) }

        super.render(mouseX, mouseY, partialTicks)
    }

    override fun mouseMoved(xPos: Double, mouseY: Double) {
        val x = xPos.toInt()
        val y = mouseY.toInt()

        if (isInRange(x, y) && editMode)
            components.getOrNull(editModeIndex)?.onMouseMove(xPos.toInt(), mouseY.toInt())
    }

    override fun mouseClicked(p_mouseClicked_1_: Double, p_mouseClicked_3_: Double, p_mouseClicked_5_: Int): Boolean {
        val x = p_mouseClicked_1_.toInt()
        val y = p_mouseClicked_3_.toInt()

        if (isInRange(x, y) && editMode)
            components.getOrNull(editModeIndex)?.onMouseClick(p_mouseClicked_1_.toInt(), p_mouseClicked_3_.toInt(), p_mouseClicked_5_)

        val i = views.indexOfFirst {
            x in xCenter + it.startX..xCenter + it.endX && y in yCenter + it.startY..yCenter + it.endY
        }

        selectedView = i
        movingView = i

        if (!editMode) {
            views.getOrNull(selectedView)?.onClick(x, y, p_mouseClicked_5_)
        }

        return super.mouseClicked(p_mouseClicked_1_, p_mouseClicked_3_, p_mouseClicked_5_)
    }

    override fun mouseReleased(mouseX: Double, mouseY: Double, button: Int): Boolean {
        movingView = -1
        return super.mouseReleased(mouseX, mouseY, button)
    }

    override fun mouseDragged(mouseX: Double, mouseY: Double, button: Int, dragX: Double, dragY: Double): Boolean {
        if (editMode)
            views.getOrNull(movingView)?.also {
                val w = it.endX - it.startX
                val h = it.endY - it.startY
                it.startX = max(-guiWidth / 2, min(guiWidth / 2 - it.width, it.startX + round(dragX).toInt()))
                it.startY = max(-guiHeight / 2, min(guiHeight / 2 - it.height, it.startY + round(dragY).toInt()))
                it.endX = it.startX + w
                it.endY = it.startY + h
            }

        return super.mouseDragged(mouseX, mouseY, button, dragX, dragY)
    }

    fun isInRange(x: Int, y: Int) = x in xCenter - (guiWidth / 2)..xCenter + (guiWidth / 2) && y in yCenter - (guiHeight / 2)..yCenter + (guiHeight / 2)

    override fun keyPressed(p_keyPressed_1_: Int, p_keyPressed_2_: Int, p_keyPressed_3_: Int): Boolean {
        if (p_keyPressed_1_ == GLFW.GLFW_KEY_DELETE) {
            if (views.getOrNull(selectedView) != null)
                views.removeAt(selectedView)
            selectedView = -1

            return false
        }
        if (p_keyPressed_1_ == GLFW.GLFW_KEY_ESCAPE) {
            if (editModeIndex == -1)
                minecraft?.displayGuiScreen(null)?.also {
                    onClose()
                }
            else
                editModeIndex = -1

            return false
        }

        if (editMode)
            components.getOrNull(editModeIndex)?.onKeyPress(p_keyPressed_1_, p_keyPressed_2_, p_keyPressed_3_)
        return super.keyPressed(p_keyPressed_1_, p_keyPressed_2_, p_keyPressed_3_)
    }

    override fun onClose() {
        saveWorkspace()
        super.onClose()
    }

    private fun saveWorkspace() {
        val json = object {
            val op = 1
            val data = views.map { it.parseToJson() }.filterNot { it.isBlank() }
        }.toJson()
        mod.channel.sendToServer(json)
    }

    override fun isPauseScreen(): Boolean = false
}