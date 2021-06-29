package kotx.customgui.gui.guis

import com.mojang.blaze3d.matrix.*
import kotx.customgui.*
import kotx.customgui.gateway.*
import kotx.customgui.gui.*
import kotx.customgui.util.*
import kotx.customgui.view.*
import kotx.customgui.view.holder.*
import kotx.customgui.view.holder.holders.*
import kotx.customgui.view.holder.parsers.*

object EditorGUI : GUI() {
    val holders = mutableListOf<ViewHolder<out View>>()

    override fun draw(stack: MatrixStack, mouseX: Int, mouseY: Int) {
        holders.sortedByDescending { it.index }.forEach {
            it.content.drawPreview(stack)
        }
    }

    fun save() {
        val guis = holders.mapNotNull { holder ->
            val parser = CustomGUIMod.getParser(holder.type.value)

            when (holder.type) {
                ViewType.TEXT -> (parser as TextViewHolderParser).encode(holder as TextViewHolder)
                ViewType.RECT -> (parser as RectViewHolderParser).encode(holder as RectViewHolder)
                ViewType.BUTTON -> (parser as TextViewHolderParser).encode(holder as TextViewHolder)
                ViewType.IMAGE -> (parser as TextViewHolderParser).encode(holder as TextViewHolder)
                else -> null
            }
        }

        CustomGUIMod.gatewayClient.send(OpCode.SAVE_GUI, json { "guis" to guis })
    }
}