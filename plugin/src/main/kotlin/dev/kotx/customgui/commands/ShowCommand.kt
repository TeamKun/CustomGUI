package dev.kotx.customgui.commands

import dev.kotx.customgui.Files
import dev.kotx.customgui.gateway.GatewayClient
import dev.kotx.customgui.gateway.OpCode
import dev.kotx.customgui.json
import dev.kotx.flylib.command.Command
import dev.kotx.flylib.command.CommandContext
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.awt.Color
import java.util.*

class ShowCommand : Command("show"), KoinComponent {
    private val gatewayClient: GatewayClient by inject()

    init {
        description("指定したGUIを指定したプレイヤーにクリック可能なGUI形式で表示します。自分以外に表示させるには権限が必要です。")
        usage {
            textArgument("gui") {
                if (sender is Player) Files.guis.forEach {
                    suggest(
                        it.name,
                        server.getOfflinePlayer(UUID.fromString(it.author)).name
                    )
                }
            }

            entityArgument("target(s)")

            textArgument("mode") {
                suggest("fix", "固定サイズで表示します。")
                suggest("flex", "画面のサイズに引き延ばします。")
            }
        }

        usage {
            textArgument("gui") {
                if (sender is Player) Files.guis.forEach {
                    suggest(
                        it.name,
                        server.getOfflinePlayer(UUID.fromString(it.author)).name
                    )
                }
            }

            entityArgument("target(s)")

            textArgument("mode") {
                suggest("fix", "固定サイズで表示します。")
                suggest("flex", "画面のサイズに引き延ばします。")
            }

            integerArgument("fadein ticks")
            integerArgument("stay ticks")
            integerArgument("fadeout ticks")
        }
    }

    override fun CommandContext.execute() {
        val gui = Files.findByName(args[0])
        val targets = Bukkit.selectEntities(sender, args[1]).filterIsInstance<Player>()
        val mode = args[2].lowercase()
        val fadeinTicks = args.getOrNull(3)?.toInt() ?: 0
        val stayTicks = args.getOrNull(4)?.toInt() ?: Int.MAX_VALUE
        val fadeoutTicks = args.getOrNull(5)?.toInt() ?: 0

        if (gui == null) {
            pluginMessage {
                bold(args[0], Color.RED)
                append("は存在しません。", Color.RED)
            }

            return
        }

        if (targets.isEmpty()) {
            pluginMessage {
                bold(args[1], Color.RED)
                append("は一人も存在しません。", Color.RED)
            }

            return
        }

        if (!player!!.hasPermission("customgui.gui.show") && targets.any { it.uniqueId != player!!.uniqueId }) {
            pluginMessage {
                append("他のプレイヤーにGUIを表示させるには、", Color.RED)
                bold("customgui.gui.show", Color.RED)
                append("の権限を所有している必要があります。", Color.RED)
            }

            return
        }

        if (mode != "fix" && mode != "flex") {
            pluginMessageFail("表示モードはfixかflexにしてください。")

            return
        }

        if (fadeinTicks < 0) {
            pluginMessageFail("フェードインTick数は正の数である必要があります。")

            return
        }

        if (stayTicks < 0) {
            pluginMessageFail("表示Tick数は正の数である必要があります。")

            return
        }

        if (fadeoutTicks < 0) {
            pluginMessageFail("フェードアウトTick数は正の数である必要があります。")

            return
        }

        targets.forEach {
            gatewayClient.send(it, OpCode.SHOW_GUI, json {
                "mode" to 1
                "fadeinTicks" to fadeinTicks
                "stayTicks" to stayTicks
                "fadeoutTicks" to fadeoutTicks
                "views" to gui.data["views"]
            })
        }

        if (targets.size == 1) {
            pluginMessage {
                bold(targets[0].name, Color.GREEN)
                append("にGUIを表示しました。", Color.GREEN)
            }
        } else {
            pluginMessageSuccess("${targets.size}人のプレイヤーにGUIを表示しました。")
        }
    }
}