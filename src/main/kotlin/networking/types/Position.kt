package de.nycode.minecraftserver.networking.types

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.Serializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.descriptors.element
import kotlinx.serialization.encoding.*

@OptIn(ExperimentalSerializationApi::class)
@Serializable
data class Position(
    val x: Int,
    val y: Int,
    val z: Int
) {
    @Serializer(forClass = Position::class)
    companion object : KSerializer<Position> {
        override val descriptor: SerialDescriptor =
            buildClassSerialDescriptor("Position") {
                element<Long>("position")
            }

        override fun deserialize(decoder: Decoder): Position =
            decoder.decodeStructure(descriptor) {
                var positionContent: Long? = null
                if (decodeSequentially()) {
                    positionContent = decodeLongElement(descriptor, 0)
                } else while (true) {
                    when (val index = decodeElementIndex(descriptor)) {
                        0 -> positionContent = decodeLongElement(descriptor, 0)
                        CompositeDecoder.DECODE_DONE -> break
                        else -> error("Unexpected index: $index")
                    }
                }
                requireNotNull(positionContent)
                positionContent.toPosition()
            }

        override fun serialize(encoder: Encoder, value: Position) {
            encoder.encodeStructure(descriptor) {
                encodeLongElement(descriptor, 0, value.toLong())
            }
        }

        private fun Position.toLong(): Long {
            return x.toLong() and 0x3FFFFFF shl 38 or (z.toLong() and 0x3FFFFFF shl 12) or (y.toLong() and 0xFFF)
        }

        private fun Long.toPosition(): Position = Position(
            (this shr 38).toInt(),
            (this and 0xFFF).toInt(),
            (this shl 26 shr 38).toInt()
        )
    }
}
