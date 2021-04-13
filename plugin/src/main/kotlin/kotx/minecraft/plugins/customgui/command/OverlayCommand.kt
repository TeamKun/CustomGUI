package kotx.minecraft.plugins.customgui.command

import kotx.ktools.asPacket
import kotx.ktools.toJson
import kotx.minecraft.libs.flylib.append
import kotx.minecraft.libs.flylib.asTextComponent
import kotx.minecraft.libs.flylib.command.Command
import kotx.minecraft.libs.flylib.command.CommandContext
import kotx.minecraft.libs.flylib.command.internal.Option
import kotx.minecraft.libs.flylib.command.internal.Permission
import kotx.minecraft.libs.flylib.command.internal.Usage
import kotx.minecraft.plugins.customgui.directory.Directories
import kotx.minecraft.plugins.customgui.extensions.joint
import kotx.minecraft.plugins.customgui.extensions.suggestEntities
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import java.awt.Color

class OverlayCommand : Command("overlay") {
    override val description: String = "指定したGUIをオーバーレイ形式で表示します。"
    override val usages: List<Usage> = listOf(
        Usage(
            "overlay <file> [op:user]",
            options = listOf(Option("aspect"))
        ),
        Usage(
            "overlay <file> <fadein_tick> <stay_tick> <fadeout_tick>",
            options = listOf(Option("aspect"))
        ),
        Usage(
            "overlay <file> <op:user> <op:fadein_tick> <op:stay_tick> <op:fadeout_tick>",
            options = listOf(Option("aspect"))
        ),
    )
    override val examples: List<String> = listOf(
        "customgui overlay TestGUI",
        "customgui overlay TestGUI Kotlinx"
    )
    override val permission: Permission = Permission.EVERYONE
    override val playerOnly: Boolean = false

    override fun CommandContext.execute() {
        if (argsWithoutOptions.isEmpty()) {
            sendHelp()
            return
        }

        if ((argsWithoutOptions.size == 2 || argsWithoutOptions.size == 5) && !player!!.isOp) {
            sendErrorMessage("管理者以外は対象を指定出来ません。")
            return
        }

        val fileName = argsWithoutOptions.first().replace("[/\\\\.]".toRegex(), "")
        val targetGui = Directories.guis.files.find {
            fileName == it.nameWithoutExtension
        }

        if (targetGui == null) {
            sendErrorMessage("${fileName}は見つかりませんでした。")
            return
        }

        val targetPlayers = when (argsWithoutOptions.size) {
            1, 4 -> listOf(player!!)
            else -> Bukkit.selectEntities(player!!, argsWithoutOptions[1]).filterIsInstance<Player>()
        }

        if (targetPlayers.isEmpty()) {
            sendErrorMessage("誰も見せる人が見つかりませんでした。")
            return
        }

        val fadeInTime = when (argsWithoutOptions.size) {
            in 0..2 -> 0
            4 -> argsWithoutOptions[1].toIntOrNull() ?: 0
            5 -> argsWithoutOptions[2].toIntOrNull() ?: 0

            else -> 0
        }

        val stayTime = when (argsWithoutOptions.size) {
            in 0..2 -> Int.MAX_VALUE
            4 -> argsWithoutOptions[2].toIntOrNull() ?: 100
            5 -> argsWithoutOptions[3].toIntOrNull() ?: 100

            else -> 0
        }

        val fadeOutTime = when (argsWithoutOptions.size) {
            in 0..2 -> Int.MAX_VALUE
            4 -> argsWithoutOptions[3].toIntOrNull() ?: 100
            5 -> argsWithoutOptions[4].toIntOrNull() ?: 100

            else -> 0
        }

        val isAspectMode = options["aspect"] != null

        val guiData = targetGui.readText()
        targetPlayers.forEach {
            it.sendPluginMessage(plugin, "customgui:workspace", object {
                val op = 2
                val data = guiData
                val fadeIn = fadeInTime
                val stay = stayTime
                val fadeOut = fadeOutTime
                val isAspect = isAspectMode
            }.toJson().asPacket())
        }

        send {
            append("${fileName}を", Color.GREEN)
            targetPlayers.mapNotNull { it.playerProfile.name }
                .map { it.asTextComponent() }
                .joint(", ".asTextComponent(Color.GRAY))
                .forEach { append(it) }
            append("に表示しました。", Color.GREEN)
        }
    }

    override fun CommandContext.tabComplete() = when {
        argsWithoutOptions.size == 1 -> Directories.guis.files.filter { it.isFile }.map { it.nameWithoutExtension }
        argsWithoutOptions.size == 2 && player!!.isOp -> suggestEntities(argsWithoutOptions[1], plugin)

        else -> emptyList()
    }
}