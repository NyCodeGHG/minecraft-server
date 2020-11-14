package de.nycode.mcserver.api.nbt

class NBTByteArray(byteArray: ByteArray) : NBTTag<ByteArray>(byteArray), Cloneable {
    override val type = NBTType.BYTE_ARRAY

    override fun toMSONString(): String {
        val stringbuilder = StringBuilder("[B;")
        for (i in value.indices) {
            if (i != 0) {
                stringbuilder.append(',')
            }
            stringbuilder.append(value[i]).append('B')
        }
        return stringbuilder.append(']').toString()
    }
    fun length() = value.size

    override fun clone() = NBTByteArray(value)
}
