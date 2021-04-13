package kotx.minecraft.plugins.customgui.command

import kotx.minecraft.libs.flylib.command.Command
import kotx.minecraft.libs.flylib.command.CommandContext
import kotx.minecraft.libs.flylib.command.internal.Permission
import kotx.minecraft.libs.flylib.command.internal.Usage

class ShowCommand : Command("show") {
    override val description: String = "指定したGUIを操作できる形で表示します。"

    override val usages: List<Usage> = listOf(
        Usage("show <file> [user]"),
        Usage("show <file> <user> <flex/fix>"),
        Usage("show <file> <user> <flex/fix> <fadein_tick> <stay_tick> <fadeout_tick>"),
    )
    override val examples: List<String> = listOf(
        "customgui show testgui",
        "customgui show testgui roadhog_kun",
        "customgui show testgui roadhog_kun fix",
        "customgui show testgui roadhog_kun fix 40 100 40",
    )
    override val permission: Permission = Permission.EVERYONE


    override fun CommandContext.execute() {
        if (args.isEmpty()) {
            sendHelp()
            return
        }


    }

    override fun CommandContext.tabComplete() = emptyList<String>()
}