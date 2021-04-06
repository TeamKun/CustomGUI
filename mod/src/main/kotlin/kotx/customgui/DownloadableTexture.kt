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

    override fun load(manager: IResourceManager) {
        if (location == null) {
            super.load(manager)
        }

        releaseId()
        val texId = getId()
        val inputStream = ByteArrayInputStream(cacheFile.inputStream().readBytes())
        val image = NativeImage.read(inputStream)
        TextureUtil.prepareImage(texId, 0, image.width, image.height)
        image.upload(0, 0, 0, 0, 0, image.width, image.height, blur, false, false, true)
    }
}
