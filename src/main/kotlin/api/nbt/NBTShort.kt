package de.nycode.mcserver.api.nbt

class NBTShort(value: Short) : NBTTag<Short>(value), Cloneable {

    override val type = NBTType.SHORT

    override fun equals(other: Any?): Boolean {
        return if (other is NBTShort) {
            other.value == value
        } else false
    }

    override fun toMSONString() = "${value}s"
}
