package kotx.customgui

import kotx.customgui.gateway.GatewayClient
import kotx.customgui.gui.GUI
import kotx.customgui.gui.guis.EditorGUI
import kotx.customgui.util.MainThreadExecutor
import kotx.customgui.view.ViewHandler
import kotx.customgui.view.ViewHolder
import kotx.customgui.view.holders.ButtonViewHolder
import kotx.customgui.view.holders.ImageViewHolder
import kotx.customgui.view.holders.RectViewHolder
import kotx.customgui.view.holders.TextViewHolder
import kotx.customgui.view.renderers.ButtonViewRenderer
import kotx.customgui.view.renderers.ImageViewRenderer
import kotx.customgui.view.renderers.RectViewRenderer
import kotx.customgui.view.renderers.TextViewRenderer
import net.minecraft.client.settings.KeyBinding
import net.minecraft.client.util.InputMappings
import net.minecraftforge.client.event.InputEvent
import net.minecraftforge.client.event.RenderGameOverlayEvent
import net.minecraftforge.client.settings.KeyConflictContext
import net.minecraftforge.client.settings.KeyModifier
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.event.TickEvent
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.client.registry.ClientRegistry
import net.minecraftforge.fml.common.Mod
import org.lwjgl.glfw.GLFW
import org.slf4j.LoggerFactory

@Mod(CustomGUIMod.MOD_ID)
class CustomGUIMod {
    companion object {
        const val MOD_ID = "customgui"
        val LOGGER = LoggerFactory.getLogger("CustomGUI")!!
        val openEditorKeyBind = KeyBinding(
            "Open CustomGUI Editor",
            KeyConflictContext.IN_GAME,
            KeyModifier.CONTROL,
            InputMappings.getInputByCode(GLFW.GLFW_KEY_O, GLFW.glfwGetKeyScancode(GLFW.GLFW_KEY_O)),
            MOD_ID
        )
        val gatewayClient = GatewayClient()
        val viewHandler = ViewHandler()
        val holders = mutableListOf<ViewHolder>()
        var opacity = 0.0
    }

    init {
        MinecraftForge.EVENT_BUS.register(this)

        ClientRegistry.registerKeyBinding(openEditorKeyBind)
    }

    @SubscribeEvent
    fun onKeyboard(event: InputEvent.KeyInputEvent) {
        if (openEditorKeyBind.isPressed)
            GUI.display(EditorGUI)
    }

    @SubscribeEvent
    fun onRenderTick(event: TickEvent.RenderTickEvent) {
        MainThreadExecutor.consume()
    }

    @SubscribeEvent
    fun onRenderOverlay(event: RenderGameOverlayEvent) {
        if (opacity <= 0.0) return

        val stack = event.matrixStack
        val width = event.window.scaledWidth
        val height = event.window.scaledHeight
        holders.sortedBy { it.index }.forEach {
            val renderer = it.content.renderer

            val x1 = width / 2 + it.content.x1
            val y1 = height / 2 + it.content.y1
            val x2 = width / 2 + it.content.x2
            val y2 = height / 2 + it.content.y2

            when (it) {
                is TextViewHolder -> (renderer as TextViewRenderer).renderFull(stack, x1, y1, x2, y2, it.content)
                is RectViewHolder -> (renderer as RectViewRenderer).renderFull(stack, x1, y1, x2, y2, it.content)
                is ButtonViewHolder -> (renderer as ButtonViewRenderer).renderFull(stack, x1, y1, x2, y2, it.content)
                is ImageViewHolder -> (renderer as ImageViewRenderer).renderFull(stack, x1, y1, x2, y2, it.content)
            }
        }
    }
}