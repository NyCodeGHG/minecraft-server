package de.nycode.minecraftserver.networking.serialization

import de.nycode.minecraftserver.networking.MAX_MINECRAFT_STRING_LENGTH
import de.nycode.minecraftserver.networking.MinecraftEnumType
import de.nycode.minecraftserver.networking.MinecraftNumberType
import de.nycode.minecraftserver.networking.minecraft
import io.ktor.utils.io.core.*
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.StructureKind
import kotlinx.serialization.encoding.CompositeDecoder
import kotlinx.serialization.encoding.CompositeDecoder.Companion.DECODE_DONE
import kotlinx.serialization.encoding.Decoder

@OptIn(ExperimentalSerializationApi::class)
open class MinecraftProtocolDecoder(input: Input) : AbstractMinecraftProtocolDecoder(input) {
    private var currentIndex = 0

    override fun decodeElementIndex(descriptor: SerialDescriptor): Int {
        return if (descriptor.elementsCount == currentIndex) {
            DECODE_DONE
        } else {
            currentIndex++
        }
    }

    override fun SerialDescriptor.getTag(index: Int): ProtocolDesc {
        return extractParameters(this, index)
    }

    override fun decodeTaggedBoolean(tag: ProtocolDesc) =
        when (val b = input.readByte()) {
            0x00.toByte() -> false
            0x01.toByte() -> true
            else -> error("Expected boolean value (0 or 1), found $b")
        }

    override fun decodeTaggedByte(tag: ProtocolDesc) =
        when (tag.type) {
            MinecraftNumberType.UNSIGNED -> decodeUByte().toByte()
            else -> input.readByte()
        }

    override fun decodeTaggedShort(tag: ProtocolDesc) =
        when (tag.type) {
            MinecraftNumberType.UNSIGNED -> decodeUShort().toShort()
            else -> input.readShort()
        }

    override fun decodeTaggedInt(tag: ProtocolDesc) =
        when (tag.type) {
            MinecraftNumberType.DEFAULT -> input.readInt()
            MinecraftNumberType.UNSIGNED -> decodeUInt().toInt()
            MinecraftNumberType.VAR -> decodeVarInt()
        }

    override fun decodeTaggedLong(tag: ProtocolDesc) =
        when (tag.type) { // TODO: VarLong
            MinecraftNumberType.UNSIGNED -> decodeULong().toLong()
            else -> input.readLong()
        }

    override fun decodeTaggedFloat(tag: ProtocolDesc) =
        input.readFloat()

    override fun decodeTaggedDouble(tag: ProtocolDesc) =
        input.readDouble()

    override fun decodeTaggedString(tag: ProtocolDesc) =
        input.minecraft.readString(MAX_MINECRAFT_STRING_LENGTH)

    override fun decodeTaggedEnum(tag: ProtocolDesc, enumDescriptor: SerialDescriptor): Int {
        val enumTag = extractEnumParameters(enumDescriptor)
        val ordinal = when (enumTag.type) {
            MinecraftEnumType.VARINT -> decodeVarInt()
            MinecraftEnumType.BYTE -> input.readByte().toInt()
            MinecraftEnumType.UNSIGNED_BYTE -> decodeUByte().toInt()
            MinecraftEnumType.INT -> input.readInt()
            MinecraftEnumType.STRING -> enumDescriptor.getElementIndex(input.minecraft.readString(enumTag.stringMaxLength))
        }
        return findEnumIndexByTag(enumDescriptor, ordinal)
    }

    override fun decodeTaggedInline(tag: ProtocolDesc, inlineDescriptor: SerialDescriptor): Decoder {
        return super.decodeTaggedInline(tag, inlineDescriptor)
    }

    override fun <T> decodeSerializableValue(deserializer: DeserializationStrategy<T>): T {
        return deserializer.deserialize(this)
    }

    override fun decodeCollectionSize(descriptor: SerialDescriptor): Int {
        currentTag.type
        return super.decodeCollectionSize(descriptor)
    }

    override fun beginStructure(descriptor: SerialDescriptor): CompositeDecoder {
        return when (descriptor.kind) {
            is StructureKind.CLASS -> {
                return MinecraftProtocolDecoder(input)
            }
            is StructureKind.LIST -> {
                super.beginStructure(descriptor)
            }
            else -> {
                super.beginStructure(descriptor)
            }
        }
    }
}
