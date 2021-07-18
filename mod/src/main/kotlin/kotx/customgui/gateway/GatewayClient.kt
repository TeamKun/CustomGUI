@file:Suppress("INACCESSIBLE_TYPE")

package kotx.customgui.gateway

import kotlinx.serialization.json.JsonObject
import kotx.customgui.CustomGUIMod
import kotx.customgui.gateway.handlers.LoadGUIHandler
import kotx.customgui.gateway.handlers.SaveGUIHandler
import kotx.customgui.gateway.handlers.ShowGUIHandler
import kotx.customgui.util.asJsonObject
import kotx.customgui.util.getInt
import kotx.customgui.util.getObject
import kotx.customgui.util.json
import net.minecraft.network.PacketBuffer
import net.minecraft.util.ResourceLocation
import net.minecraftforge.fml.network.NetworkEvent
import net.minecraftforge.fml.network.NetworkRegistry
import org.apache.commons.lang3.ArrayUtils
import java.util.function.BiConsumer
import java.util.function.Function
import java.util.function.Supplier

class GatewayClient {
    private val channel = NetworkRegistry.ChannelBuilder.named(ResourceLocation(CustomGUIMod.MOD_ID, "messenger"))
        .clientAcceptedVersions(NetworkRegistry.ACCEPTVANILLA::equals)
        .serverAcceptedVersions(NetworkRegistry.ACCEPTVANILLA::equals)
        .networkProtocolVersion { NetworkRegistry.ACCEPTVANILLA }
        .simpleChannel()

    init {
        val encoder: BiConsumer<JsonObject, PacketBuffer> = BiConsumer { json, buffer ->
            buffer.writeBytes(json.toString().toByteArray(Charsets.UTF_8))
            buffer.writeByte(0)
        }

        val decoder: Function<PacketBuffer, JsonObject> = Function { buffer ->
            var bytes = ByteArray(buffer.readableBytes())
            buffer.getBytes(0, bytes)
            bytes = ArrayUtils.remove(bytes, 0)

            bytes.decodeToString().asJsonObject()
        }

        val consumer: BiConsumer<JsonObject, Supplier<NetworkEvent.Context>> = BiConsumer { json, context ->
            handle(json)

            context.get().packetHandled = true
        }

        channel.registerMessage(0, JsonObject::class.java, encoder, decoder, consumer)
    }

    private val handlers = listOf(
        SaveGUIHandler(),
        LoadGUIHandler(),
        ShowGUIHandler(),
    )

    private fun handle(json: JsonObject) {
        val opCode = OpCode.get(json.getInt("op"))
        val data = json.getObject("data")

        handlers.filter { it.opCode == opCode }.forEach { it.handle(data) }
    }

    fun send(opCode: OpCode, data: JsonObject) {
        channel.sendToServer(json {
            "op" to opCode.value
            "data" to data
        })
    }
}