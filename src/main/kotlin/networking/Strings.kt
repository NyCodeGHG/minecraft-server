package de.nycode.minecraftserver.networking

const val MAX_MINECRAFT_STRING_LENGTH = 32767

inline fun readString(
    maxLength: Int,
    readByte: () -> Byte,
    readBytes: (length: Int) -> ByteArray
): String {
    val length = readVarInt(readByte)

    if (length > maxLength * 4) {
        // TODO: Correct error handling
        error("The received encoded string buffer length is longer than maximum allowed! ($length > ${maxLength * 4})")
    }

    if (length < 0) {
        // TODO: Correct error handling
        error("The received encoded string buffer length is less than zero! WeirdChamp")
    }

    val stringBuffer = readBytes(length).decodeToString()
    if (stringBuffer.length > maxLength) {
        error("The received string length is longer than the maximum allowed ($length > $maxLength)")
    }
    return stringBuffer
}

inline fun writeString(
    string: String,
    writeByte: (Byte) -> Unit,
    writeFully: (ByteArray) -> Unit
) {
    val bytes = string.encodeToByteArray()
    if (bytes.size > MAX_MINECRAFT_STRING_LENGTH) {
        error("String is too big. (${bytes.size} bytes encoded, max $MAX_MINECRAFT_STRING_LENGTH")
    }
    writeVarInt(bytes.size, writeByte)
    writeFully(bytes)
}
