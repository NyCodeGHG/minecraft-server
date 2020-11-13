package de.nycode.mcserver.api.nbt

class NBTLong(value: Long) : NBTTag<Long>(value), Cloneable {
    override val type = NBTType.LONG

    override fun toMSONString(): String {
        return "${value}L"
    }

    override fun clone() = NBTLong(value)
}
