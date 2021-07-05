package kotx.customgui.view.renderers

import io.ktor.client.*
import io.ktor.client.engine.okhttp.*
import io.ktor.client.features.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.coroutines.*
import kotx.customgui.util.*
import kotx.customgui.view.views.*
import net.minecraft.client.renderer.texture.*
import net.minecraft.resources.*
import net.minecraft.util.*
import org.apache.commons.io.*
import java.io.*

class ImageViewTexture(
    val view: ImageView,
    resourceLocation: ResourceLocation
) : SimpleTexture(resourceLocation), CoroutineScope {
    override val coroutineContext = Dispatchers.Default

    private val client = HttpClient(OkHttp) {
        expectSuccess = false
        BrowserUserAgent()
        defaultRequest {
            accept(ContentType.Any)
            header("Accept-Encoding", "")
            header("Accept-Language", "ja,en-US;q=0.9,en;q=0.8")
        }
    }

    override fun loadTexture(manager: IResourceManager) {
        val cacheFile = File("./mods/CustomGUI/caches/${view.id}")
        cacheFile.parentFile.mkdirs()

        if (cacheFile.exists()) {
            loadImage(manager, cacheFile)
            return
        }

        launch {
            val bytes = client.get<HttpStatement>(view.url).receive<InputStream>().readBytes()
            FileUtils.copyInputStreamToFile(bytes.inputStream(), cacheFile)

            MainThreadExecutor.offer {
                loadImage(manager, cacheFile)
            }
        }
    }

    private fun loadImage(manager: IResourceManager, cacheFile: File) {
        if (textureLocation == null) {
            super.loadTexture(manager)
        }

        deleteGlTexture()

        val inputStream = cacheFile.inputStream().readBytes().inputStream()
        val image = NativeImage.read(inputStream)

        TextureUtil.prepareImage(getGlTextureId(), 0, image.width, image.height)
        image.uploadTextureSub(
            0,
            0,
            0,
            0,
            0,
            image.width,
            image.height,
            blur,
            false,
            false,
            true
        )

        view.isLoading = false
        view.isLoaded = true
    }
}