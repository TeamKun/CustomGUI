package dev.kotx.customgui

import kotlinx.serialization.json.JsonArray

data class GUI(
    val author: String,
    val name: String,
    val views: JsonArray
)