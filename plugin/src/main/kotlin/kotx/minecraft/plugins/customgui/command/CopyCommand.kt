package kotx.minecraft.plugins.customgui.command

import kotx.minecraft.libs.flylib.command.Command
import kotx.minecraft.libs.flylib.command.CommandContext
import kotx.minecraft.libs.flylib.command.internal.Permission
import kotx.minecraft.libs.flylib.command.internal.Usage
import kotx.minecraft.plugins.customgui.directory.Directories


class CopyCommand : Command("copy") {
    override val description: String = "指定したファイルを自分の物としてコピーします。"
    override val usages: List<Usage> = listOf(
        Usage(
            "copy <from> <to>"
        )
    )
    override val examples: List<String> = listOf(
        "customgui copy TestGUI CopiedTestGUI"
    )

    override val permission: Permission = Permission.EVERYONE
    override val playerOnly: Boolean = false

    override fun CommandContext.execute() {
        if (args.size != 2) {
            sendHelp()
            return
        }

        val fromFileName = args.first().replace("[/\\\\.]".toRegex(), "")
        val toFileName = args[1].replace("[/\\\\.]".toRegex(), "")
        val fromFile = Directories.guis.files.find { fromFileName == it.nameWithoutExtension }

        if (fromFile == null) {
            sendErrorMessage("${toFileName}は見つかりませんでした。")
            return
        }

        if (Directories.guis.files.any { toFileName == it.nameWithoutExtension }) {
            sendErrorMessage("${toFileName}は既に存在しています。")
            return
        }

        Directories.guis.write("${player!!.uniqueId}/$toFileName.json", fromFile.readText())
        sendSuccessMessage("${fromFileName}を${toFileName}にコピーしました")
    }

    override fun CommandContext.tabComplete() =
        if (args.size == 1) Directories.guis.files.map { it.nameWithoutExtension } else emptyList()
}