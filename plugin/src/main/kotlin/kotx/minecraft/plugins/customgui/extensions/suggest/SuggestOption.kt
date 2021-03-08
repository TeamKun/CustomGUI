package kotx.minecraft.plugins.customgui.extensions.suggest

import org.bukkit.plugin.java.JavaPlugin

class SuggestOption(
    val name: String,
    val valueType: SuggestOptionType,
    val defaultValues: (JavaPlugin, String?) -> List<String> = { _, _ -> emptyList() }
)

enum class SuggestOptionType(
    val isValid: (String) -> Boolean
) {
    NUMBER(
        {
            it.toDoubleOrNull() != null
        }
    ),

    RANGE(
        {
            it.matches("[0-9]+".toRegex())
                    || it.matches("[0-9]+\\.\\.[0-9]+".toRegex())
                    || it.matches("\\.\\.[0-9]+".toRegex())
                    || it.matches("[0-9]+\\.\\.".toRegex())
        }
    ),

    TEXT({
        true
    }),

    GAMEMODE({ s ->
        listOf("survival", "creative", "adventure", "spectator").any { s.equals(it, true) || "!$s".equals(it, true) }
    }),

    SORT({ s ->
        listOf("nearest", "furthest", "random", "arbitrary").any { s.equals(it, true) }
    })
}