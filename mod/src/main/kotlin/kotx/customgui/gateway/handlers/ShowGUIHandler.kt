package kotx.customgui.gateway.handlers

import kotlinx.coroutines.*
import kotlinx.serialization.json.JsonObject
import kotx.customgui.CustomGUIMod
import kotx.customgui.gateway.GatewayHandler
import kotx.customgui.gateway.OpCode
import kotx.customgui.gui.GUI
import kotx.customgui.gui.guis.ClickableGUI
import kotx.customgui.util.getBoolean
import kotx.customgui.util.getInt
import kotx.customgui.util.getObjectArray
import kotx.customgui.util.timer
import kotx.customgui.view.ViewType

class ShowGUIHandler : GatewayHandler {
    override val opCode: OpCode = OpCode.SHOW_GUI

    private val scope = CoroutineScope(Dispatchers.Default) + CoroutineName("Animator")
    private var timer: Job? = null
    override fun handle(json: JsonObject) {
        val mode = json.getInt("mode")
        val isFlex = json.getBoolean("flex")
        val fadeinTicks = json.getInt("fadeinTicks")
        val stayTicks = json.getInt("stayTicks")
        val fadeoutTicks = json.getInt("fadeoutTicks")

        val guis = json.getObjectArray("views").map {
            val type = ViewType.get(it.getInt("type"))
            val parser = CustomGUIMod.viewHandler.getParser(type)
            parser.decode(it)
        }

        when (mode) {
            //show gui
            1 -> {
                ClickableGUI.opacity = 0.0
                ClickableGUI.holders.clear()
                ClickableGUI.holders.addAll(guis)
                ClickableGUI.isFlex = isFlex

                GUI.display(ClickableGUI)
                var frame = 1
                timer?.cancel()
                timer = scope.timer(17, true) {
                    val tick = frame / 3
                    ClickableGUI.opacity = when {
                        tick in 0..fadeinTicks -> 255.0 / (fadeinTicks * 3) * frame
                        tick in fadeinTicks..(fadeinTicks + stayTicks) -> 255.0
                        tick in (fadeinTicks + stayTicks)..Int.MAX_VALUE -> 255.0 - 255.0 / (fadeinTicks * 3) * (frame - (fadeinTicks + stayTicks) * 3)

                        else -> 255.0
                    }.coerceAtLeast(0.0).coerceAtMost(255.0)

                    if (tick > fadeinTicks + stayTicks + fadeoutTicks) {
                        ClickableGUI.opacity = 0.0
                        ClickableGUI.holders.clear()
                        GUI.display()
                        cancel()
                    }

                    frame++
                }
            }

            //overlay gui
            2 -> {
                CustomGUIMod.opacity = 0.0
                CustomGUIMod.holders.clear()
                CustomGUIMod.holders.addAll(guis)
                CustomGUIMod.isFlex = isFlex

                var frame = 1
                timer?.cancel()
                timer = scope.timer(17) {
                    val tick = frame / 3
                    CustomGUIMod.opacity = when {
                        tick in 0..fadeinTicks -> 255.0 / (fadeinTicks * 3) * frame
                        tick in fadeinTicks..(fadeinTicks + stayTicks) -> 255.0
                        tick in (fadeinTicks + stayTicks)..Int.MAX_VALUE -> 255.0 - 255.0 / (fadeinTicks * 3) * (frame - (fadeinTicks + stayTicks) * 3)

                        else -> 255.0
                    }.coerceAtLeast(0.0).coerceAtMost(255.0)

                    if (tick > fadeinTicks + stayTicks + fadeoutTicks) {
                        CustomGUIMod.opacity = 0.0
                        CustomGUIMod.holders.clear()
                        cancel()
                    }

                    frame++
                }
            }
        }
    }
}