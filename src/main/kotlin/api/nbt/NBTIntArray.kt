package de.nycode.mcserver.api.nbt

class NBTIntArray(value: IntArray) : NBTTag<IntArray>(value), Cloneable {
    override val type = NBTType.INT_ARRAY

    override fun toMSONString(): String {
        val stringbuilder = StringBuilder("[I;")
        for (i in value.indices) {
            if (i != 0) {
                stringbuilder.append(',')
            }
            stringbuilder.append(value[i])
        }
        return stringbuilder.append(']').toString()
    }

    fun size() = value.size

    override fun clone() = NBTIntArray(value)
}
