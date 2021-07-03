package kotx.customgui.gui.guis

import com.mojang.blaze3d.matrix.*
import kotx.customgui.gui.*
import kotx.customgui.view.*
import kotx.customgui.view.creators.*
import kotx.customgui.view.holders.*
import kotx.customgui.view.renderers.*
import java.awt.*

object EditorGUI : GUI() {
    val holders = mutableListOf<ViewHolder>()
    val creators = listOf<ViewCreator<out View>>(
        TextViewCreator()
    )

    private const val editorWidth = 360
    private const val editorHeight = 203

    private var selectingCreator: Int? = null

    //0 for first point
    //1 for second point
    private var creatorStep: Int = 0
    private var lastLocation: Pair<Int, Int>? = null

    override fun initialize() {
        val w = width / (creators.size + 1)
        creators.forEachIndexed { i, creator ->
            val x = w * (i + 1)
            buttonCenter(
                creator.type.capitalizedName(),
                x,
                50
            ) {
                selectingCreator = i
                creatorStep = 0
            }
        }
    }

    override fun draw(stack: MatrixStack, mouseX: Int, mouseY: Int) {
        //Dimmed background
        rect(stack, 0, 0, width, height, Color(0, 0, 0, 100))
        //Editor background
        rectCenter(stack, width / 2, height / 2, editorWidth, editorHeight, Color(12, 12, 12))

        //info
        if (selectingCreator != null) {
            val creator = creators[selectingCreator!!]
            val text = when {
                creator.points == 1 && creatorStep == 0 -> "${creator.type.capitalizedName()}を置く場所をクリックしてください。"
                creator.points == 2 && creatorStep == 0 -> "${creator.type.capitalizedName()}の始点をクリックしてください。"
                creator.points == 2 && creatorStep == 1 -> "${creator.type.capitalizedName()}の終点をクリックしてください。"
                else -> ""
            }

            textCenter(stack, text, width / 2, 50, Color.WHITE)

            if (creatorStep == 0)
                rect(stack, mouseX - 2, mouseY - 2, mouseX + 2, mouseY + 2, Color.GREEN)
            else {
                rect(stack, lastLocation!!.first, lastLocation!!.second, mouseX, mouseY, Color.YELLOW)
                rect(stack, mouseX - 2, mouseY - 2, mouseX + 2, mouseY + 2, Color.RED)
            }
        }

        //components
        holders.sortedBy { it.index }.forEach {
            val renderer = it.content.renderer
            when (it) {
                is TextViewHolder -> (renderer as TextViewRenderer).renderPreview(stack, it.content.x1, it.content.y1, it.content.x2, it.content.y2, it.content)
                is RectViewHolder -> (renderer as RectViewRenderer).renderPreview(stack, it.content.x1, it.content.y1, it.content.x2, it.content.y2, it.content)
                is ButtonViewHolder -> (renderer as ButtonViewRenderer).renderPreview(stack, it.content.x1, it.content.y1, it.content.x2, it.content.y2, it.content)
                is ImageViewHolder -> (renderer as ImageViewRenderer).renderPreview(stack, it.content.x1, it.content.y1, it.content.x2, it.content.y2, it.content)
            }
        }
    }

    override fun onMouseClick(button: MouseButton, mouseX: Int, mouseY: Int) {
        if (!isInEditor(mouseX, mouseY)) return

        if (selectingCreator != null) {
            val creator = creators[selectingCreator!!]
            when {
                creator.points == 1 -> {
                    display(creator)
                    creatorStep = 0
                }
                creatorStep >= creator.points -> {
                    display(creator)
                    creatorStep = 0
                }
                else -> {
                    lastLocation = mouseX to mouseY
                    creatorStep++
                }
            }
        }
    }

    private fun isInEditor(mouseX: Int, mouseY: Int) =
        mouseX in (width - editorWidth / 2)..(width + editorWidth / 2)
                && mouseY in (height - editorHeight / 2)..(height + editorHeight / 2)
}