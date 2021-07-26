package dev.kotx.customgui

import org.bukkit.plugin.java.JavaPlugin
import java.io.File
import java.util.*

object Files {
    var guis = listOf<GUI>()
        private set
        get() = field.toList()

    private val guiDirectory = File("./plugins/CustomGUI/guis/")

    fun init(plugin: JavaPlugin) {
        plugin.server.scheduler.scheduleSyncRepeatingTask(plugin, {
            guis = guiDirectory.allFiles().map {
                GUI(it.parentFile.name, it.nameWithoutExtension, it.readText().asJsonObject())
            }
        }, 0, 60)
    }

    fun save(gui: GUI) {
        val guiFile = File(guiDirectory, "${gui.author}/${gui.name}.json")
        if (!guiFile.parentFile.exists())
            guiFile.parentFile.mkdirs()
        if (!guiFile.exists())
            guiFile.createNewFile()

        guiFile.writeText(gui.data.toString())
    }

    fun findByName(name: String) = guis.find { it.name == name }
    fun filterByPlayer(uuid: UUID) = guis.filter { it.author == uuid.toString() }

    private fun File.allFiles(): List<File> =
        listFiles()?.flatMap { if (it.isDirectory) it.allFiles() else listOf(it) } ?: emptyList()
}