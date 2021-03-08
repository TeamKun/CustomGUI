package kotx.customgui.gui

import kotx.customgui.ServerThreadExecutor
import kotx.customgui.gui.component.view.ImageView
import kotx.customgui.scaledHeight
import kotx.customgui.scaledWidth
import net.minecraft.client.gui.AbstractGui
import net.minecraftforge.client.event.RenderGameOverlayEvent
import net.minecraftforge.eventbus.api.SubscribeEvent
import java.util.concurrent.CompletableFuture
import java.util.concurrent.Executor
import kotlin.math.max
import kotlin.math.min

object GuiOverlay : AbstractGui() {
    var opacity = 1.0
        set(value) {
            field = max(0.0, min(1.0, value))
        }
    var views = mutableListOf<View>()

    @SubscribeEvent
    fun onRender(event: RenderGameOverlayEvent) {
        ServerThreadExecutor.executeQueuedTaskImmediately()

        val scaleW = scaledWidth.toFloat() / GuiDesignerScreen.guiWidth
        val scaleH = scaledHeight.toFloat() / GuiDesignerScreen.guiHeight

        views.forEach {
            it.renderPage(scaleW, scaleH, opacity.toFloat())
        }
    }
}