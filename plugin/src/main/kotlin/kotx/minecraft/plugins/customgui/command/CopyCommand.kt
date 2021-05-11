package kotx.minecraft.plugins.customgui.command

import kotx.minecraft.libs.flylib.command.Command
import kotx.minecraft.libs.flylib.command.internal.Argument
import kotx.minecraft.libs.flylib.command.internal.Usage
import kotx.minecraft.plugins.customgui.directory.Directories


class CopyCommand : Command("copy") {
    override val description: String = "指定したファイルを自分の物としてコピーします。"
    override val usages: List<Usage> = listOf(
        Usage(
            Argument.Text("from") {
                Directories.guis.files.map { it.nameWithoutExtension }
            },
            Argument.Text("to"),
        ) {
            val fromFileName = args.first().replace("[/\\\\.]".toRegex(), "")
            val toFileName = args[1].replace("[/\\\\.]".toRegex(), "")
            val fromFile = Directories.guis.files.find { fromFileName == it.nameWithoutExtension }

            if (fromFile == null) {
                sendErrorMessage("${toFileName}は見つかりませんでした。")
                return@Usage
            }

            if (Directories.guis.files.any { toFileName == it.nameWithoutExtension }) {
                sendErrorMessage("${toFileName}は既に存在しています。")
                return@Usage
            }

            Directories.guis.write("${player!!.uniqueId}/$toFileName.json", fromFile.readText())
            sendSuccessMessage("${fromFileName}を${toFileName}にコピーしました")
        }
    )
    override val examples: List<String> = listOf(
        "customgui copy TestGUI CopiedTestGUI"
    )
}