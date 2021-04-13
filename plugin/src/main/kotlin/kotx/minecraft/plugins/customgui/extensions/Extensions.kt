package kotx.minecraft.plugins.customgui.extensions

import kotx.minecraft.plugins.customgui.extensions.suggest.SuggestOption
import kotx.minecraft.plugins.customgui.extensions.suggest.SuggestOptionType
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta
import org.bukkit.plugin.java.JavaPlugin
import java.io.File
import java.nio.file.Files
import java.nio.file.Path
import kotlin.streams.toList

val Material.itemStack: ItemStack
    get() = ItemStack(this, 1)

fun Material.itemStack(block: ItemStack.() -> Unit) = ItemStack(this, 1).apply(block)

fun ItemStack.itemMeta(block: ItemMeta.() -> Unit): ItemStack {
    itemMeta = itemMeta.apply(block)
    return this
}

fun Path.allFiles(): List<Path> {
    return if (toFile().isDirectory)
        Files.list(this).toList().flatMap { it.allFiles() }
    else
        listOf(this)
}

fun File.allFiles(): List<File> {
    return if (isDirectory)
        listFiles().toList().flatMap { it.allFiles() }
    else
        listOf(this)
}

fun <T> List<T>.joint(other: T): List<T> {
    val res = mutableListOf<T>()
    forEachIndexed { i, it ->
        res.add(it)
        if (i < size - 1)
            res.add(other)
    }

    return res.toList()
}

fun <T> List<T>.joint(other: (T) -> T): List<T> {
    val res = mutableListOf<T>()
    forEachIndexed { i, it ->
        res.add(it)
        if (i < size - 1)
            res.add(other(it))
    }

    return res.toList()
}

private val suggestOptions = listOf(
    SuggestOption("advancements", SuggestOptionType.TEXT) { _, _ -> listOf("{}") },
    SuggestOption("distance", SuggestOptionType.RANGE) { _, s ->
        if (s?.toIntOrNull() ?: 0 > 0)
            listOf(s + "0", s + "00")
        else
            emptyList()
    },
    SuggestOption("dx", SuggestOptionType.NUMBER) { _, s ->
        if (s?.toIntOrNull() ?: 0 > 0)
            listOf(s + "0", s + "00")
        else
            emptyList()
    },
    SuggestOption("dy", SuggestOptionType.NUMBER) { _, s ->
        if (s?.toIntOrNull() ?: 0 > 0)
            listOf(s + "0", s + "00")
        else
            emptyList()
    },
    SuggestOption("dz", SuggestOptionType.NUMBER) { _, s ->
        if (s?.toIntOrNull() ?: 0 > 0)
            listOf(s + "0", s + "00")
        else
            emptyList()
    },
    SuggestOption("gamemode", SuggestOptionType.GAMEMODE) { _, _ ->
        listOf(
            "survival",
            "creative",
            "adventure",
            "spectator"
        )
    },
    SuggestOption("level", SuggestOptionType.RANGE) { p, _ ->
        p.server.onlinePlayers.map { it.level.toString() }
    },
    SuggestOption("limit", SuggestOptionType.NUMBER) { p, s ->
        listOf((p.server.onlinePlayers.size).toString(), (p.server.onlinePlayers.size / 2).toString())
    },
    SuggestOption("name", SuggestOptionType.TEXT) { p, _ ->
        p.server.onlinePlayers.map { it.name }
    },
    SuggestOption("nbt", SuggestOptionType.TEXT) { _, _ -> listOf("{}") },
    SuggestOption("predicate", SuggestOptionType.TEXT),
    SuggestOption("scores", SuggestOptionType.TEXT),
    SuggestOption("sort", SuggestOptionType.RANGE) { _, _ ->
        listOf("nearest", "furthest", "random", "arbitrary")
    },
    SuggestOption("tags", SuggestOptionType.TEXT),
    SuggestOption("team", SuggestOptionType.TEXT) { _, _ ->
        Bukkit.getScoreboardManager().mainScoreboard.teams.map { it.name }
    },
    SuggestOption("x", SuggestOptionType.NUMBER) { _, s ->
        if (s?.toIntOrNull() ?: 0 > 0)
            listOf(s + "0", s + "00")
        else
            emptyList()
    },
    SuggestOption("x_rotation", SuggestOptionType.RANGE) { _, s ->
        if (s?.toIntOrNull() ?: 0 in -9..9)
            listOf(s + "0", "-" + s + "0")
        else
            emptyList()
    },
    SuggestOption("y", SuggestOptionType.NUMBER) { _, s ->
        if (s?.toIntOrNull() ?: 0 > 0)
            listOf(s + "0", s + "00")
        else
            emptyList()
    },
    SuggestOption("y_rotation", SuggestOptionType.RANGE) { _, s ->
        if (s?.toIntOrNull() ?: 0 in -9..9)
            listOf(s + "0", "-" + s + "0")
        else
            emptyList()
    },
    SuggestOption("z", SuggestOptionType.NUMBER) { _, s ->
        if (s?.toIntOrNull() ?: 0 > 0)
            listOf(s + "0", s + "00")
        else
            emptyList()
    },
    SuggestOption("z_rotation", SuggestOptionType.RANGE) { _, s ->
        if (s?.toIntOrNull() ?: 0 in -9..9)
            listOf(s + "0", "-" + s + "0")
        else
            emptyList()
    },
)
private val selectors = listOf("@a", "@e", "@s", "@r")

