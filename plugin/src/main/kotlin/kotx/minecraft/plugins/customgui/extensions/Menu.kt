package kotx.minecraft.plugins.customgui.extensions

import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.ItemStack

class Menu(
    private val title: String,
    private val size: Int,
    private val items: MutableMap<Int, Pair<ItemStack, (InventoryClickEvent) -> Boolean>>,
) : Listener {
    fun display(player: Player) {
        val inventory = Bukkit.createInventory(null, size, title).apply {
            items.forEach {
                setItem(it.key, it.value.first)
            }
        }
        player.openInventory(inventory)
        EventWaiter.register(InventoryClickEvent::class.java) { e ->
            if (e.whoClicked !is Player) return@register false
            if (e.whoClicked.uniqueId == player.uniqueId) return@register false
            if (e.inventory != inventory) return@register false

            e.isCancelled = true

            items[e.slot]?.second?.invoke(e) ?: false
        }
    }

    class Builder {
        var title: String = ""
        var size: Int = 9
        private val items: MutableMap<Int, Pair<ItemStack, (InventoryClickEvent) -> Boolean>> = mutableMapOf()

        fun addItem(index: Int, stack: ItemStack, onClick: (InventoryClickEvent) -> Boolean) {
            items[index] = stack to onClick
        }

        fun build(): Menu = Menu(title, size, items)
    }

    companion object {
        fun create(player: Player, block: Builder.() -> Unit) {
            Builder().apply(block).build().display(player)
        }
    }
}