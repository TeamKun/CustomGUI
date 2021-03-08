package kotx.minecraft.plugins.customgui.extensions

import org.bukkit.event.Event
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.player.AsyncPlayerChatEvent

object EventWaiter : Listener {
    private val waitingEvents = mutableMapOf<Class<*>, MutableList<WaitingEvent<in Event>>>()

    @EventHandler
    fun onInventoryClick(event: InventoryClickEvent) = onEvent(event)

    @EventHandler
    fun onChat(event: AsyncPlayerChatEvent) = onEvent(event)

    private fun onEvent(event: Event) {
        val clazz = event::class.java
        waitingEvents.filter { it.key == clazz }.forEach { (k, v) ->
            val delTarget = v.filter { it.onClick(event) }.toList()
            v.removeAll(delTarget)
        }
    }

    fun <T : Event> register(clazz: Class<T>, onClick: (T) -> Boolean) {
        waitingEvents.putIfAbsent(clazz, mutableListOf())
        waitingEvents[clazz]?.add(WaitingEvent(onClick as (Event) -> Boolean))
    }

    class WaitingEvent<T : Event>(
        val onClick: (T) -> Boolean,
    )
}