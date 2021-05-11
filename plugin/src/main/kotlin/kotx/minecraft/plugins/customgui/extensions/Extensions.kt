package kotx.minecraft.plugins.customgui.extensions

import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta
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