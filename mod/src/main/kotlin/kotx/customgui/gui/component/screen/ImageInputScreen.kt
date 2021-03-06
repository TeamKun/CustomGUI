package kotx.customgui.gui.component.screen

import com.mojang.blaze3d.matrix.*
import io.ktor.client.*
import io.ktor.client.engine.okhttp.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.coroutines.*
import kotx.customgui.*
import kotx.customgui.gui.*
import kotx.customgui.gui.component.view.*
import kotx.ktools.*
import net.minecraft.client.*
import net.minecraft.client.gui.screen.*
import net.minecraft.client.gui.widget.*
import net.minecraft.util.text.*
import net.minecraftforge.fml.client.gui.widget.*
import org.apache.commons.io.*
import org.lwjgl.glfw.*
import java.awt.Color
import java.io.*
import java.nio.file.*
import javax.imageio.*
import kotlin.coroutines.*
import kotlin.math.*

class ImageInputScreen(
    private val startX: Int,
    private val startY: Int
) : Screen(StringTextComponent("画像を選択")), CoroutineScope {
    override val coroutineContext: CoroutineContext = Dispatchers.Default

    private var url = ""
    private var widget: TextFieldWidget? = null
    private var confirmButton: ExtendedButton? = null
    private val fieldWidth = 320
    private val fieldHeight = 20

    override fun init() {
        url = ""

        confirmButton = ExtendedButton(
            xCenter - 100, scaledHeight - 70, 80, 20, StringTextComponent("確定")
        ) {
            minecraft?.displayGuiScreen(GuiDesignerScreen)
            postData()
        }
        addButton(confirmButton)
        addButton(ExtendedButton(
            xCenter + 20, scaledHeight - 70, 80, 20, StringTextComponent("キャンセル")
        ) {
            minecraft?.displayGuiScreen(GuiDesignerScreen)
        })

        widget = TextFieldWidget(
            font,
            xCenter - fieldWidth / 2,
            yCenter - fieldHeight / 2,
            fieldWidth,
            fieldHeight,
            StringTextComponent("画像のリンクを入力")
        ).apply {
            text = ""

            setCanLoseFocus(true)
            setMaxStringLength(1024)

            setResponder {
                confirmButton?.active = it.isUrl
                this@ImageInputScreen.url = it
            }
        }
        addButton(widget!!)

        super.init()
    }

    override fun render(stack: MatrixStack, mouseX: Int, mouseY: Int, partialTicks: Float) {
        fillAbsolute(stack, 0, 0, scaledWidth, scaledHeight, Color(0, 0, 0, 100))
        Minecraft.getInstance().fontRenderer.drawStringCentered(stack, "画像のリンクを入力", width / 2, 60, Color.WHITE)
        super.render(stack, mouseX, mouseY, partialTicks)
    }

    override fun keyPressed(p_keyPressed_1_: Int, p_keyPressed_2_: Int, p_keyPressed_3_: Int): Boolean {
        if (p_keyPressed_1_ == GLFW.GLFW_KEY_ESCAPE) {
            minecraft?.displayGuiScreen(GuiDesignerScreen)
            return false
        }

        if (p_keyPressed_1_ == GLFW.GLFW_KEY_ENTER) {
            if (url.isUrl) {
                minecraft?.displayGuiScreen(GuiDesignerScreen)
                postData()
            }
            return false
        }

        return super.keyPressed(p_keyPressed_1_, p_keyPressed_2_, p_keyPressed_3_)
    }

    private val client = HttpClient(OkHttp)
    private fun postData() {
        launch {
            val imageId = randomUUID()
            val response = runBlocking {
                client.get<HttpStatement>(url) {
                    accept(ContentType.Any)
                    header(
                        "User-Agent",
                        "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.80 Safari/537.36"
                    )
                    header("Accept-Encoding", "")
                    header("Accept-Language", "ja,en-US;q=0.9,en;q=0.8")
                }
            }
            val bytes = response.receive<InputStream>().readBytes()

            FileUtils.copyInputStreamToFile(
                bytes.inputStream(),
                Paths.get("mods", "CustomGUI", "caches", "$imageId.png").toFile()
            )
            val img = ImageIO.read(bytes.inputStream())

            GuiDesignerScreen.views.add(
                ImageView().apply {
                    this.startX = this@ImageInputScreen.startX
                    this.startY = this@ImageInputScreen.startY

                    var stretchX = 1f
                    if (GuiDesignerScreen.guiWidth < img.width + startX)
                        stretchX = (img.width.toFloat() + startX) / GuiDesignerScreen.guiWidth

                    var stretchY = 1f
                    if (GuiDesignerScreen.guiHeight < img.height + startY)
                        stretchY = (img.height.toFloat() + startY) / GuiDesignerScreen.guiHeight

                    val stretch = max(stretchX, stretchY)
                    this.endX = this.startX + (img.width / stretch).toInt()
                    this.endY = this.startY + (img.height / stretch).toInt()
                    this.url = this@ImageInputScreen.url
                    this.resId = imageId
                }.apply {
                    init()
                }
            )
        }
    }
}