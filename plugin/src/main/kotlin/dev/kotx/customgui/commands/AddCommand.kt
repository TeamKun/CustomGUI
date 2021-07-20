package dev.kotx.customgui.commands

import dev.kotx.customgui.CustomGUIListener
import dev.kotx.customgui.Files
import dev.kotx.customgui.GUI
import dev.kotx.customgui.asJsonObject
import dev.kotx.customgui.getArray
import dev.kotx.flylib.command.Command
import dev.kotx.flylib.command.CommandContext
import net.kyori.adventure.text.TextComponent
import org.bukkit.entity.Player
import java.awt.Color
import java.io.File
import java.util.*

class AddCommand : Command("add") {
    init {
        description("現在作業中のGUIを名前をつけて保存します。")

        usage {
            textArgument("name")
        }
    }

    private val workspaceDirectory = File("./plugins/CustomGUI/workspaces/")
    override fun CommandContext.execute() {
        if (sender !is Player) {
            pluginMessageFail("プレイヤーのみがこのコマンドを実行できます。")
            return
        }

        val playerWorkspaceFile = File(workspaceDirectory, "${player!!.uniqueId}.json")
        if (!playerWorkspaceFile.exists()) {
            pluginMessageFail("作業スペースが一度も開かれていません。")
            return
        }

        val normalizer = "[a-zA-Z0-9-_]+".toRegex()

        if (!normalizer.matches(args.first())) {
            pluginMessage {
                bold(args.first(), Color.RED)
                append("は使用できない文字が含まれています。GUI名には半角英数字とハイフン、アンダースコアを使用することが出来ます。")
            }
            return
        }

        val gui = Files.findByName(args.first())
        if (gui != null) {
            val author = server.getOfflinePlayer(UUID.fromString(gui.author))
            if (gui.author == player!!.uniqueId.toString() || player!!.hasPermission("customgui.gui.delete")) {
                pluginMessage {
                    bold(args.first())
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

                CustomGUIListener.wait(player!!) {
                    val message = (it.message() as TextComponent).content()
                    when (message.lowercase()) {
                        "yes", "y", "はい" -> {
                            val playerWorkspaceJson = playerWorkspaceFile.readText().asJsonObject()
                            val views = playerWorkspaceJson.getArray("views")

                            Files.save(
                                GUI(
                                    player!!.uniqueId.toString(),
                                    player!!.name,
                                    views
                                )
                            )

                            pluginMessage {
                                bold(args.first(), Color.GREEN)
                                append("を更新しました。")
                            }
                        }

                        else -> {
                            pluginMessage {
                                bold(args.first(), Color.RED)
                                append("の更新をキャンセルしました。")
                            }
                        }
                    }
                }
            } else {
                pluginMessage {
                    bold(args.first(), Color.RED)
                    append("という名前のGUIは", Color.RED)
                    bold(author.name ?: "<UnknownPlayer>", Color.RED)
                    append("が既に作成しています。作成されていないGUI名を使用して下さい。", Color.RED)
                }
            }
            return
        }

        val playerWorkspaceJson = playerWorkspaceFile.readText().asJsonObject()
        val views = playerWorkspaceJson.getArray("views")

        Files.save(
            GUI(
                player!!.uniqueId.toString(),
                player!!.name,
                views
            )
        )

        pluginMessage {
            bold(args.first(), Color.GREEN)
            append("という名前でGUIを保存しました。")
        }
    }
}