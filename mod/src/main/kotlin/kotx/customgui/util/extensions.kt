package kotx.customgui.util

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.boolean
import kotlinx.serialization.json.booleanOrNull
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.double
import kotlinx.serialization.json.doubleOrNull
import kotlinx.serialization.json.float
import kotlinx.serialization.json.floatOrNull
import kotlinx.serialization.json.int
import kotlinx.serialization.json.intOrNull
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import kotlinx.serialization.json.long
import kotlinx.serialization.json.longOrNull
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.FontRenderer
import net.minecraft.util.text.StringTextComponent
import java.time.Instant
import java.time.format.DateTimeFormatter

val mc: Minecraft
    get() = Minecraft.getInstance()

val fontRenderer: FontRenderer
    get() = mc.fontRenderer

fun String.component() = StringTextComponent(this)

fun String.asJsonElement() = Json.parseToJsonElement(this)
fun String.asJsonObject() = Json.parseToJsonElement(this).jsonObject
fun String.asJsonArray() = Json.parseToJsonElement(this).jsonArray

fun JsonObject.getString(key: String) = get(key)!!.jsonPrimitive.content
fun JsonObject.getStringOrNull(key: String) = try {
    get(key)?.jsonPrimitive?.contentOrNull
} catch (e: Exception) {
    null
}

fun JsonObject.getInt(key: String) = get(key)!!.jsonPrimitive.int
fun JsonObject.getIntOrNull(key: String) = try {
    get(key)?.jsonPrimitive?.intOrNull
} catch (e: Exception) {
    null
}

fun JsonObject.getLong(key: String) = get(key)!!.jsonPrimitive.long
fun JsonObject.getLongOrNull(key: String) = try {
    get(key)?.jsonPrimitive?.longOrNull
} catch (e: Exception) {
    null
}

fun JsonObject.getDouble(key: String) = get(key)!!.jsonPrimitive.double
fun JsonObject.getDoubleOrNull(key: String) = try {
    get(key)?.jsonPrimitive?.doubleOrNull
} catch (e: Exception) {
    null
}

fun JsonObject.getFloat(key: String) = get(key)!!.jsonPrimitive.float
fun JsonObject.getFloatOrNull(key: String) = try {
    get(key)?.jsonPrimitive?.floatOrNull
} catch (e: Exception) {
    null
}

fun JsonObject.getBoolean(key: String) = get(key)!!.jsonPrimitive.boolean
fun JsonObject.getBooleanOrNull(key: String) = try {
    get(key)?.jsonPrimitive?.booleanOrNull
} catch (e: Exception) {
    null
}

fun JsonObject.getInstant(key: String) = get(key)!!
    .jsonPrimitive
    .content
    .let { DateTimeFormatter.ISO_DATE_TIME.parse(it) }
    .let { Instant.from(it) }

fun JsonObject.getInstantOrNull(key: String) = try {
    get(key)
        ?.jsonPrimitive
        ?.content
        ?.let { DateTimeFormatter.ISO_DATE_TIME.parse(it) }
        ?.let { Instant.from(it) }
} catch (e: Exception) {
    null
}

fun JsonObject.getPrimitive(key: String) = get(key)!!.jsonPrimitive
fun JsonObject.getPrimitiveOrNull(key: String) = try {
    get(key)?.jsonPrimitive
} catch (e: Exception) {
    null
}

fun JsonObject.getObject(key: String) = get(key)!!.jsonObject
fun JsonObject.getObjectOrNull(key: String) = try {
    get(key)?.jsonObject
} catch (e: Exception) {
    null
}

fun JsonObject.getArray(key: String) = get(key)!!.jsonArray
fun JsonObject.getArrayOrNull(key: String) = try {
    get(key)?.jsonArray
} catch (e: Exception) {
    null
}

fun JsonObject.getObjectArray(key: String) = get(key)!!.jsonArray.map { it.jsonObject }
fun JsonObject.getPrimitiveArray(key: String) = get(key)!!.jsonArray.map { it.jsonPrimitive }
fun JsonObject.getArrayArray(key: String) = get(key)!!.jsonArray.map { it.jsonArray }

fun JsonObject.getStringArray(key: String) = get(key)!!.jsonArray.map { it.jsonPrimitive.content }
fun JsonObject.getLongArray(key: String) = get(key)!!.jsonArray.map { it.jsonPrimitive.long }
fun JsonObject.getFloatArray(key: String) = get(key)!!.jsonArray.map { it.jsonPrimitive.float }
fun JsonObject.getDoubleArray(key: String) = get(key)!!.jsonArray.map { it.jsonPrimitive.double }
fun JsonObject.getBooleanArray(key: String) = get(key)!!.jsonArray.map { it.jsonPrimitive.boolean }

