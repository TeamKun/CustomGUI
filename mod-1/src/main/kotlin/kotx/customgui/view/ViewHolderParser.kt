package kotx.customgui.view

import kotlinx.serialization.json.JsonObject

interface ViewHolderParser<T : ViewHolder> {
    val type: ViewType
    val viewParser: ViewParser<out View>

    fun encode(holder: T): JsonObject
    fun decode(json: JsonObject): T
}