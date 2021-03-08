package kotx.minecraft.plugins.customgui.directory

import kotx.minecraft.plugins.customgui.extensions.allFiles
import java.io.File
import java.util.*
import kotlin.concurrent.timerTask

/**
 * TabCompleteでずっとFile#allFilesを使うと鯖が重くなる可能性がある為5秒に一回、もしくはファイル操作時のみ更新するように
 */

class DirectoryHandler(
    private val baseDir: File
) {
    var files = baseDir.allFiles().toMutableList()
        private set

    init {
        Timer().schedule(timerTask {
            files = baseDir.allFiles().toMutableList()
        }, 0, 5000)
    }

    fun get(path: String) = File("${baseDir.absolutePath}/${path.replace("\\", "/")}")

    fun write(path: String, content: String) {
        get(path).apply {
            if (!parentFile.exists())
                parentFile.mkdirs()
            if (!exists())
                createNewFile().also { files.add(this) }

            writeText(content)
        }
    }

    fun read(path: String) = get(path).readText()
}