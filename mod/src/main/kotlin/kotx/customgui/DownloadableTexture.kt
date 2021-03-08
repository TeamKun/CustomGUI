package kotx.customgui

import io.ktor.client.*
import io.ktor.client.engine.okhttp.*
import io.ktor.client.features.observer.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import net.minecraft.client.renderer.texture.NativeImage
import net.minecraft.client.renderer.texture.SimpleTexture
import net.minecraft.client.renderer.texture.TextureUtil
import net.minecraft.resources.IResourceManager
import net.minecraft.util.ResourceLocation
import org.apache.commons.io.FileUtils
import java.io.ByteArrayInputStream
import java.io.File
import kotlin.coroutines.CoroutineContext

class DownloadableTexture(
    location: ResourceLocation,
    private val cacheFile: File,
    private val imageUrl: String,
) : SimpleTexture(location), CoroutineScope {
    override val coroutineContext: CoroutineContext = Dispatchers.Default
    private val client = HttpClient(OkHttp) {
        ResponseObserver {
            println(it.headers)
        }
    }


    override fun loadTexture(manager: IResourceManager) {
        if (textureLocation == null) {
            super.loadTexture(manager)
        }

        deleteGlTexture()
        val texId = getGlTextureId()
        val inputStream = ByteArrayInputStream(cacheFile.inputStream().readBytes())
        val image = NativeImage.read(inputStream)
        TextureUtil.prepareImage(texId, 0, image.width, image.height)
        image.uploadTextureSub(0, 0, 0, 0, 0, image.width, image.height, blur, false, false, true)
    }

    private suspend fun downloadTexture(): File {
        val response = client.get<HttpStatement>(imageUrl) {
            accept(ContentType.Any)
            header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.80 Safari/537.36")
            header("Accept-Encoding", "")
            header("Accept-Language", "ja,en-US;q=0.9,en;q=0.8")
        }

        FileUtils.copyInputStreamToFile(response.receive(), cacheFile)

        return cacheFile
    }
}