package kotx.customgui.gui.component.view

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.mojang.blaze3d.systems.RenderSystem
import io.ktor.client.*
import io.ktor.client.engine.okhttp.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.coroutines.runBlocking
import kotx.customgui.*
import kotx.customgui.gui.View
import kotx.ktools.toJson
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.Tessellator
import net.minecraft.client.renderer.WorldVertexBufferUploader
import net.minecraft.client.renderer.vertex.DefaultVertexFormats
import net.minecraft.util.ResourceLocation
import org.apache.commons.io.FileUtils
import java.awt.Color
import java.nio.file.Paths
import java.util.concurrent.CompletableFuture


@JsonIgnoreProperties("resLoc", "tex", "width", "height", "completed", "client")
class ImageView : View {
    var url: String = ""
    override val type: String = "image"
    override var startX: Int = 0
    override var startY: Int = 0
    override var endX: Int = 0
    override var endY: Int = 0
    override val canResize = true
    var resId = ""
    val resLoc = ResourceLocation(CustomGUIMod.modId, "textures/images/$resId.png")
    var tex: DownloadableTexture? = null
    var client = HttpClient(OkHttp)
    var completed = false


    override fun init() {
        val cacheFile = Paths.get("mods", "CustomGUI", "caches", "$resId.png").toFile()
        completed = false
        CompletableFuture.runAsync({
            runBlocking {
                if (!cacheFile.exists()) {
                    val response = client.get<HttpStatement>(url) {
                        accept(ContentType.Any)
                        header(
                            "User-Agent",
                            "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.80 Safari/537.36"
                        )
                        header("Accept-Encoding", "")
                        header("Accept-Language", "ja,en-US;q=0.9,en;q=0.8")
                    }

                    FileUtils.copyInputStreamToFile(response.receive(), cacheFile)
                }

                tex = DownloadableTexture(resLoc, cacheFile)
                Minecraft.getInstance().textureManager.loadTexture(
                    resLoc,
                    tex
                )
                completed = true
            }
        }, ServerThreadExecutor)
    }

    override fun renderPreview(mouseX: Int, mouseY: Int) {
        val stX = xCenter + startX
        val stY = yCenter + startY
        val enX = xCenter + endX
        val enY = yCenter + endY

        val x1 = stX.toDouble()
        val y1 = stY.toDouble()
        val x2 = enX.toDouble()
        val y2 = enY.toDouble()
        val z = 0.0
        val u1 = 0.0f
        val v1 = 0.0f
        val u2 = 1.0f
        val v2 = 1.0f

        if (completed) {
            val bufferBuilder = Tessellator.getInstance().buffer
            Minecraft.getInstance().renderManager.textureManager.bindTexture(resLoc)
            RenderSystem.enableBlend()
            bufferBuilder.begin(7, DefaultVertexFormats.POSITION_TEX)
            bufferBuilder.pos(x1, y2, z).tex(u1, v2).endVertex()
            bufferBuilder.pos(x2, y2, z).tex(u2, v2).endVertex()
            bufferBuilder.pos(x2, y1, z).tex(u2, v1).endVertex()
            bufferBuilder.pos(x1, y1, z).tex(u1, v1).endVertex()
            bufferBuilder.finishDrawing()
            WorldVertexBufferUploader.draw(bufferBuilder)
        } else {
            fillAbsolute(stX, stY, enX, enY, Color(255, 255, 255, 50))
        }

        Minecraft.getInstance().fontRenderer.drawString(url, stX, enY, Color.WHITE, true)
    }

    override fun renderPage(scaleW: Float, scaleH: Float, opacity: Float) {
        val stX = xCenter + (startX * scaleW).toInt()
        val stY = yCenter + (startY * scaleH).toInt()
        val enX = xCenter + (endX * scaleW).toInt()
        val enY = yCenter + (endY * scaleH).toInt()

        val x1 = stX.toDouble()
        val y1 = stY.toDouble()
        val x2 = enX.toDouble()
        val y2 = enY.toDouble()
        val z = 0.0
        val u1 = 0.0f
        val v1 = 0.0f
        val u2 = 1.0f
        val v2 = 1.0f

        if (completed) {
            val bufferBuilder = Tessellator.getInstance().buffer
            Minecraft.getInstance().renderManager.textureManager.bindTexture(resLoc)
            RenderSystem.pushMatrix()
            RenderSystem.enableBlend()
            RenderSystem.color4f(1f, 1f, 1f, opacity)
            bufferBuilder.begin(7, DefaultVertexFormats.POSITION_TEX)
            bufferBuilder.pos(x1, y2, z).tex(u1, v2).endVertex()
            bufferBuilder.pos(x2, y2, z).tex(u2, v2).endVertex()
            bufferBuilder.pos(x2, y1, z).tex(u2, v1).endVertex()
            bufferBuilder.pos(x1, y1, z).tex(u1, v1).endVertex()
            bufferBuilder.finishDrawing()
            WorldVertexBufferUploader.draw(bufferBuilder)
            RenderSystem.popMatrix()
        } else {
            fillAbsolute(stX, stY, enX, enY, Color(255, 255, 255, 50))
        }
    }

    override fun onClick(mouseX: Int, mouseY: Int, button: Int) {

    }

    override fun parseToJson(): String = if (completed) this.toJson() else ""
}