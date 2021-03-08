package kotx.ktools

import com.fasterxml.jackson.databind.ObjectMapper
import kotlinx.serialization.json.*

fun <T> T.toJson() = ObjectMapper().writeValueAsString(this)!!
inline fun <reified T> String.parseToObject() = ObjectMapper().readValue(this, T::class.java)!!

fun String.asJsonObject() = Json.parseToJsonElement(this).jsonObject
fun String.asJsonArray() = Json.parseToJsonElement(this).jsonArray
fun String.asJsonPrimitive() = Json.parseToJsonElement(this).jsonPrimitive
fun String.asJsonElement() = Json.parseToJsonElement(this)

fun JsonObject.isObject(key: String) = try {
    this[key]?.jsonObject != null
} catch (e: Exception) {
    false
}

fun JsonObject.getObject(key: String) = this[key]!!.jsonObject
fun JsonObject.getObjectOrNull(key: String) = this[key]?.jsonObject

fun JsonObject.getArray(key: String) = this[key]!!.jsonArray
fun JsonObject.getObjectArray(key: String) = this[key]!!.jsonArray.map { it.jsonObject }
fun JsonObject.getArrayArray(key: String) = this[key]!!.jsonArray.map { it.jsonArray }
fun JsonObject.getPrimitiveArray(key: String) = this[key]!!.jsonArray.map { it.jsonPrimitive }
fun JsonObject.getStringArray(key: String) = this[key]!!.jsonArray.map { it.jsonPrimitive.content }
fun JsonObject.getIntArray(key: String) = this[key]!!.jsonArray.map { it.jsonPrimitive.int }
fun JsonObject.getLongArray(key: String) = this[key]!!.jsonArray.map { it.jsonPrimitive.long }
fun JsonObject.getFloatArray(key: String) = this[key]!!.jsonArray.map { it.jsonPrimitive.float }
fun JsonObject.getDoubleArray(key: String) = this[key]!!.jsonArray.map { it.jsonPrimitive.double }
fun JsonObject.getArrayOrNull(key: String) = this[key]?.jsonArray

fun JsonObject.getPrimitive(key: String) = this[key]!!.jsonPrimitive
fun JsonObject.getPrimitiveOrNull(key: String) = this[key]?.jsonPrimitive

fun JsonObject.isString(key: String) = try {
    this[key]?.jsonPrimitive?.isString == true
} catch (e: Exception) {
    false
}

fun JsonObject.getString(key: String) = this[key]!!.jsonPrimitive.content
fun JsonObject.getStringOrNull(key: String) = this[key]?.jsonPrimitive?.contentOrNull
fun JsonObject.getStringOrDefault(key: String, default: String) = this[key]?.jsonPrimitive?.contentOrNull ?: default

fun JsonObject.isInt(key: String) = try {
    this[key]!!.jsonPrimitive.intOrNull != null
} catch (e: Exception) {
    false
}

fun JsonObject.getInt(key: String) = this[key]!!.jsonPrimitive.int
fun JsonObject.getIntOrNull(key: String) = this[key]?.jsonPrimitive?.intOrNull
fun JsonObject.getIntOrDefault(key: String, default: Int) = this[key]?.jsonPrimitive?.intOrNull ?: default

fun JsonObject.isLong(key: String) = try {
    this[key]!!.jsonPrimitive.longOrNull != null
} catch (e: Exception) {
    false
}

fun JsonObject.getLong(key: String) = this[key]!!.jsonPrimitive.long
fun JsonObject.getLongOrNull(key: String) = this[key]?.jsonPrimitive?.longOrNull
fun JsonObject.getLongOrDefault(key: String, default: Long) = this[key]?.jsonPrimitive?.longOrNull ?: default

fun JsonObject.isFloat(key: String) = try {
    this[key]!!.jsonPrimitive.floatOrNull != null
} catch (e: Exception) {
    false
}

fun JsonObject.getFloat(key: String) = this[key]!!.jsonPrimitive.float
fun JsonObject.getFloatOrNull(key: String) = this[key]?.jsonPrimitive?.floatOrNull
fun JsonObject.getFloatOrDefault(key: String, default: Float) = this[key]?.jsonPrimitive?.floatOrNull ?: default

fun JsonObject.isDouble(key: String) = try {
    this[key]!!.jsonPrimitive.doubleOrNull != null
} catch (e: Exception) {
    false
}

fun JsonObject.getDouble(key: String) = this[key]!!.jsonPrimitive.double
fun JsonObject.getDoubleOrDefault(key: String, default: Double) = this[key]?.jsonPrimitive?.doubleOrNull ?: default

fun JsonObject.isBoolean(key: String) = try {
    this[key]!!.jsonPrimitive.booleanOrNull != null
} catch (e: Exception) {
    false
}

fun JsonObject.getBoolean(key: String) = this[key]!!.jsonPrimitive.boolean
fun JsonObject.getBooleanOrNull(key: String) = this[key]?.jsonPrimitive?.booleanOrNull
fun JsonObject.getBooleanOrDefault(key: String, default: Boolean) = this[key]?.jsonPrimitive?.booleanOrNull ?: default

fun JsonArray.getObject(index: Int) = this[index].jsonObject
fun JsonArray.getArray(index: Int) = this[index].jsonArray
fun JsonArray.getPrimitive(index: Int) = this[index].jsonPrimitive

fun JsonArray.isString(index: Int) = try {
    this[index].jsonPrimitive.isString
} catch (e: Exception) {
    false
}

fun JsonArray.getString(index: Int) = this[index].jsonPrimitive.content
fun JsonArray.getStringOrNull(index: Int) = this[index].jsonPrimitive.contentOrNull

fun JsonArray.isInt(index: Int) = try {
    this[index].jsonPrimitive.intOrNull != null
} catch (e: Exception) {
    false
}

fun JsonArray.getInt(index: Int) = this[index].jsonPrimitive.int
fun JsonArray.getIntOrNull(index: Int) = this[index].jsonPrimitive.intOrNull

fun JsonArray.isLong(index: Int) = try {
    this[index].jsonPrimitive.longOrNull != null
} catch (e: Exception) {
    false
}

fun JsonArray.getLong(index: Int) = this[index].jsonPrimitive.long
fun JsonArray.getLongOrNull(index: Int) = this[index].jsonPrimitive.longOrNull

fun JsonArray.isFloat(index: Int) = try {
    this[index].jsonPrimitive.floatOrNull != null
} catch (e: Exception) {
    false
}

fun JsonArray.getFloat(index: Int) = this[index].jsonPrimitive.float
fun JsonArray.getFloatOrNull(index: Int) = this[index].jsonPrimitive.floatOrNull

fun JsonArray.isDouble(index: Int) = try {
    this[index].jsonPrimitive.doubleOrNull != null
} catch (e: Exception) {
    false
}

fun JsonArray.getDouble(index: Int) = this[index].jsonPrimitive.double
fun JsonArray.getDoubleOrNull(index: Int) = this[index].jsonPrimitive.doubleOrNull

fun JsonArray.isBoolean(index: Int) = try {
    this[index].jsonPrimitive.booleanOrNull != null
} catch (e: Exception) {
    false
}

fun JsonArray.getBoolean(index: Int) = this[index].jsonPrimitive.boolean
fun JsonArray.isBooleanOrNull(index: Int) = this[index].jsonPrimitive.booleanOrNull