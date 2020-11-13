package de.nycode.mcserver.api.nbt

class NBTFloat(value: Float) : NBTTag<Float>(value), Cloneable {
    override val type = NBTType.FLOAT

    override fun toMSONString() = "${value}f"

    override fun clone() = NBTFloat(value)
}
