package kotx.minecraft.plugins.customgui.extensions

import net.md_5.bungee.api.chat.ClickEvent
import net.md_5.bungee.api.chat.ComponentBuilder
import net.md_5.bungee.api.chat.HoverEvent
import org.bukkit.ChatColor
import org.bukkit.command.CommandSender

fun String.format(chatColor: ChatColor) = "§${chatColor.char}$this"

fun CommandSender.send(block: ComponentBuilder.() -> Unit) {
    spigot().sendMessage(*ComponentBuilder().apply(block).create())
}

operator fun ComponentBuilder.plus(text: String): ComponentBuilder = append(text)
fun ComponentBuilder.hoverText(block: ComponentBuilder.() -> Unit) = event(
    HoverEvent(HoverEvent.Action.SHOW_TEXT, ComponentBuilder().apply(block).create())
)!!

fun ComponentBuilder.clickCommand(command: String) = event(
    ClickEvent(
        ClickEvent.Action.RUN_COMMAND,
        command
    )
)!!

fun ComponentBuilder.clickSuggestCommand(command: String) = event(
    ClickEvent(
        ClickEvent.Action.SUGGEST_COMMAND,
        command
    )
)!!

fun ComponentBuilder.clickOpenUrl(url: String) = event(
    ClickEvent(
        ClickEvent.Action.OPEN_URL,
        url
    )
)!!