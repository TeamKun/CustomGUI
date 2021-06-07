@file:Suppress("INACCESSIBLE_TYPE")

package kotx.customgui

import kotlinx.serialization.json.*
import kotx.customgui.gui.*
import kotx.customgui.gui.component.view.*
import kotx.ktools.*
import net.minecraft.client.*
import net.minecraft.client.settings.*
import net.minecraft.client.util.*
import net.minecraft.util.*
import net.minecraftforge.client.event.*
import net.minecraftforge.client.settings.*
import net.minecraftforge.common.*
import net.minecraftforge.eventbus.api.*
import net.minecraftforge.fml.client.registry.*
import net.minecraftforge.fml.common.*
import net.minecraftforge.fml.network.*
import net.minecraftforge.fml.network.simple.*
import org.apache.commons.lang3.*
import org.apache.commons.lang3.StringUtils
import org.koin.core.context.*
import org.koin.dsl.*
import org.lwjgl.glfw.*
import java.nio.charset.*
import java.util.*
import java.util.Timer
import kotlin.concurrent.*

@Mod(CustomGUIMod.modId)
class CustomGUIMod {
    companion object {
        const val modId = "customgui"
        const val modName = "customgui"
        val keyBinding = KeyBinding(
            "Open Custom GUI Editor",
            KeyConflictContext.IN_GAME,
            KeyModifier.CONTROL,
            InputMappings.getInputByCode(GLFW.GLFW_KEY_P, GLFW.glfwGetKeyScancode(GLFW.GLFW_KEY_P)),
            "customgui"
        )
    }

    val channel: SimpleChannel = NetworkRegistry.ChannelBuilder.named(ResourceLocation("customgui", "workspace"))
        .clientAcceptedVersions(NetworkRegistry.ACCEPTVANILLA::equals)
        .serverAcceptedVersions(NetworkRegistry.ACCEPTVANILLA::equals)
        .networkProtocolVersion { "1145141919810364364893931" }
        .simpleChannel()

    var timer = Timer()

