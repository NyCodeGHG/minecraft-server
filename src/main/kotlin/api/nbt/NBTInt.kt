package de.nycode.mcserver.api.nbt

class NBTInt(value: Int) : NBTTag<Int>(value), Cloneable {
    override val type = NBTType.INT

    override fun toMSONString() = value.toString()

    override fun clone() = NBTInt(value)
}
