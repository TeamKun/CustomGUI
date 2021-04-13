package kotx.minecraft.plugins.customgui.command

import kotx.minecraft.libs.flylib.command.Command
import kotx.minecraft.libs.flylib.command.CommandContext
import kotx.minecraft.libs.flylib.command.internal.Permission
import kotx.minecraft.libs.flylib.command.internal.Usage
import kotx.minecraft.plugins.customgui.directory.Directories
import java.util.*


class RemoveCommand : Command("remove") {
    override val description: String = "指定したファイルを削除します。(自身のGUIのみ)"
    override val usages: List<Usage> = listOf(
        Usage("remove <targetFile>")
    )
    override val examples: List<String> = listOf(
        "customgui remove TestGUI"
    )
    override val permission: Permission = Permission.EVERYONE


    override fun CommandContext.execute() {
        if (args.isEmpty()) {
            sendHelp()
            return
        }

        val fileName = args.first().replace("[/\\\\.]".toRegex(), "")
        val target = Directories.guis.files.find {
            fileName == it.nameWithoutExtension
        }

        if (target == null) {
            sendErrorMessage("${fileName}は見つかりませんでした。")
            return
        }


        if (target.parentFile.name != player!!.uniqueId.toString() && !player!!.isOp) {
            val owner = plugin.server.getOfflinePlayer(UUID.fromString(target.parentFile.name))
            sendErrorMessage("${fileName}は${owner.name}が制作した物です。OP以外は他のプレイヤーのGUIを削除できません。")
            return
        }

        target.delete()
        sendSuccessMessage("${fileName}を削除しました。")
    }

    override fun CommandContext.tabComplete() =
        if (args.size == 1)
            (if (player!!.isOp) Directories.guis.files else Directories.guis.files.filter { it.parentFile.name == player!!.uniqueId.toString() }).map { it.nameWithoutExtension }
        else
            emptyList()
}