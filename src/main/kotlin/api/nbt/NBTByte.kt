package de.nycode.mcserver.api.nbt

class NBTByte(value: Byte) : NBTTag<Byte>(value), Cloneable {
    override val type = NBTType.BYTE

    override fun toMSONString(): String {
        return "${value}b"
    }

    override fun clone() = NBTByte(value)
}
