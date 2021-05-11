package kotx.minecraft.plugins.customgui.command

import io.papermc.paper.event.player.AsyncChatEvent
import kotx.minecraft.libs.flylib.appendln
import kotx.minecraft.libs.flylib.command.Command
import kotx.minecraft.libs.flylib.command.CommandContext
import kotx.minecraft.libs.flylib.command.internal.Argument
import kotx.minecraft.libs.flylib.command.internal.Usage
import kotx.minecraft.plugins.customgui.directory.Directories
import kotx.minecraft.plugins.customgui.extensions.EventWaiter
import net.kyori.adventure.text.TextComponent
import org.bukkit.entity.Player
import java.awt.Color
import java.nio.file.Paths
import java.util.*

class AddCommand : Command("add") {
    override val description: String = "現在のワークスペースをファイルに保存します。"
    override val usages: List<Usage> = listOf(
        Usage(
            Argument.Text("file_name")
        ) {
            val fileName = args.first().replace("[/\\\\.]".toRegex(), "")
            if (fileName.length > 32) {
                sendErrorMessage("ファイル名は32文字を超えてはいけません。")
                return@Usage
            }

            val duplicatedFile = Directories.guis.files.find { fileName == it.nameWithoutExtension }
            if (duplicatedFile != null) {
                if (duplicatedFile.parentFile.name != player?.uniqueId.toString()) {
                    sendErrorMessage("${fileName}は既に存在しています。他のユーザーのファイルは上書きできません。")
                    return@Usage
                }

                send {
                    appendln("${fileName}は既に存在しています。上書きしますか？")
                    appendln("以下のどちらかをチャットに入力 (デフォルト: No)\n")
                    appendln("Yes (Y)", Color.GREEN)
                    appendln("No (N)", Color.RED)
                }

                EventWaiter.register(AsyncChatEvent::class.java) {
                    if (it.player.uniqueId != player?.uniqueId) return@register false
                    it.isCancelled = true
                    when ((it.message() as? TextComponent)?.content()?.lowercase(Locale.getDefault())) {
                        "yes", "y" -> if (player != null) saveGui(player!!, fileName)

                        else -> sendSuccessMessage("上書きをキャンセルしました。")
                    }
                    true
                }

                return@Usage
            }

            if (player != null) saveGui(player!!, fileName)
        },
    )
    override val examples: List<String> = listOf(
        "customgui add TestGUI"
    )

    private fun CommandContext.saveGui(player: Player, fileName: String) {
        val playerWorkspace =
            Paths.get("plugins", "CustomGUI", "workspaces", player.uniqueId.toString() + ".json").toFile()
        if (!playerWorkspace.exists()) {
            playerWorkspace.parentFile.mkdirs()
            playerWorkspace.createNewFile()
        }

        Directories.guis.write("${player.uniqueId}/$fileName.json", playerWorkspace.readText())

        sendSuccessMessage("${fileName}を保存しました。")
    }
}