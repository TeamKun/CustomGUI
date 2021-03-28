package kotx.customgui

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import net.minecraft.client.renderer.texture.NativeImage
import net.minecraft.client.renderer.texture.SimpleTexture
import net.minecraft.client.renderer.texture.TextureUtil
import net.minecraft.resources.IResourceManager
import net.minecraft.util.ResourceLocation
import java.io.ByteArrayInputStream
import java.io.File
import kotlin.coroutines.CoroutineContext

class DownloadableTexture(
    location: ResourceLocation,
    private val cacheFile: File,
) : SimpleTexture(location), CoroutineScope {
    override val coroutineContext: CoroutineContext = Dispatchers.Default


    override fun loadTexture(manager: IResourceManager) {
        first loadTexture ")
        if (textureLocation == null) {
            super.loadTexture(manager)
        }

        before deleteGlTexture ")
        deleteGlTexture()
        before getGlTextureId ")
        val texId = getGlTextureId()
        before ByteArrayInputStream ")
        val inputStream = ByteArrayInputStream(cacheFile.inputStream().readBytes())
        before NativeImage # read ")
        val image = NativeImage.read(inputStream)
        before prepareImage ")
        TextureUtil.prepareImage(texId, 0, image.width, image.height)
        before uploadTextureSub ")
        image.uploadTextureSub(0, 0, 0, 0, 0, image.width, image.height, blur, false, false, true)
        finished loadTexture ")
    }
}
