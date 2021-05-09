package de.nycode.minecraftserver.networking.serialization

import de.nycode.minecraftserver.networking.MinecraftEnumType
import de.nycode.minecraftserver.networking.MinecraftNumberType
import de.nycode.minecraftserver.networking.minecraft
import io.ktor.utils.io.core.*
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.StructureKind
import kotlinx.serialization.encoding.CompositeEncoder

@OptIn(ExperimentalSerializationApi::class)
open class MinecraftProtocolEncoder(
    output: Output
) : AbstractMinecraftProtocolEncoder(output) {

    override fun shouldEncodeElementDefault(descriptor: SerialDescriptor, index: Int) = true

    override fun SerialDescriptor.getTag(index: Int): ProtocolDesc {
        return extractParameters(this, index)
    }

    override fun encodeTaggedInt(
        tag: ProtocolDesc, value: Int
    ) {
        when (tag.type) {
            MinecraftNumberType.DEFAULT -> output.writeInt(value)
            MinecraftNumberType.UNSIGNED -> encodeUInt(value.toUInt())
            MinecraftNumberType.VAR -> encodeVarInt(value)
        }
    }

    override fun encodeTaggedByte(
        tag: ProtocolDesc, value: Byte
    ) {
        when (tag.type) {
            MinecraftNumberType.UNSIGNED -> encodeUByte(value.toUByte())
            else -> output.writeByte(value)
        }
    }

    override fun encodeTaggedShort(
        tag: ProtocolDesc, value: Short
    ) {
        when (tag.type) {
            MinecraftNumberType.UNSIGNED -> encodeUShort(value.toUShort())
            else -> output.writeShort(value)
        }
    }

    override fun encodeTaggedLong(
        tag: ProtocolDesc, value: Long
    ) {
        // TODO: impl VarLong?
        when (tag.type) {
            MinecraftNumberType.UNSIGNED -> encodeULong(value.toULong())
            else -> output.writeLong(value)
        }
    }

    override fun encodeTaggedFloat(
        tag: ProtocolDesc, value: Float
    ) = output.writeFloat(value)

    override fun encodeTaggedDouble(
        tag: ProtocolDesc, value: Double
    ) = output.writeDouble(value)

    override fun encodeTaggedBoolean(
        tag: ProtocolDesc, value: Boolean
    ) = output.writeByte(if (value) 0x01 else 0x00)

    override fun encodeTaggedString(
        tag: ProtocolDesc, value: String
    ) = output.minecraft.writeString(value)

    override fun encodeTaggedEnum(
        tag: ProtocolDesc,
        enumDescriptor: SerialDescriptor,
        ordinal: Int
    ) {
        val enumTag = extractEnumElementParameters(
            enumDescriptor,
            ordinal
        )
        when (extractEnumParameters(enumDescriptor).type) {
            MinecraftEnumType.VARINT -> encodeVarInt(enumTag.ordinal)
            MinecraftEnumType.BYTE -> output.writeByte(enumTag.ordinal.toByte())
            MinecraftEnumType.UNSIGNED_BYTE -> encodeUByte(enumTag.ordinal.toUByte())
            MinecraftEnumType.INT -> output.writeInt(enumTag.ordinal)
            MinecraftEnumType.STRING ->
                output.minecraft.writeString(enumDescriptor.getElementName(ordinal))
        }
    }

    override fun beginStructure(
        descriptor: SerialDescriptor
    ): CompositeEncoder {
        return when (descriptor.kind) {
            is StructureKind.CLASS -> {
                // TODO
                return MinecraftProtocolEncoder(output)
            }
            is StructureKind.LIST -> {
                // TODO
                super.beginStructure(descriptor)
            }
            else -> {
                super.beginStructure(descriptor)
            }
        }
    }
}
