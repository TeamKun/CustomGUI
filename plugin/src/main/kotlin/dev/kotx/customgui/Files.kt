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
                GUI(it.parentFile.name, it.name, it.readText().asJsonArray())
            }
        }, 0, 3000)
    }

    private fun File.allFiles(): List<File> = listFiles().flatMap { if (it.isDirectory) it.allFiles() else listOf(it) }
}