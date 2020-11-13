package de.nycode.mcserver.api.nbt

class NBTDouble(value: Double) : NBTTag<Double>(value), Cloneable {
    override val type = NBTType.DOUBLE

    override fun toMSONString(): String {
        return "${value}d"
    }

    override fun clone() = NBTDouble(value)
}
