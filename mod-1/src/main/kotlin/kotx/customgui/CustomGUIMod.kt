package kotx.customgui

import kotx.customgui.gateway.*
import net.minecraft.client.settings.*
import net.minecraft.client.util.*
import net.minecraftforge.client.event.*
import net.minecraftforge.client.settings.*
import net.minecraftforge.common.*
import net.minecraftforge.eventbus.api.*
import net.minecraftforge.fml.client.registry.*
import net.minecraftforge.fml.common.*
import org.lwjgl.glfw.*
import org.slf4j.*

@Mod(CustomGUIMod.MOD_ID)
class CustomGUIMod {
    companion object {
        const val MOD_ID = "customgui"
        val LOGGER = LoggerFactory.getLogger("CustomGUI")!!
        val openEditorKeyBind = KeyBinding(
            "Open CustomGUI Editor",
            KeyConflictContext.IN_GAME,
            KeyModifier.CONTROL,
            InputMappings.getInputByCode(GLFW.GLFW_KEY_P, GLFW.glfwGetKeyScancode(GLFW.GLFW_KEY_P)),
            MOD_ID
        )
        val gatewayClient = GatewayClient()
    }

    init {
        MinecraftForge.EVENT_BUS.register(this)

        ClientRegistry.registerKeyBinding(openEditorKeyBind)

        gatewayClient.connect()
    }

    @SubscribeEvent
    fun onKeyboard(event: InputEvent.KeyInputEvent) {

    }
}