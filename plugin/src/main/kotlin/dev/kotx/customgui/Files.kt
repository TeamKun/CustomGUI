package dev.kotx.customgui

import java.io.File
import java.util.*

object Files {
    var guis = mutableListOf<GUI>()
        private set

    private val guiDirectory = File("./plugins/CustomGUI/guis/")

    fun init() {
        guis = guiDirectory.allFiles().map {
            GUI(it.parentFile.name, it.nameWithoutExtension, it.readText().asJsonObject())
        }.toMutableList()
    }

    fun save(gui: GUI) {
        val guiFile = File(guiDirectory, "${gui.author}/${gui.name}.json")
        if (!guiFile.parentFile.exists())
            guiFile.parentFile.mkdirs()
        if (!guiFile.exists())
            guiFile.createNewFile()

        guis.removeIf { it.name == gui.name }
        guis.add(gui)

        guiFile.writeText(gui.data.toString())
    }

    fun findByName(name: String) = guis.find { it.name == name }
    fun filterByPlayer(uuid: UUID) = guis.filter { it.author == uuid.toString() }
    fun deleteByName(name: String) {
        val gui = guis.find { it.name == name } ?: return
        guis.remove(gui)

        val guiFile = File(guiDirectory, "${gui.author}/${gui.name}.json")
        if (guiFile.exists())
            guiFile.delete()
    }

    private fun File.allFiles(): List<File> =
        listFiles()?.flatMap { if (it.isDirectory) it.allFiles() else listOf(it) } ?: emptyList()
}