fun JsonObject.getObjectArrayOrNull(key: String) = try {
    get(key)?.jsonArray?.map { it.jsonObject }
} catch (e: Exception) {
    null
}

fun JsonObject.getPrimitiveArrayOrNull(key: String) = try {
    get(key)?.jsonArray?.map { it.jsonPrimitive }
} catch (e: Exception) {
    null
}

fun JsonObject.getArrayArrayOrNull(key: String) = try {
    get(key)?.jsonArray?.map { it.jsonArray }
} catch (e: Exception) {
    null
}

fun JsonObject.getStringArrayOrNull(key: String) = try {
    get(key)?.jsonArray?.map { it.jsonPrimitive.contentOrNull }
} catch (e: Exception) {
    null
}

fun JsonObject.getLongArrayOrNull(key: String) = try {
    get(key)?.jsonArray?.map { it.jsonPrimitive.longOrNull }
} catch (e: Exception) {
    null
}

fun JsonObject.getFloatArrayOrNull(key: String) = try {
    get(key)?.jsonArray?.map { it.jsonPrimitive.floatOrNull }
} catch (e: Exception) {
    null
}

fun JsonObject.getDoubleArrayOrNull(key: String) = try {
    get(key)?.jsonArray?.map { it.jsonPrimitive.doubleOrNull }
} catch (e: Exception) {
    null
}

fun JsonObject.getBooleanArrayOrNull(key: String) = try {
    get(key)?.jsonArray?.map { it.jsonPrimitive.booleanOrNull }
} catch (e: Exception) {
    null
}

fun JsonElement.getString(key: String) = jsonObject[key]!!.jsonPrimitive.content
fun JsonElement.getStringOrNull(key: String) = try {
    jsonObject[key]?.jsonPrimitive?.contentOrNull
} catch (e: Exception) {
    null
}

fun JsonElement.getInt(key: String) = jsonObject[key]!!.jsonPrimitive.int
fun JsonElement.getIntOrNull(key: String) = try {
    jsonObject[key]?.jsonPrimitive?.intOrNull
} catch (e: Exception) {
    null
}

fun JsonElement.getLong(key: String) = jsonObject[key]!!.jsonPrimitive.long
fun JsonElement.getLongOrNull(key: String) = try {
    jsonObject[key]?.jsonPrimitive?.longOrNull
} catch (e: Exception) {
    null
}

fun JsonElement.getDouble(key: String) = jsonObject[key]!!.jsonPrimitive.double
fun JsonElement.getDoubleOrNull(key: String) = try {
    jsonObject[key]?.jsonPrimitive?.doubleOrNull
} catch (e: Exception) {
    null
}

fun JsonElement.getFloat(key: String) = jsonObject[key]!!.jsonPrimitive.float
fun JsonElement.getFloatOrNull(key: String) = try {
    jsonObject[key]?.jsonPrimitive?.floatOrNull
} catch (e: Exception) {
    null
}

fun JsonElement.getBoolean(key: String) = jsonObject[key]!!.jsonPrimitive.boolean
fun JsonElement.getBooleanOrNull(key: String) = try {
    jsonObject[key]?.jsonPrimitive?.booleanOrNull
} catch (e: Exception) {
    null
}

fun JsonElement.getPrimitive(key: String) = jsonObject[key]!!.jsonPrimitive
fun JsonElement.getPrimitiveOrNull(key: String) = try {
    jsonObject[key]?.jsonPrimitive
} catch (e: Exception) {
    null
}

fun JsonElement.getObject(key: String) = jsonObject[key]!!.jsonObject
fun JsonElement.getObjectOrNull(key: String) = try {
    jsonObject[key]?.jsonObject
} catch (e: Exception) {
    null
}

fun JsonElement.getArray(key: String) = jsonObject[key]!!.jsonArray
fun JsonElement.getArrayOrNull(key: String) = try {
    jsonObject[key]?.jsonArray
} catch (e: Exception) {
    null
}

fun JsonElement.getObjectArray(key: String) = jsonObject[key]!!.jsonArray.map { it.jsonObject }
fun JsonElement.getPrimitiveArray(key: String) = jsonObject[key]!!.jsonArray.map { it.jsonPrimitive }
fun JsonElement.getArrayArray(key: String) = jsonObject[key]!!.jsonArray.map { it.jsonArray }

fun JsonElement.getObjectArrayOrNull(key: String) = try {
    jsonObject[key]?.jsonArray?.map { it.jsonObject }
} catch (e: Exception) {
    null
}

fun JsonElement.getPrimitiveArrayOrNull(key: String) = try {
    jsonObject[key]?.jsonArray?.map { it.jsonPrimitive }
} catch (e: Exception) {
    null
}

fun JsonElement.getArrayArrayOrNull(key: String) = try {
    jsonObject[key]?.jsonArray?.map { it.jsonArray }
} catch (e: Exception) {
    null
}