package dev.kotx.customgui

import org.bukkit.plugin.java.JavaPlugin
import java.io.File

object Files {
    var guis = listOf<GUI>()
        private set

    private val guiDirectory = File("./plugins/CustomGUI/guis/")

    fun init(plugin: JavaPlugin) {
        plugin.server.scheduler.scheduleSyncRepeatingTask(plugin, {
            guiDirectory.allFiles().map {
                GUI(it.parentFile.name, it.nameWithoutExtension, it.readText().asJsonArray())
            }
        }, 0, 3000)
    }

    fun save(gui: GUI) {
        val guiFile = File(guiDirectory, "${gui.author}/${gui.name}.json")
        if (!guiFile.parentFile.exists())
            guiFile.parentFile.mkdirs()
        if (!guiFile.exists())
            guiFile.createNewFile()

        guiFile.writeText(gui.views.toString())
    }

    fun findByName(name: String) = guiDirectory.allFiles().find { it.nameWithoutExtension == name }?.let {
        GUI(it.parentFile.name, it.nameWithoutExtension, it.readText().asJsonArray())
    }

    private fun File.allFiles(): List<File> = listFiles().flatMap { if (it.isDirectory) it.allFiles() else listOf(it) }
}