package de.nycode.minecraftserver.networking

import io.ktor.utils.io.*
import io.ktor.utils.io.core.*

val Input.minecraft
    get() = MinecraftByteInput(this)

val Output.minecraft
    get() = MinecraftByteOutput(this)

@JvmInline
value class MinecraftByteInput(private val buffer: Input) {
    /**
     * Read a String from the [buffer] with the specified [maxLength]
     */
    fun readString(maxLength: Int): String {
        return readString(maxLength, buffer::readByte, buffer::readBytes)
    }

    /**
     * Read a VarInt from the [buffer]
     * See https://wiki.vg/Protocol#VarInt_and_VarLong
     */
    fun readVarInt(): Int {
        return readVarInt(buffer::readByte)
    }
}

@JvmInline
value class MinecraftByteOutput(private val buffer: Output) {
    /**
     * Write a String to the [buffer]
     */
    fun writeString(string: String) {
        writeString(string, buffer::writeByte, buffer::writeFully)
    }

    /**
     * Write VarInt to the [buffer]
     */
    fun writeVarInt(value: Int) {
        writeVarInt(value, buffer::writeByte)
    }
}

@JvmInline
value class MinecraftByteReadChannel(private val channel: ByteReadChannel) {
    suspend fun readString(maxLength: Int): String {
        return readString(
            maxLength,
            { channel.readByte() },
            { length ->
                ByteArray(length).also {
                    channel.readFully(
                        it,
                        0,
                        length
                    )
                }
            }
        )
    }

    suspend fun readVarInt(): Int {
        return readVarInt {
            channel.readByte()
        }
    }
}

@JvmInline
value class MinecraftByteWriteChannel(private val channel: ByteWriteChannel) {
    suspend fun writeString(string: String) {
        writeString(string, { channel.writeByte(it) }, { channel.writeFully(it) })
    }

    suspend fun writeVarInt(value: Int) {
        writeVarInt(value) { channel.writeByte(it) }
    }
}
