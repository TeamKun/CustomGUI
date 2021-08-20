package kotx.customgui

import kotx.customgui.gateway.GatewayClient
import kotx.customgui.gui.GUI
import kotx.customgui.gui.guis.EditorGUI
import kotx.customgui.util.MainThreadExecutor
import kotx.customgui.util.mc
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
import java.io.File

@Mod(CustomGUIMod.MOD_ID)
class CustomGUIMod {
    companion object {
        const val MOD_ID = "customgui"
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
        var isFlex = false
    }

    init {
        MinecraftForge.EVENT_BUS.register(this)

        ClientRegistry.registerKeyBinding(openEditorKeyBind)

        File("./mods/CustomGUI/caches/").listFiles().forEach { it.delete() }
    }

    @SubscribeEvent
    fun onKeyboard(event: InputEvent.KeyInputEvent) {
        if (openEditorKeyBind.isPressed) {
            EditorGUI.canLoad = true
            GUI.display(EditorGUI)
        }
    }

    @SubscribeEvent
    fun onRenderTick(event: TickEvent.RenderTickEvent) {
        MainThreadExecutor.consume()
    }

    @SubscribeEvent
    fun onRenderOverlay(event: RenderGameOverlayEvent.Post) {
        if (event.type != RenderGameOverlayEvent.ElementType.EXPERIENCE) return
        if (event.isCancelable) return
        if (mc.currentServerData == null || mc.currentServerData?.isOnLAN == true) return
        if (mc.currentScreen != null) return

        if (opacity <= 0.0) return

        val stack = event.matrixStack
        val width = event.window.scaledWidth
        val height = event.window.scaledHeight

        val xFactor = width.toDouble() / EditorGUI.editorWidth.toDouble()
        val yFactor = height.toDouble() / EditorGUI.editorHeight.toDouble()

        holders.sortedBy { it.index }.forEach {
            val renderer = it.content.renderer

            var x1 = it.content.x1
            var y1 = it.content.y1
            var x2 = it.content.x2
            var y2 = it.content.y2

            if (isFlex) {
                x1 = (x1 * xFactor).toInt()
                y1 = (y1 * yFactor).toInt()
                x2 = (x2 * xFactor).toInt()
                y2 = (y2 * yFactor).toInt()
            }

            x1 += width / 2
            y1 += height / 2
            x2 += width / 2
            y2 += height / 2

            when (it) {
                is TextViewHolder -> (renderer as TextViewRenderer).renderFull(
                    stack,
                    x1,
                    y1,
                    x2,
                    y2,
                    opacity,
                    it.content
                )
                is RectViewHolder -> (renderer as RectViewRenderer).renderFull(
                    stack,
                    x1,
                    y1,
                    x2,
                    y2,
                    opacity,
                    it.content
                )
                is ButtonViewHolder -> (renderer as ButtonViewRenderer).renderFull(
                    stack,
                    x1,
                    y1,
                    x2,
                    y2,
                    opacity,
                    it.content
                )
                is ImageViewHolder -> (renderer as ImageViewRenderer).renderFull(
                    stack,
                    x1,
                    y1,
                    x2,
                    y2,
                    opacity,
                    it.content
                )
            }
        }
    }
}