package dev.kotx.customgui.gateway

import dev.kotx.customgui.asJsonObject
import dev.kotx.customgui.gateway.handlers.SaveGUIHandler
import dev.kotx.customgui.getInt
import dev.kotx.customgui.getObject
import dev.kotx.customgui.json
import kotlinx.serialization.json.JsonObject
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.plugin.messaging.PluginMessageListener
import java.io.ByteArrayOutputStream
import java.io.DataOutputStream

class GatewayClient(
    private val plugin: JavaPlugin
) : PluginMessageListener {
    private val handlers = mutableListOf(
        SaveGUIHandler()
    )

    override fun onPluginMessageReceived(channel: String, player: Player, message: ByteArray) {
        val msg = message.drop(1).dropLast(1).toByteArray().toString(Charsets.UTF_8)
        val json = msg.asJsonObject()
        val opCode = OpCode.get(json.getInt("op"))
        val data = json.getObject("data")

        handlers.filter { it.opCode == opCode }.forEach { it.handle(player, data) }
    }

    fun send(player: Player, opCode: OpCode, data: JsonObject) {
        player.sendPluginMessage(plugin, "customgui:messenger", json {
            "op" to opCode.value
            "data" to data
        }.toString().asPacket())
    }

    private fun String.asPacket(): ByteArray {
        val baos = ByteArrayOutputStream()
        val out = DataOutputStream(baos)
        out.writeByte(0)
        out.writeBytes(this)
        out.writeByte(0)

        return baos.toByteArray()
    }
}