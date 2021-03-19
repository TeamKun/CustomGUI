package kotx.customgui.gui

import com.mojang.blaze3d.systems.RenderSystem
import kotx.customgui.ServerThreadExecutor
import kotx.customgui.fillAbsolute
import kotx.customgui.scaledHeight
import kotx.customgui.scaledWidth
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.AbstractGui
import net.minecraftforge.client.event.RenderGameOverlayEvent
import net.minecraftforge.event.TickEvent
import net.minecraftforge.eventbus.api.SubscribeEvent
import java.awt.Color
import kotlin.math.max
import kotlin.math.min

object GuiOverlay : AbstractGui() {
    var opacity = 1.0
        set(value) {
            field = max(0.0, min(1.0, value))
        }
    var views = mutableListOf<View>()

    @SubscribeEvent
    fun onRender(event: TickEvent.RenderTickEvent) {
        if (Minecraft.getInstance().currentServerData == null || Minecraft.getInstance().currentServerData?.isOnLAN == true) return
        if (Minecraft.getInstance().currentScreen != null) return
        ServerThreadExecutor.executeQueuedTaskImmediately()

        val scaleW = scaledWidth.toFloat() / GuiDesignerScreen.guiWidth
        val scaleH = scaledHeight.toFloat() / GuiDesignerScreen.guiHeight

        views.forEach {
            it.renderPage(scaleW, scaleH, opacity.toFloat())
        }
    }
}