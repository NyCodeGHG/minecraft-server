@file:OptIn(ExperimentalSerializationApi::class)

package de.nycode.minecraftserver.networking.types

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.descriptors.element
import kotlinx.serialization.encoding.*
import java.util.*

@Serializer(forClass = UUID::class)
object UUIDSerializer : KSerializer<UUID> {
    override val descriptor: SerialDescriptor = buildClassSerialDescriptor("UUID:Binary") {
        element<Long>("msb")
        element<Long>("lsb")
    }

    override fun serialize(encoder: Encoder, value: UUID) {
        encoder.encodeStructure(descriptor) {
            encodeLongElement(descriptor, 0, value.mostSignificantBits)
            encodeLongElement(descriptor, 1, value.leastSignificantBits)
        }
    }

    override fun deserialize(decoder: Decoder): UUID =
        decoder.decodeStructure(descriptor) {
            var mostSignificationBits: Long? = null
            var leastSignificationBits: Long? = null

            if (decodeSequentially()) {
                mostSignificationBits = decodeLongElement(descriptor, 0)
                leastSignificationBits = decodeLongElement(descriptor, 1)
            } else while (true) {
                when (val index = decodeElementIndex(descriptor)) {
                    0 -> mostSignificationBits = decodeLongElement(descriptor, 0)
                    1 -> leastSignificationBits = decodeLongElement(descriptor, 1)
                    CompositeDecoder.DECODE_DONE -> break
                    else -> error("Unexpected index: $index")
                }
            }

            requireNotNull(mostSignificationBits)
            requireNotNull(leastSignificationBits)

            UUID(mostSignificationBits, leastSignificationBits)
        }
}

@Serializer(forClass = UUID::class)
object UUIDStringSerializer : KSerializer<UUID> {
    override val descriptor: SerialDescriptor =
        buildClassSerialDescriptor("UUID:String") {
            element<String>("uuid")
        }

    override fun serialize(encoder: Encoder, value: UUID) {
        encoder.encodeStructure(descriptor) {
            encodeStringElement(descriptor, 0, value.toString())
        }
    }

    override fun deserialize(decoder: Decoder): UUID =
        decoder.decodeStructure(descriptor) {
            var uuid: String? = null

            if (decodeSequentially()) {
                uuid = decodeStringElement(descriptor, 0)
            } else while (true) {
                when (val index = decodeElementIndex(descriptor)) {
                    0 -> uuid = decodeStringElement(descriptor, 0)
                    CompositeDecoder.DECODE_DONE -> break
                    else -> error("Unexpected index: $index")
                }
            }
            requireNotNull(uuid)
            UUID.fromString(uuid)
        }
}