    init {
        startKoin {
            modules(module {
                single { this@CustomGUIMod }
            })
            printLogger()
        }

        channel.registerMessage(0, String::class.java, { st, buffer ->
            buffer.writeBytes(st.toByteArray(Charsets.UTF_8))
            buffer.writeByte(0)
        }, {
            var bytes = ByteArray(it.readableBytes())
            it.getBytes(0, bytes)
            bytes = ArrayUtils.remove(bytes, 0)
            StringUtils.toEncodedString(bytes, StandardCharsets.UTF_8)
        }) { st, ctx ->
            val json = st.asJsonObject()
            val data = json.getString("data")
            println(json)

            when (json.getIntOrNull("op") ?: -1) {
                0 -> {
                    data.asJsonArray().jsonArray.map { it.jsonObject }.mapNotNull {
                        when (it.getStringOrNull("type")?.lowercase(Locale.getDefault())) {
                            "button" -> it.toString().parseToObject<ButtonView>()
                            "text" -> it.toString().parseToObject<TextView>()
                            "image" -> it.toString().parseToObject<ImageView>()
                            "rect" -> it.toString().parseToObject<RectView>()
                            else -> null
                        }.also {
                            it?.init()
                        }
                    }.also {
                        GuiDesignerScreen.views = it.toMutableList()
                    }
                }

                2 -> {
                    channel.sendToServer(object {
                        val op = 1
                        val data = GuiDesignerScreen.views.map { it.parseToJson() }.filterNot { it.isBlank() }
                    }.toJson())

                    val fadeIn = json.getIntOrNull("fadeIn")
                    val stay = json.getIntOrNull("stay")
                    val fadeOut = json.getIntOrNull("fadeOut")
                    val isAspect = json.getBooleanOrNull("aspect")

                    if (fadeIn != null && stay != null && fadeOut != null && isAspect != null) {
                        var current = 0
                        if (Minecraft.getInstance().currentScreen == GuiViewerScreen)
                            Minecraft.getInstance().displayGuiScreen(null)

                        GuiViewerScreen.views.clear()
                        GuiViewerScreen.opacity = 0.0
                        GuiOverlay.views.clear()
                        GuiOverlay.opacity = 0.0
                        GuiOverlay.isAspectMode = isAspect
                        timer.cancel()
                        timer = Timer()
                        timer.schedule(timerTask {
                            when {
                                current in 0..fadeIn -> {
                                    if (fadeIn == 0)
                                        GuiOverlay.opacity = 1.0
                                    else
                                        GuiOverlay.opacity += (1.0 / fadeIn)
                                }

                                current in (fadeIn + 1)..(fadeIn + stay) -> {
                                    GuiOverlay.opacity = 1.0
                                }

                                current in (fadeIn + stay + 1)..(fadeIn + stay + fadeOut) -> {
                                    GuiOverlay.opacity -= (1.0 / fadeIn)
                                }

                                current > fadeIn + stay + fadeOut -> {
                                    GuiOverlay.views.clear()
                                    cancel()
                                }
                            }
                            current++
                        }, 0, 50)
                    }

                    data.asJsonArray().jsonArray.map { it.jsonObject }.mapNotNull {
                        when (it.getStringOrNull("type")?.lowercase(Locale.getDefault())) {
                            "button" -> it.toString().parseToObject<ButtonView>()
                            "text" -> it.toString().parseToObject<TextView>()
                            "image" -> it.toString().parseToObject<ImageView>()
                            "rect" -> it.toString().parseToObject<RectView>()
                            else -> null
                        }.also {
                            it?.init()
                        }
                    }.also {
                        GuiOverlay.views = it.toMutableList()
                    }
                }

                3 -> {
                    channel.sendToServer(object {
                        val op = 1
                        val data = GuiDesignerScreen.views.map { it.parseToJson() }.filterNot { it.isBlank() }
                    }.toJson())

                    val fadeIn = json.getIntOrNull("fadeIn")
                    val stay = json.getIntOrNull("stay")
                    val fadeOut = json.getIntOrNull("fadeOut")
                    val isAspect = json.getBooleanOrNull("aspect")

                    if (fadeIn != null && stay != null && fadeOut != null && isAspect != null) {
                        var current = 0
                        GuiOverlay.views.clear()
                        GuiOverlay.opacity = 0.0

                        GuiViewerScreen.views.clear()
                        GuiViewerScreen.opacity = 0.0
                        GuiViewerScreen.isAspectMode = isAspect

                        timer.cancel()
                        timer = Timer()
                        timer.schedule(timerTask {
                            when {
                                current in 0..fadeIn -> {
                                    if (fadeIn == 0)
                                        GuiViewerScreen.opacity = 1.0
                                    else
                                        GuiViewerScreen.opacity += (1.0 / fadeIn)
                                }

                                current in (fadeIn + 1)..(fadeIn + stay) -> {
                                    GuiViewerScreen.opacity = 1.0
                                }

                                current in (fadeIn + stay + 1)..(fadeIn + stay + fadeOut) -> {
                                    GuiViewerScreen.opacity -= (1.0 / fadeIn)
                                }

                                current > fadeIn + stay + fadeOut -> {
                                    GuiViewerScreen.views.clear()
                                    Minecraft.getInstance().displayGuiScreen(null)
                                    Minecraft.getInstance().isGameFocused = true
                                    cancel()
                                }
                            }
                            current++
                        }, 0, 50)
                    }

                    data.asJsonArray().jsonArray.map { it.jsonObject }.mapNotNull {
                        when (it.getStringOrNull("type")?.lowercase(Locale.getDefault())) {
                            "button" -> it.toString().parseToObject<ButtonView>()
                            "text" -> it.toString().parseToObject<TextView>()
                            "image" -> it.toString().parseToObject<ImageView>()
                            "rect" -> it.toString().parseToObject<RectView>()
                            else -> null
                        }.also {
                            it?.init()
                        }
                    }.also {
                        GuiViewerScreen.views = it.toMutableList()
                    }

                    Minecraft.getInstance().displayGuiScreen(GuiViewerScreen)
                }

                else -> {
                    //do nothing
                }
            }

            ctx.get()?.packetHandled = true
        }

        MinecraftForge.EVENT_BUS.register(this)
        MinecraftForge.EVENT_BUS.register(GuiOverlay)

        ClientRegistry.registerKeyBinding(
            keyBinding
        )
    }

    @SubscribeEvent
    fun onKeyboard(event: InputEvent.KeyInputEvent) {
        if (!keyBinding.isPressed) return

        channel.sendToServer(object {
            val op = 0
            val data = object {

            }
        }.toJson())

        Minecraft.getInstance().displayGuiScreen(GuiDesignerScreen)
    }
}