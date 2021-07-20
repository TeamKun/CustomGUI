package kotx.customgui.view.creators

import com.mojang.blaze3d.matrix.MatrixStack
import io.ktor.client.*
import io.ktor.client.engine.okhttp.*
import io.ktor.client.features.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeout
import kotx.customgui.gui.guis.editor.EditorGUI
import kotx.customgui.util.component
import kotx.customgui.util.fontRenderer
import kotx.customgui.view.ViewCreator
import kotx.customgui.view.ViewType
import kotx.customgui.view.holders.ImageViewHolder
import kotx.customgui.view.views.ImageView
import net.minecraft.client.gui.widget.TextFieldWidget
import net.minecraft.client.gui.widget.button.Button
import org.apache.commons.io.FileUtils
import org.lwjgl.glfw.GLFW
import java.awt.Color
import java.io.File
import java.util.*
import javax.imageio.ImageIO
import kotlin.concurrent.schedule

class ImageViewCreator : ViewCreator<ImageView, ImageViewHolder>(), CoroutineScope {
    override val coroutineContext = Dispatchers.Default

    override var initView: ImageViewHolder? = null

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
    private var success = true
    private var timer = Timer()

    override fun initialize() {
        textField = textFieldCenter("URL", width / 2, 70, 200, fontRenderer.FONT_HEIGHT + 11).apply {
            setCanLoseFocus(true)
            setMaxStringLength(1024)
        }

        if (initView != null)
            textField.text = initView!!.content.url

        button = buttonCenter("   作成   ", width / 2, 110) {
            handle()
        }

        val urlRegex =
            "((([A-Za-z]{3,9}:(?:\\/\\/)?)(?:[-;:&=\\+\\\$,\\w]+@)?[A-Za-z0-9.-]+|(?:www.|[-;:&=\\+\\\$,\\w]+@)[A-Za-z0-9.-]+)((?:\\/[\\+~%\\/.\\w\\-_]*)?\\??(?:[-\\+=&;%@.\\w_]*)#?(?:[.\\!\\/\\\\w]*))?)".toRegex(
                RegexOption.IGNORE_CASE
            )
        textField.setResponder {
            button.active = urlRegex.matches(it)
        }

        button.active = false

        handler.setFocusedDefault(textField)
    }

    private fun Button.handle() {
        message = "ロード中...".component()
        active = false

        launch {
            try {
                withTimeout(5000) {
                    val bytes = client.get<ByteArray>(textField.text) {
                        onDownload { bytesSentTotal, contentLength ->
                            val percentage = ((bytesSentTotal.toDouble() / contentLength.toDouble()) * 100).toInt()
                            message = if (percentage in 0..100)
                                "ロード中... ($percentage%)".component()
                            else
                                "ロード中...".component()
                        }
                    }
                    val image = ImageIO.read(bytes.copyOf().inputStream())

                    val id = UUID.randomUUID().toString().replace("-", "")

                    val cacheFile = File("./mods/CustomGUI/caches/$id")
                    cacheFile.parentFile.mkdirs()

                    FileUtils.copyInputStreamToFile(bytes.copyOf().inputStream(), cacheFile)

                    x2 = x1 + image.width
                    y2 = y1 + image.height

                    if (initView != null) {
                        EditorGUI.holders.removeIf { it.index == initView!!.index }
                        x2 = initView!!.content.x2
                        y2 = initView!!.content.y2
                    }

                    build(
                        ImageView(
                            id,
                            textField.text
                        )
                    )
                }
            } catch (e: Exception) {
                message = "作成".component()
                success = false
                timer.cancel()
                timer = Timer()
                timer.schedule(5000) {
                    success = true
                }
                active = true
            }
        }
    }

    override fun onKeyPress(key: Int, modifiers: Int): Boolean {
        if (key == GLFW.GLFW_KEY_ENTER && textField.text.isNotBlank())
            button.handle()

        return super.onKeyPress(key, modifiers)
    }

    override fun draw(stack: MatrixStack, mouseX: Int, mouseY: Int) {
        rect(stack, 0, 0, width, height, Color(0, 0, 0, 100))
        if (success) {
            textCenter(stack, "画像のURLを入力", width / 2, 30, Color.WHITE)
        } else {
            textCenter(stack, "画像の読み込みに失敗しました。", width / 2, 30, Color.RED)
        }
    }
}