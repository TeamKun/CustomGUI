package kotx.minecraft.plugins.customgui.command

import kotx.minecraft.libs.flylib.command.Command
import kotx.minecraft.libs.flylib.command.internal.Argument
import kotx.minecraft.libs.flylib.command.internal.Usage
import kotx.minecraft.plugins.customgui.directory.Directories
import java.util.*


class RemoveCommand : Command("remove") {
    override val description: String = "指定したファイルを削除します。(自身のGUIのみ)"
    override val usages: List<Usage> = listOf(
        Usage(Argument.Text("target_file") {
            (if (player!!.isOp) Directories.guis.files else Directories.guis.files.filter { it.parentFile.name == player!!.uniqueId.toString() }).map { it.nameWithoutExtension }
        }) {
            val fileName = args.first().replace("[/\\\\.]".toRegex(), "")
            val target = Directories.guis.files.find {
                fileName == it.nameWithoutExtension
            }

            if (target == null) {
                sendErrorMessage("${fileName}は見つかりませんでした。")
                return@Usage
            }


            if (target.parentFile.name != player!!.uniqueId.toString() && !player!!.isOp) {
                val owner = plugin.server.getOfflinePlayer(UUID.fromString(target.parentFile.name))
                sendErrorMessage("${fileName}は${owner.name}が制作した物です。OP以外は他のプレイヤーのGUIを削除できません。")
                return@Usage
            }

            target.delete()
            sendSuccessMessage("${fileName}を削除しました。")
        }
    )
    override val examples: List<String> = listOf(
        "customgui remove TestGUI"
    )
}