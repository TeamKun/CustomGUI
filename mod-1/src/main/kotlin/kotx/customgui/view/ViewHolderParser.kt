package kotx.customgui.view

import kotlinx.serialization.json.*

interface ViewHolderParser<T : ViewHolder> {
    val type: ViewType
    val viewParser: ViewParser<out View>

    fun encode(view: T): JsonObject
    fun decode(json: JsonObject): T
}