package dev.kotx.customgui.commands

import dev.kotx.customgui.CustomGUIListener
import dev.kotx.customgui.Files
import dev.kotx.customgui.GUI
import dev.kotx.flylib.command.Command
import dev.kotx.flylib.command.CommandContext
import net.kyori.adventure.text.TextComponent
import org.bukkit.entity.Player
import java.awt.Color
import java.util.*

class CopyCommand : Command("copy") {
    init {
        description("指定したGUIを自分の物として別の名前でコピーします。")

        usage {
            textArgument("from") {
                Files.guis.forEach {
                    suggest(it.name, server.getOfflinePlayer(UUID.fromString(it.author)).name)
                }
            }

            textArgument("to")
        }
    }

    override fun CommandContext.execute() {
        if (args.size != 2) {
            sendHelp()
            return
        }

        val fromGui = args[0].let { Files.findByName(it) }
        val toGui = args[1].let { Files.findByName(it) }

        if (fromGui == null) {
            pluginMessage {
                bold(args[0], Color.RED)
                append("は存在しません。", Color.RED)
            }
            return
        }

        val normalizer = "[a-zA-Z0-9-_]+".toRegex()

        if (!normalizer.matches(args[1])) {
            pluginMessage {
                bold(args[1], Color.RED)
                append("は使用できない文字が含まれています。GUI名には半角英数字とハイフン、アンダースコアを使用することが出来ます。", Color.RED)
            }
            return
        }

        if (toGui != null) {
            val author = server.getOfflinePlayer(UUID.fromString(toGui.author))

            when {
                sender !is Player -> overwrite(fromGui)

                toGui.author == player?.uniqueId?.toString() || sender.hasPermission("customgui.gui.delete") -> {
                    pluginMessage {
                        bold(args[1])
                        append("という名前のGUIは")
                        bold(author.name ?: "<UnknownPlayer>")
                        appendln("が既に作成しています。上書きしますか？")
                        append("チャットに入力")
                        appendln(":", Color.LIGHT_GRAY)
                        append("はい", Color.GREEN)
                        append(":", Color.LIGHT_GRAY)
                        boldln("Yes(Y)", Color.GREEN)
                        append("いいえ", Color.RED)
                        append(":", Color.LIGHT_GRAY)
                        bold("No(N)", Color.RED)
                    }

                    CustomGUIListener.waitForChat(player!!) {
                        it.isCancelled = true
                        val message = (it.message() as TextComponent).content()
                        when (message.lowercase()) {
                            "yes", "y", "はい" -> {
                                overwrite(fromGui)
                            }

                            else -> {
                                pluginMessage {
                                    bold(args[1], Color.RED)
                                    append("の上書きをキャンセルしました。", Color.RED)
                                }
                            }
                        }
                    }
                }

                else -> pluginMessage {
                    bold(args[1], Color.RED)
                    append("という名前のGUIは", Color.RED)
                    bold(author.name ?: "<UnknownPlayer>", Color.RED)
                    append("が既に作成しています。上書きするには、", Color.RED)
                    bold("customgui.gui.delete", Color.RED)
                    append("の権限を所有している必要があります。", Color.RED)
                }
            }
            return
        }

        Files.save(GUI(player!!.uniqueId.toString(), args[1], fromGui.data))
        pluginMessage {
            bold(args[0], Color.GREEN)
            append("を", Color.GREEN)
            bold(args[1], Color.GREEN)
            append("にコピーしました。", Color.GREEN)
        }
    }

    private fun CommandContext.overwrite(fromGui: GUI) {
        Files.save(
            GUI(
                player!!.uniqueId.toString(),
                player!!.name,
                fromGui.data
            )
        )

        pluginMessage {
            bold(args[1], Color.GREEN)
            append("を", Color.GREEN)
            bold(fromGui.name, Color.GREEN)
            append("で上書きしました。", Color.GREEN)
        }
    }
}