package de.nycode.minecraftserver.networking.packets

import de.nycode.minecraftserver.networking.packets.client.Handshake
import de.nycode.minecraftserver.networking.packets.client.status.Ping
import de.nycode.minecraftserver.networking.packets.client.status.Request
import de.nycode.minecraftserver.networking.packets.server.status.Pong
import de.nycode.minecraftserver.networking.packets.server.status.ServerListPing
import kotlinx.serialization.KSerializer
import kotlin.reflect.KClass

typealias PacketId = Int

enum class PacketState(
    val client: List<PacketType<out ClientPacket>>,
    val server: List<PacketType<out ServerPacket>>
) {
    HANDSHAKE(
        client = listOf(
            PacketType(0x00, Handshake::class, Handshake.serializer())
        ),
        server = emptyList()
    ),
    STATUS(
        client = listOf(
            PacketType(0x00, Request::class, Request.serializer()),
            PacketType(0x01, Ping::class, Ping.serializer())
        ),
        server = listOf(
            PacketType(0x00, ServerListPing::class, ServerListPing.serializer()),
            PacketType(0x01, Pong::class, Pong.serializer())
        )
    ),
    LOGIN(
        client = listOf(),
        server = listOf()
    ),
    PLAY(
        client = listOf(),
        server = listOf()
    );

    val byKCLass: Map<KClass<out Packet>, PacketType<out Packet>> =
        client.associateBy { it.kClass } + server.associateBy { it.kClass }
    val clientById: Map<PacketId, PacketType<out ClientPacket>> = client.associateBy { it.id }
    val serverById: Map<PacketId, PacketType<out ServerPacket>> = server.associateBy { it.id }
}

data class PacketType<T : Packet>(
    val id: PacketId,
    val kClass: KClass<T>,
    val serializer: KSerializer<T>
)
