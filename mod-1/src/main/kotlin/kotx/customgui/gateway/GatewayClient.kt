@file:Suppress("INACCESSIBLE_TYPE")

package kotx.customgui.gateway

import kotlinx.serialization.json.*
import kotx.customgui.*
import kotx.customgui.gateway.handlers.*
import kotx.customgui.util.*
import net.minecraft.network.*
import net.minecraft.util.*
import net.minecraftforge.fml.network.*
import org.apache.commons.lang3.*
import java.util.function.*
import java.util.function.Function

class GatewayClient {
    private val channel = NetworkRegistry.ChannelBuilder.named(ResourceLocation(CustomGUIMod.MOD_ID, "workspace"))
        .clientAcceptedVersions(NetworkRegistry.ACCEPTVANILLA::equals)
        .serverAcceptedVersions(NetworkRegistry.ACCEPTVANILLA::equals)
        .networkProtocolVersion { NetworkRegistry.ACCEPTVANILLA }
        .simpleChannel()

    fun connect() {
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

        send(OpCode.LOAD_GUI, json {})
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