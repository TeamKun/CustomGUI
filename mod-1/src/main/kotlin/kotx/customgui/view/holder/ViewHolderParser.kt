package kotx.customgui.view.holder

import kotlinx.serialization.json.*
import kotx.customgui.view.*

interface ViewHolderParser<V : View, T : ViewHolder<V>> {
    val type: ViewType
    val parser: ViewParser<V>

    fun encode(holder: T): JsonObject
    fun decode(json: JsonObject): T
}