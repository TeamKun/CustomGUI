package kotx.customgui.view.creators

import com.mojang.blaze3d.matrix.MatrixStack
import io.ktor.client.*
import io.ktor.client.engine.okhttp.*
import io.ktor.client.features.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotx.customgui.util.component
import kotx.customgui.util.fontRenderer
import kotx.customgui.view.ViewCreator
import kotx.customgui.view.ViewType
import kotx.customgui.view.views.ImageView
import net.minecraft.client.gui.widget.TextFieldWidget
import net.minecraft.client.gui.widget.button.Button
import org.apache.commons.io.FileUtils
import org.lwjgl.glfw.GLFW
import java.awt.Color
import java.io.File
import java.io.InputStream
import java.util.*
import javax.imageio.ImageIO

class ImageViewCreator : ViewCreator<ImageView>(), CoroutineScope {
    override val coroutineContext = Dispatchers.Default

    override val type: ViewType = ViewType.IMAGE
    override val points: Int = 1

    private val client = HttpClient(OkHttp) {
        expectSuccess = false
        BrowserUserAgent()
        defaultRequest {
            accept(ContentType.Any)
            header("Accept-Encoding", "")
            header("Accept-Language", "ja,en-US;q=0.9,en;q=0.8")
        }
    }

    private lateinit var textField: TextFieldWidget
    private lateinit var button: Button

    override fun initialize() {
        textField = textFieldCenter("URL", width / 2, 50, 200, fontRenderer.FONT_HEIGHT + 11).apply {
            setCanLoseFocus(true)
            setMaxStringLength(1024)
            setFocused2(true)
        }

        button = buttonCenter("作成", width / 2, 100) {
            handle()
        }

        textField.setResponder {
            button.active = it.isNotBlank()
        }

        button.active = false
    }

    private fun Button.handle() {
        message = "ロード中...".component()
        active = false

        launch {
            val bytes = client.get<HttpStatement>(textField.text).receive<InputStream>().readBytes()
            val image = ImageIO.read(bytes.inputStream())

            val id = UUID.randomUUID().toString().replace("-", "")

            val cacheFile = File("./mods/CustomGUI/caches/$id")
            cacheFile.parentFile.mkdirs()

            FileUtils.copyInputStreamToFile(bytes.inputStream(), cacheFile)

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

    override fun onKeyPress(key: Int, modifiers: Int): Boolean {
        if (key == GLFW.GLFW_KEY_ENTER && textField.text.isNotBlank())
            button.handle()

        return true
    }

    override fun draw(stack: MatrixStack, mouseX: Int, mouseY: Int) {
        rect(stack, 0, 0, width, height, Color(0, 0, 0, 100))
    }
}