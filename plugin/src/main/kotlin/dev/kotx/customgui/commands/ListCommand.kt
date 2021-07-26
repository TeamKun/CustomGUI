package dev.kotx.customgui.commands

import dev.kotx.customgui.Files
import dev.kotx.flylib.command.Command
import dev.kotx.flylib.command.CommandContext
import java.awt.Color
import java.util.*

class ListCommand : Command("list") {
    init {
        description("全てのGUIの名前と作成者を表示します。")
    }

    override fun CommandContext.execute() {
        if (Files.guis.isEmpty()) {
            pluginMessageFail("一つもGUIが作成されていません。")
        } else {
            pluginMessage {
                append("GUI一覧:", Color.GREEN)
                append("(", Color.LIGHT_GRAY)
                append(Files.guis.size.toString(), Color.GREEN)
                appendln(")", Color.LIGHT_GRAY)

                Files.guis.forEach {
                    append(it.name, Color.RED)
                    append("(", Color.LIGHT_GRAY)
                    append(
                        it.author.let { server.getOfflinePlayer(UUID.fromString(it)) }.name ?: "<UNKNOWN>",
                        Color.RED
                    )
                    append(")", Color.LIGHT_GRAY)
                    append(" ")
                }
            }
        }
    }
}