fun suggestEntities(input: String, plugin: JavaPlugin): MutableList<String> {
    val suggestions = mutableListOf<String>()

    if (input.isBlank()) {
        suggestions.addAll(plugin.server.onlinePlayers.map { it.name }.toMutableList())
        suggestions.addAll(selectors)

        return suggestions.map { "$input$it" }.toMutableList()
    }

    if (input == "@") {
        suggestions.add("a")
        suggestions.add("e")
        suggestions.add("s")
        suggestions.add("r")

        return suggestions.map { "$input$it" }.toMutableList()
    }

    if (selectors.contains(input.toLowerCase())) {
        suggestions.add("[]")
        return suggestions.map { "$input$it" }.toMutableList()
    }

    if (selectors.any { input.toLowerCase().startsWith("$it[") && input.endsWith("]") }) {
        return mutableListOf()
    }

    val selector = selectors.find { input.toLowerCase().startsWith("$it[") }
    if (selector != null) {
        val option = input.replace("$selector[", "").run {
            if (lastOrNull() == ']')
                substring(0 until length)
            else
                this
        }

        val currentOption = option.split(",").last()
        val currentOptionKey = currentOption.split("=").getOrNull(0)
        val currentOptionValue = currentOption.split("=").getOrNull(1)
        val currentOptionInfo = suggestOptions.find { it.name == currentOptionKey }

        when {
            currentOption.split("=").size == 1 -> {
                val options = suggestOptions.filter { it.name.startsWith(currentOption, true) }
                if (options.size == 1) {
                    suggestions.addAll(options.first().defaultValues(plugin, null).map {
                        "${options.first().name.drop(currentOption.length)}=${it}"
                    })
                }
                suggestions.addAll(options.map { "${it.name.drop(currentOption.length)}=" })
            }
            currentOptionInfo != null && currentOptionValue.isNullOrEmpty() -> suggestions.addAll(
                currentOptionInfo.defaultValues(
                    plugin,
                    null
                )
            )
            currentOptionInfo != null && !currentOptionValue.isNullOrEmpty() -> suggestions.addAll(
                currentOptionInfo.defaultValues(
                    plugin,
                    currentOptionValue
                ).filter { it.startsWith(currentOptionValue, true) }
                    .map { it.drop(currentOptionValue.length) })
        }
    } else {
        suggestions.addAll(plugin.server.onlinePlayers.map { it.name }.filter { it.startsWith(input, true) }
            .map { it.drop(input.length) })
    }
    return suggestions.map { "$input$it" }.toMutableList()
}