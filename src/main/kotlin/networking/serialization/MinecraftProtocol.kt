package de.nycode.minecraftserver.networking.serialization

import io.ktor.utils.io.core.*
import kotlinx.serialization.BinaryFormat
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerializationStrategy
import kotlinx.serialization.modules.EmptySerializersModule
import kotlinx.serialization.modules.SerializersModule

@OptIn(ExperimentalSerializationApi::class)
class MinecraftProtocol(
    override val serializersModule: SerializersModule = EmptySerializersModule
) : BinaryFormat {

    companion object Default : BinaryFormat by MinecraftProtocol()

    override fun <T> encodeToByteArray(serializer: SerializationStrategy<T>, value: T): ByteArray {
        val packetBuilder = BytePacketBuilder()
        encodeToByteArray(packetBuilder, serializer, value)

        return packetBuilder.build().readBytes()
    }

    fun <T> encodeToByteArray(
        output: Output,
        serializer: SerializationStrategy<T>,
        value: T
    ) {
        val encoder = MinecraftProtocolEncoder(output)
        encoder.encodeSerializableValue(serializer, value)
    }

    override fun <T> decodeFromByteArray(deserializer: DeserializationStrategy<T>, bytes: ByteArray): T {
        val packetRead = ByteReadPacket(bytes)
        return decodeFromByteArray(deserializer, packetRead)
    }

    fun <T> decodeFromByteArray(
        deserializer: DeserializationStrategy<T>,
        input: Input
    ): T {
        val decoder = MinecraftProtocolDecoder(input)
        return decoder.decodeSerializableValue(deserializer)
    }
}
