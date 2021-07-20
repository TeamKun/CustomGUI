package dev.kotx.customgui.commands

import dev.kotx.customgui.Files
import dev.kotx.customgui.asJsonObject
import dev.kotx.customgui.getArray
import dev.kotx.flylib.command.Command
import dev.kotx.flylib.command.CommandContext
import org.bukkit.entity.Player
import java.awt.Color
import java.io.File

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
                append("は使用できない文字が含まれています。ファイル名には半角英数字とハイフン、アンダースコアを使用することが出来ます。")
            }
            return
        }

        if (Files.guis.any { it.name == args.first() }) {
            Files.guis.find { it.name == args.first() }!!.author
            pluginMessage {

            }
            return
        }

        val playerWorkspaceJson = playerWorkspaceFile.readText().asJsonObject()
        val views = playerWorkspaceJson.getArray("views")
    }
}