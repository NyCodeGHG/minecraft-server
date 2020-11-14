package de.nycode.mcserver

import de.nycode.mcserver.api.Identifier
import de.nycode.mcserver.api.nbt.NBTCompound
import de.nycode.mcserver.api.nbt.NBTList
import de.nycode.mcserver.api.nbt.NBTString
import de.nycode.mcserver.api.nbt.io.NBTInputStream
import de.nycode.mcserver.api.nbt.io.NBTOutputStream
import de.nycode.mcserver.api.nbt.io.NBTSerializer
import java.io.*
import java.nio.charset.StandardCharsets
import java.util.*
import kotlin.experimental.and

fun DataInput.readVarInt(): Int {
    var numRead = 0
    var result = 0
    var read: Byte
    do {
        read = readByte()
        val value = (read and 127).toInt()
        result = result or (value shl 7 * numRead)
        numRead++
        if (numRead > 5) {
            throw RuntimeException("VarInt is too big")
        }
    } while ((read and 128.toByte()) != 0.toByte())

    return result
}

fun DataOutput.writeVarInt(integer: Int) {
    var value = integer
    var part: Int
    while (true) {
        part = value and 0x7F
        value = value ushr 7
        if (value != 0) {
            part = part or 0x80
        }
        this.writeByte(part)
        if (value == 0)
            break
    }
}

fun DataInput.readString(): String {
    val size = readVarInt()
    val bytes = ByteArray(size)
    this.readFully(bytes)
    return String(bytes, StandardCharsets.UTF_8)
}

@Throws(IOException::class)
fun DataOutput.writeString(string: String) {
    val bytes = string.toByteArray(StandardCharsets.UTF_8)

    if (bytes.size > Short.MAX_VALUE) {
        throw IOException("Attempt to write a string with a length greater than Short.MAX_VALUE to ByteBuf!")
    }

    this.writeVarInt(bytes.size)
    this.write(bytes)
}

fun DataOutput.writeUUID(uuid: UUID) {
    writeLong(uuid.mostSignificantBits)
    writeLong(uuid.leastSignificantBits)
}

fun DataInput.readUUID(): UUID {
    val mostSignificantBits = readLong()
    val leastSignificantBits = readLong()
    return UUID(mostSignificantBits, leastSignificantBits)
}

fun DataOutput.writeIdentifier(identifier: Identifier) {
    writeString(identifier.toString())
}

fun DataInput.readIdentifier(): Identifier {
    return Identifier(readString())
}

fun DataOutputStream.writeCompound(compound: NBTCompound) {
    val stream = NBTOutputStream(this)
    stream.writeTagCompound(compound)
}

fun DataInputStream.readCompound(depth: Int): NBTCompound {
    val stream = NBTInputStream(this)
    return stream.readTagCompound(depth)
}
