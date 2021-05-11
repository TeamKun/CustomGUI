package kotx.minecraft.plugins.customgui.command

import kotx.ktools.asPacket
import kotx.ktools.toJson
import kotx.minecraft.libs.flylib.append
import kotx.minecraft.libs.flylib.asTextComponent
import kotx.minecraft.libs.flylib.command.Command
import kotx.minecraft.libs.flylib.command.CommandContext
import kotx.minecraft.libs.flylib.command.internal.Argument
import kotx.minecraft.libs.flylib.command.internal.Permission
import kotx.minecraft.libs.flylib.command.internal.Usage
import kotx.minecraft.libs.flylib.joint
import kotx.minecraft.plugins.customgui.directory.Directories
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import java.awt.Color
import java.util.*

class ShowCommand : Command("show") {
    override val description: String = "指定したGUIを操作できる形で表示します。"

    override val usages: List<Usage> = listOf(
        Usage(
            Argument.Text("file") { Directories.guis.files.map { it.nameWithoutExtension } },
        ),
        Usage(
            Argument.Text("file") { Directories.guis.files.map { it.nameWithoutExtension } },
            Argument.Entity("user"),
            permission = Permission.OP,
        ),
        Usage(
            Argument.Text("file") { Directories.guis.files.map { it.nameWithoutExtension } },
            Argument.Entity("user"),
            Argument.Selection("mode", "flex", "fix"),
            permission = Permission.OP,
        ),
        Usage(
            Argument.Text("file") { Directories.guis.files.map { it.nameWithoutExtension } },
            Argument.Entity("user"),
            Argument.Selection("mode", "flex", "fix"),
            Argument.Integer("fadein_tick"),
            Argument.Integer("stay_tick"),
            Argument.Integer("fadeout_tick"),
            permission = Permission.OP,
        ),
    )
    override val examples: List<String> = listOf(
        "customgui show testgui",
        "customgui show testgui roadhog_kun",
        "customgui show testgui roadhog_kun fix",
        "customgui show testgui roadhog_kun fix 40 100 40",
    )

    override fun CommandContext.execute() {
        if (args.isEmpty()) {
            sendHelp()
            return
        }

        val targetGui = Directories.guis.files.find { it.nameWithoutExtension == args.first() }
        val targetUsers = (if (args.size >= 2)
            Bukkit.selectEntities(sender, args[1])
        else
            listOf(player)).filterNotNull().filterIsInstance<Player>()

        if (targetGui == null) {
            sendErrorMessage("${args.first()}が見つかりません！")
            return
        }

        if (targetUsers.isEmpty()) {
            sendErrorMessage("対象が見つかりません！")
            return
        }

        if (targetUsers.any { it.playerProfile.name != sender.name } && !sender.isOp) {
            sendErrorMessage("管理者以外が自分以外のユーザーにGUIを表示させることは出来ません！")
            return
        }

        val guiData = targetGui.readText()
        val fixMode = if (args.size >= 3) args[2].lowercase(Locale.getDefault()) == "fix" else false
        val fadeinTick = if (args.size >= 4) args[3].toIntOrNull() else 0
        val stayTick = if (args.size >= 5) args[4].toIntOrNull() else Int.MAX_VALUE
        val fadeoutTick = if (args.size >= 6) args[5].toIntOrNull() else 0

        targetUsers.forEach {
            it.sendPluginMessage(plugin, "customgui:workspace", object {
                val op = 3
                val data = guiData
                val fadeIn = fadeinTick
                val stay = stayTick
                val fadeOut = fadeoutTick
                val isAspect = fixMode
            }.toJson().asPacket())
        }

        send {
            append("${targetGui.nameWithoutExtension}を", Color.GREEN)
            targetUsers.mapNotNull { it.playerProfile.name?.asTextComponent() }
                .joint(", ".asTextComponent(Color.GRAY))
                .forEach { append(it) }
            append("に表示しました。", Color.GREEN)
        }
    }
}