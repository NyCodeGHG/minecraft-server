package de.nycode.minecraftserver.networking

import de.nycode.minecraftserver.networking.packets.*
import de.nycode.minecraftserver.networking.serialization.MinecraftProtocol
import io.ktor.utils.io.*
import io.ktor.utils.io.core.*
import kotlinx.serialization.SerializationStrategy

// FIXME migrate to sealed interface in Kotlin 1.5 and add length to it
sealed class PacketContent<T : Packet> {
    data class Found<T : Packet>(
        val state: PacketState,
        val type: PacketType<T>,
        val length: Int,
        val packet: T
    ) : PacketContent<T>()

    data class NotFound<T : Packet>(
        val length: Int,
        val id: Int,
        val remainingContent: ByteArray,
    ) : PacketContent<T>() {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other !is NotFound<*>) return false

            if (length != other.length) return false
            if (id != other.id) return false
            if (!remainingContent.contentEquals(other.remainingContent)) return false

            return true
        }

        override fun hashCode(): Int {
            var result = length
            result = 31 * result + id
            result = 31 * result + remainingContent.contentHashCode()
            return result
        }
    }
}

fun MinecraftByteInput.readPacket(
    state: PacketState,
    isServer: Boolean,
): PacketContent<Packet> {
    return readPacket(
        state,
        isServer,
        { readVarInt() },
        { byteArray -> buffer.readFully(byteArray) },
    )
}

suspend fun MinecraftByteReadChannel.readPacket(
    state: PacketState,
    isServer: Boolean,
): PacketContent<Packet> {
    return readPacket(
        state,
        isServer,
        { readVarInt() },
        { byteArray -> channel.readFully(byteArray) },
    )
}

fun MinecraftByteInput.readServerPacket(
    state: PacketState,
): PacketContent<ServerPacket> {
    return readPacket(
        state = state,
        isServer = true,
    ) as PacketContent<ServerPacket>
}

fun MinecraftByteInput.readClientPacket(
    state: PacketState,
): PacketContent<ClientPacket> {
    return readPacket(
        state = state,
        isServer = false,
    ) as PacketContent<ClientPacket>
}

suspend fun MinecraftByteReadChannel.readServerPacket(
    state: PacketState,
): PacketContent<ServerPacket> {
    return readPacket(
        state = state,
        isServer = true,
    ) as PacketContent<ServerPacket>
}

suspend fun MinecraftByteReadChannel.readClientPacket(
    state: PacketState,
): PacketContent<ClientPacket> {
    return readPacket(
        state = state,
        isServer = false,
    ) as PacketContent<ClientPacket>
}

fun <T : Packet> MinecraftByteOutput.writePacket(
    state: PacketState,
    value: T,
) {
    writePacket(
        state,
        value,
        { varint -> writeVarInt(varint) },
        { bytes -> buffer.writeFully(bytes) }
    )
}

suspend fun <T : Packet> MinecraftByteWriteChannel.writePacket(
    state: PacketState,
    value: T,
    flushing: Boolean = true,
) {
    writePacket(
        state,
        value,
        { varint -> writeVarInt(varint) },
        { bytes -> channel.writeFully(bytes) }
    )
    if (flushing) channel.flush()
}

inline fun readPacket(
    state: PacketState,
    isServer: Boolean,
    readVarInt: () -> Int,
    readFullyTo: (ByteArray) -> Unit,
): PacketContent<Packet> {
    val length = readVarInt()
    val id = readVarInt()

    // this here is required to not instantiate a new ByteArray/ByteReadPacket
    // to memory just for reading the ID and after read the rest of the stream
    val idVarIntBytesCount = varIntBytesCount(id)

    val payload = ByteArray(length - idVarIntBytesCount)
    readFullyTo(payload)

    val packetType = when (isServer) {
        true -> state.serverById[id]
        false -> state.clientById[id]
    } ?: return PacketContent.NotFound(
        length = length,
        id = id,
        remainingContent = payload
    )

    val packet = MinecraftProtocol.decodeFromByteArray(
        packetType.serializer,
        payload
    )

    return PacketContent.Found(
        state = state,
        type = packetType as PacketType<Packet>,
        length = length,
        packet = packet,
    )
}

inline fun <T : Packet> writePacket(
    state: PacketState,
    value: T,
    writeVarInt: (Int) -> Unit,
    writeFully: (ByteArray) -> Unit
) {
    val type = state.byKCLass[value::class] ?: error("Packet not found at state")
    val idVarIntBytesCount = varIntBytesCount(type.id)

    val packet = MinecraftProtocol.encodeToByteArray(
        serializer = type.serializer as SerializationStrategy<T>,
        value = value
    )

    // the idVarIntBytesCount is usage here to prevent memory allocation of a Buffer
    // to write the packet id and the packet payload and then get the final length
    // here we avoid this.
    val length = packet.size + idVarIntBytesCount

    writeVarInt(length)
    writeVarInt(type.id)
    writeFully(packet)
}
