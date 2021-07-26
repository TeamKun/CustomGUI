package dev.kotx.customgui

import kotlinx.serialization.json.JsonObject

data class GUI(
    val author: String,
    val name: String,
    val data: JsonObject
)