package dev.kotx.customgui.commands

import dev.kotx.customgui.Constants.workspaceDirectory
import dev.kotx.customgui.Files
import dev.kotx.customgui.GUI
import dev.kotx.customgui.asJsonObject
import dev.kotx.flylib.command.Command
import dev.kotx.flylib.command.CommandContext
import org.bukkit.entity.Player
import java.awt.Color
import java.io.File
import java.util.*


class UpdateCommand : Command("update") {
    init {
        description("指定したGUIを現在作業中のGUIで更新します。現在存在しないGUIを指定することは出来ません。")

        usage {
            textArgument("name")
        }
    }

    override fun CommandContext.execute() {
        if (args.isEmpty()) {
            sendHelp()
            return
        }

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
                append("は使用できない文字が含まれています。GUI名には半角英数字とハイフン、アンダースコアを使用することが出来ます。", Color.RED)
            }
            return
        }

        val gui = Files.findByName(args.first())

        if (gui == null) {
            pluginMessage {
                bold(args.first(), Color.RED)
                append("という名前のGUIは見つかりませんでした。", Color.RED)
            }
            return
        }

        if (gui.author == player!!.uniqueId.toString() || player!!.hasPermission("customgui.gui.delete")) {
            Files.save(
                GUI(
                    player!!.uniqueId.toString(),
                    player!!.name,
                    playerWorkspaceFile.readText().asJsonObject()
                )
            )

            pluginMessage {
                bold(args.first(), Color.GREEN)
                append("を現在のGUIで上書きしました。", Color.GREEN)
            }
        } else {
            val author = server.getOfflinePlayer(UUID.fromString(gui.author))
            pluginMessage {
                bold(args.first(), Color.RED)
                append("という名前のGUIは", Color.RED)
                bold(author.name ?: "<UnknownPlayer>", Color.RED)
                append("が作成しています。上書きするには、", Color.RED)
                bold("customgui.gui.delete", Color.RED)
                append("の権限を所有している必要があります。", Color.RED)
            }
        }
    }
}