package kotx.customgui.view.creators

import com.mojang.blaze3d.matrix.*
import io.ktor.client.*
import io.ktor.client.features.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import kotlinx.coroutines.*
import kotx.customgui.util.*
import kotx.customgui.view.*
import kotx.customgui.view.views.*
import org.apache.commons.io.*
import java.awt.*
import java.io.*
import java.util.*
import javax.imageio.*

class ImageViewCreator : ViewCreator<ImageView>(), CoroutineScope {
    override val coroutineContext = Dispatchers.Default

    override val type: ViewType = ViewType.IMAGE
    override val points: Int = 1

    private val client = HttpClient {
        expectSuccess = false
        BrowserUserAgent()
    }

    override fun initialize() {
        val textField = textFieldCenter("URL", width / 2, 50, 200, fontRenderer.FONT_HEIGHT + 11)

        val button = buttonCenter("作成", width / 2, 100) {
            message = "ロード中...".component()
            active = false

            launch {
                val stream = client.get<HttpStatement>(textField.text).receive<ByteArray>().inputStream()
                val image = ImageIO.read(stream)

                val id = UUID.randomUUID().toString().replace("-", "")

                val cacheFile = File("./mods/CustomGUI/caches/$id")
                cacheFile.parentFile.mkdirs()

                FileUtils.copyInputStreamToFile(stream, cacheFile)

                x2 = x1 + image.width
                y2 = y1 + image.height

                build(
                    ImageView(
                        id,
                        textField.text
                    )
                )
            }
        }

        textField.setFocused2(true)

        textField.setResponder {
            button.active = it.isNotBlank()
        }

        button.active = false
    }

    override fun draw(stack: MatrixStack, mouseX: Int, mouseY: Int) {
        rect(stack, 0, 0, width, height, Color(0, 0, 0, 100))
    }
}