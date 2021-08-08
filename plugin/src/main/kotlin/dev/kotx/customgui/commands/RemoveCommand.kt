package dev.kotx.customgui.commands

import dev.kotx.customgui.Files
import dev.kotx.flylib.command.Command
import dev.kotx.flylib.command.CommandContext
import org.bukkit.entity.Player
import java.awt.Color
import java.util.*

class RemoveCommand : Command("remove") {
    init {
        description("指定したGUIを削除します。自分以外のGUIを削除するには権限が必要です。")

        usage {
            textArgument("gui") {
                when (sender) {
                    is Player -> if (player!!.hasPermission("customgui.gui.delete"))
                        Files.guis
                    else
                        Files.guis.filter { it.author == player!!.uniqueId.toString() }

                    else -> Files.guis
                }.forEach {
                    suggest(it.name, server.getOfflinePlayer(UUID.fromString(it.author)).name)
                }
            }
        }
    }

    override fun CommandContext.execute() {
        val gui = Files.findByName(args[0])

        if (gui == null) {
            pluginMessage {
                bold(args[0], Color.RED)
                append("は存在しません。", Color.RED)
            }

            return
        }

        if (sender !is Player) {
            Files.deleteByName(gui.name)
            pluginMessage {
                bold(args[1], Color.GREEN)
                append("を削除しました。", Color.GREEN)
            }

            return
        }

        if (gui.author == player!!.uniqueId.toString() || sender.hasPermission("customgui.gui.delete")) {
            Files.deleteByName(gui.name)
            pluginMessage {
                bold(args[1], Color.GREEN)
                append("を削除しました。", Color.GREEN)
            }

            return
        }

        val author = server.getOfflinePlayer(UUID.fromString(gui.author))
        pluginMessage {
            bold(args.first(), Color.RED)
            append("という名前のGUIは", Color.RED)
            bold(author.name ?: "<UnknownPlayer>", Color.RED)
            append("が作成しています。削除するには、", Color.RED)
            bold("customgui.gui.delete", Color.RED)
            append("の権限を所有している必要があります。", Color.RED)
        }
    }
}