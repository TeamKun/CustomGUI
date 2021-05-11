package kotx.minecraft.plugins.customgui.command

import kotx.minecraft.libs.flylib.append
import kotx.minecraft.libs.flylib.appendln
import kotx.minecraft.libs.flylib.asTextComponent
import kotx.minecraft.libs.flylib.command.Command
import kotx.minecraft.libs.flylib.command.CommandContext
import kotx.minecraft.libs.flylib.joint
import kotx.minecraft.plugins.customgui.directory.Directories
import net.kyori.adventure.text.Component
import java.awt.Color
import java.util.*


class ListCommand : Command("list") {
    override val description: String = "GUI一覧を表示します。"
    override val examples: List<String> = listOf(
        "customgui list"
    )
    override fun CommandContext.execute() {
        if (args.isNotEmpty()) {
            sendHelp()
            return
        }

        if (Directories.guis.files.isEmpty()) {
            sendErrorMessage("GUIが一つも登録されていません！")
            return
        }

        send {
            append("GUI一覧: ", Color.GREEN)
            append("(", Color.WHITE)
            append(Directories.guis.files.size.toString(), Color.GREEN)
            appendln(")", Color.WHITE)
            Directories.guis.files.map { f ->
                Component.text {
                    it.append(f.nameWithoutExtension, Color.GREEN)
                    it.append(" (", Color.WHITE)
                    it.append(
                        plugin.server.getOfflinePlayer(UUID.fromString(f.parentFile.name)).name
                            ?: "<Unknown Player>", Color.RED
                    )
                    it.append(")", Color.WHITE)
                }
            }.joint(", ".asTextComponent(Color.GRAY)) { append(it) }
        }
    }
}