package kotx.customgui.view

import kotlinx.serialization.json.JsonObject

interface ViewParser<T : View> {
    fun encode(view: T): JsonObject
    fun decode(json: JsonObject): T
}