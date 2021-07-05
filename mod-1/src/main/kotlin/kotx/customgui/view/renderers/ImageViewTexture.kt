package kotx.customgui.view.renderers

import io.ktor.client.*
import io.ktor.client.features.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
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

    private val client = HttpClient {
        expectSuccess = false
        BrowserUserAgent()
    }

    override fun loadTexture(manager: IResourceManager) {
        val cacheFile = File("./mods/CustomGUI/caches/${view.id}")
        cacheFile.parentFile.mkdirs()

        if (cacheFile.exists()) {
            loadImage(cacheFile)
            return
        }

        launch {
            FileUtils.copyInputStreamToFile(client.get<HttpStatement>(view.url).receive(), cacheFile)

            MainThreadExecutor.offer {
                loadImage(cacheFile)
            }
        }
    }

    private fun loadImage(cacheFile: File) {
        TextureUtil.prepareImage(getGlTextureId(), 0, view.width, view.height)
        NativeImage.read(cacheFile.inputStream()).uploadTextureSub(
            0,
            0,
            0,
            0,
            0,
            view.width,
            view.height,
            blur,
            false,
            false,
            false
        )

        view.isLoading = false
        view.isLoaded = true
    }
}