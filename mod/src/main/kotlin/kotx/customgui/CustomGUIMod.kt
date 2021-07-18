package kotx.customgui

import kotx.customgui.gateway.GatewayClient
import kotx.customgui.gui.GUI
import kotx.customgui.gui.guis.editor.EditorGUI
import kotx.customgui.util.MainThreadExecutor
import kotx.customgui.view.ViewHandler
import net.minecraft.client.settings.KeyBinding
import net.minecraft.client.util.InputMappings
import net.minecraftforge.client.event.InputEvent
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
    fun onRender(event: TickEvent.RenderTickEvent) {
        MainThreadExecutor.consume()
    }
}