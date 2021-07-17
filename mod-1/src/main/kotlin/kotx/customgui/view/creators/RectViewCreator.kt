package kotx.customgui.view.creators

import com.mojang.blaze3d.matrix.MatrixStack
import kotx.customgui.gui.guis.editor.EditorGUI
import kotx.customgui.util.component
import kotx.customgui.view.ViewCreator
import kotx.customgui.view.ViewType
import kotx.customgui.view.holders.RectViewHolder
import kotx.customgui.view.views.RectView
import net.minecraft.client.gui.widget.AbstractSlider
import net.minecraft.client.gui.widget.button.Button
import org.lwjgl.glfw.GLFW
import java.awt.Color

class RectViewCreator : ViewCreator<RectView, RectViewHolder>() {
    override val type: ViewType = ViewType.RECT
    override val points: Int = 2

    override var initView: RectViewHolder? = null

    private lateinit var button: Button

    private var red = 1.0
    private var green = 1.0
    private var blue = 1.0
    private var alpha = 1.0

    private lateinit var redSlider: AbstractSlider
    private lateinit var greenSlider: AbstractSlider
    private lateinit var blueSlider: AbstractSlider
    private lateinit var alphaSlider: AbstractSlider

    override fun initialize() {
        if (initView != null) {
            red = initView!!.content.color.red.toDouble() / 255
            green = initView!!.content.color.green.toDouble() / 255
            blue = initView!!.content.color.blue.toDouble() / 255
            alpha = initView!!.content.color.alpha.toDouble() / 255
        } else {
            red = 1.0
            green = 1.0
            blue = 1.0
            alpha = 1.0
        }

        redSlider = handler.widget(object :
            AbstractSlider(width / 2 - 100, 50, 200, 20, "赤 ${(red * 255).toInt()}".component(), red) {
            override fun func_230979_b_() {
                red = sliderValue
            }

            override fun func_230972_a_() {
                message = "赤 ${(sliderValue * 255).toInt()}".component()
            }
        }) as AbstractSlider

        greenSlider = handler.widget(object :
            AbstractSlider(width / 2 - 100, 80, 200, 20, "緑 ${(green * 255).toInt()}".component(), green) {
            override fun func_230979_b_() {
                green = sliderValue
            }

            override fun func_230972_a_() {
                message = "緑 ${(sliderValue * 255).toInt()}".component()
            }
        }) as AbstractSlider

        blueSlider = handler.widget(object :
            AbstractSlider(width / 2 - 100, 110, 200, 20, "青 ${(blue * 255).toInt()}".component(), blue) {
            override fun func_230979_b_() {
                blue = sliderValue
            }

            override fun func_230972_a_() {
                message = "青 ${(sliderValue * 255).toInt()}".component()
            }
        }) as AbstractSlider

        alphaSlider = handler.widget(object :
            AbstractSlider(width / 2 - 100, 140, 200, 20, "不透明度 ${(alpha * 255).toInt()}".component(), alpha) {
            override fun func_230979_b_() {
                this@RectViewCreator.alpha = sliderValue
            }

            override fun func_230972_a_() {
                message = "不透明度 ${(sliderValue * 255).toInt()}".component()
            }
        }) as AbstractSlider

        button = buttonCenter("作成", width / 2, 190) {
            handle()
        }
    }

    override fun onKeyPress(key: Int, modifiers: Int): Boolean {
        if (key == GLFW.GLFW_KEY_ENTER)
            handle()

        return super.onKeyPress(key, modifiers)
    }

    private fun handle() {
        if (initView != null) EditorGUI.holders.removeIf { it.index == initView!!.index }
        build(
            RectView(
                Color(
                    (red * 255).toInt(), (green * 255).toInt(), (blue * 255).toInt(), (alpha * 255).toInt()
                )
            )
        )
    }

    override fun draw(stack: MatrixStack, mouseX: Int, mouseY: Int) {
        rect(stack, 0, 0, width, height, Color(0, 0, 0, 100))
        textCenter(stack, "色を指定", width / 2, 30, Color.WHITE)
        rectCenter(
            stack, width / 2, 190, 200, 30, Color(
                (red * 255).toInt(), (green * 255).toInt(), (blue * 255).toInt(), (alpha * 255).toInt()
            )
        )
    }
}