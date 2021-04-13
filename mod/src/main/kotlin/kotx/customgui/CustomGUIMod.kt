@file:Suppress("INACCESSIBLE_TYPE")

package kotx.customgui

import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotx.customgui.gui.GuiDesignerScreen
import kotx.customgui.gui.GuiOverlay
import kotx.customgui.gui.GuiViewerScreen
import kotx.customgui.gui.component.view.ButtonView
import kotx.customgui.gui.component.view.ImageView
import kotx.customgui.gui.component.view.RectView
import kotx.customgui.gui.component.view.TextView
import kotx.ktools.*
import net.minecraft.client.Minecraft
import net.minecraft.util.ResourceLocation
import net.minecraftforge.client.event.InputEvent
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.network.NetworkRegistry
import net.minecraftforge.fml.network.simple.SimpleChannel
import org.apache.commons.lang3.ArrayUtils
import org.apache.commons.lang3.StringUtils
import org.koin.core.context.startKoin
import org.koin.dsl.module
import org.lwjgl.glfw.GLFW
import java.nio.charset.StandardCharsets
import java.util.*
import kotlin.concurrent.timerTask

@Mod(CustomGUIMod.modId)
class CustomGUIMod {
    companion object {
        const val modId = "customgui"
        const val modName = "customgui"
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
                        when (it.getStringOrNull("type")?.toLowerCase()) {
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
                        if (Minecraft.getInstance().screen == GuiViewerScreen)
                            Minecraft.getInstance().setScreen(null)

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
                        when (it.getStringOrNull("type")?.toLowerCase()) {
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
                                    Minecraft.getInstance().forceSetScreen(null)
                                    cancel()
                                }
                            }
                            current++
                        }, 0, 50)
                    }

                    data.asJsonArray().jsonArray.map { it.jsonObject }.mapNotNull {
                        when (it.getStringOrNull("type")?.toLowerCase()) {
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

                    Minecraft.getInstance().forceSetScreen(GuiViewerScreen)
                }

                else -> {
                    //do nothing
                }
            }

            ctx.get()?.packetHandled = true
        }

        MinecraftForge.EVENT_BUS.register(this)
        MinecraftForge.EVENT_BUS.register(GuiOverlay)
    }


    @SubscribeEvent
    fun onKeyPressed(event: InputEvent.KeyInputEvent) {
        if (event.modifiers != GLFW.GLFW_MOD_SHIFT) return
        if (event.key != GLFW.GLFW_KEY_O) return
        if (Minecraft.getInstance().currentServer == null || Minecraft.getInstance().currentServer?.isLan == true) return

        channel.sendToServer(object {
            val op = 0
            val data = object {

            }
        }.toJson())

        Minecraft.getInstance().setScreen(GuiDesignerScreen)
    }
}