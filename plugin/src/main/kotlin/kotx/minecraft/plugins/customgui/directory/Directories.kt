package kotx.minecraft.plugins.customgui.directory

import java.nio.file.Paths

object Directories {
    val guis = DirectoryHandler(Paths.get("plugins", "CustomGUI", "gui").toFile())
}