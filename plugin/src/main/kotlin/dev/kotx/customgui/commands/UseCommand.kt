package dev.kotx.customgui.commands

import dev.kotx.customgui.Files
import dev.kotx.customgui.gateway.GatewayClient
import dev.kotx.customgui.gateway.OpCode
import dev.kotx.flylib.command.Command
import dev.kotx.flylib.command.CommandContext
import org.bukkit.entity.Player
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.awt.Color
import java.io.File
import java.util.*

class UseCommand : Command("use"), KoinComponent {
    private val gatewayClient: GatewayClient by inject()

    init {
        description("指定したGUIを自分の作業スペースに読み込みます。誰のGUIで合っても読み込むことが出来ます。")

        usage {
            textArgument("gui") {
                if (sender is Player) Files.guis.forEach {
                    suggest(
                        it.name,
                        server.getOfflinePlayer(UUID.fromString(it.author)).name
                    )
                }
            }
        }
    }

    private val workspaceDirectory = File("./plugins/CustomGUI/workspaces/")
    override fun CommandContext.execute() {
        if (sender !is Player) {
            pluginMessageFail("プレイヤーのみがこのコマンドを実行できます。")
            return
        }

        val gui = Files.findByName(args[0])

        if (gui == null) {
            pluginMessage {
                bold(args[0], Color.RED)
                append("は存在しません。", Color.RED)
            }

            return
        }

        val playerWorkspaceFile = File(workspaceDirectory, "${player!!.uniqueId}.json")
        if (!workspaceDirectory.exists())
            workspaceDirectory.mkdirs()

        if (!playerWorkspaceFile.exists())
            playerWorkspaceFile.createNewFile()

        playerWorkspaceFile.writeText(gui.data.toString())

        gatewayClient.send(player!!, OpCode.LOAD_GUI, gui.data)

        pluginMessage {
            bold(args[0], Color.GREEN)
            append("をワークスペースに読み込みました。", Color.GREEN)
        }
    }
}