package kotx.customgui.view.renderers

import com.mojang.blaze3d.matrix.*
import com.mojang.blaze3d.systems.*
import kotx.customgui.*
import kotx.customgui.gui.*
import kotx.customgui.util.*
import kotx.customgui.view.*
import kotx.customgui.view.views.*
import net.minecraft.client.*
import net.minecraft.client.renderer.*
import net.minecraft.client.renderer.vertex.*
import net.minecraft.util.*
import org.lwjgl.opengl.*
import java.awt.*

class ImageViewRenderer : ViewRenderer<ImageView> {
    override fun renderPreview(stack: MatrixStack, x1: Int, y1: Int, x2: Int, y2: Int, view: ImageView) {
        renderImage(view, stack, x1, y1, x2, y2)
        GUI.textShadow(stack, view.url, x1, y2, Color.WHITE)
    }

    override fun renderFull(stack: MatrixStack, x1: Int, y1: Int, x2: Int, y2: Int, view: ImageView) {
        renderImage(view, stack, x1, y1, x2, y2)
    }

    private fun renderImage(view: ImageView, stack: MatrixStack, x1: Int, y1: Int, x2: Int, y2: Int) {
        load(view)
        if (view.isLoading) {
            GUI.rect(stack, x1, y1, x2, y2, Color(255, 255, 255, 100))
        }

        if (view.isLoaded) {
            val z = 0.0
            val x1 = x1.toDouble()
            val x2 = x2.toDouble()
            val y1 = y1.toDouble()
            val y2 = y2.toDouble()
            val u1 = 0.0f
            val v1 = 0.0f
            val u2 = 1.0f
            val v2 = 1.0f
            val bufferBuilder = Tessellator.getInstance().buffer
            val resourceLocation = ResourceLocation(CustomGUIMod.MOD_ID, "textures/images/${view.id}")
            Minecraft.getInstance().textureManager.bindTexture(resourceLocation)
            RenderSystem.enableBlend()
            RenderSystem.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA)
            RenderSystem.alphaFunc(516, 0.01f)
            RenderSystem.color4f(1f, 1f, 1f, 1f)
            bufferBuilder.begin(7, DefaultVertexFormats.POSITION_TEX)
            bufferBuilder.pos(x1, y2, z).tex(u1, v2).endVertex()
            bufferBuilder.pos(x2, y2, z).tex(u2, v2).endVertex()
            bufferBuilder.pos(x2, y1, z).tex(u2, v1).endVertex()
            bufferBuilder.pos(x1, y1, z).tex(u1, v1).endVertex()
            bufferBuilder.finishDrawing()
            WorldVertexBufferUploader.draw(bufferBuilder)
        }
    }

    private fun load(view: ImageView) {
        if (!view.isLoaded && !view.isLoading) {
            view.isLoading = true
            val resourceLocation = ResourceLocation(CustomGUIMod.MOD_ID, "textures/images/${view.id}")
            mc.textureManager.loadTexture(resourceLocation, ImageViewTexture(view, resourceLocation))
        }
    }
}