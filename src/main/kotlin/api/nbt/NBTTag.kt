package de.nycode.mcserver.api.nbt

abstract class NBTTag<T>(var value: T) {

    abstract val type: NBTType

    abstract fun toMSONString(): String

    fun getTypeId(): Byte {
        return type.id
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is NBTTag<*>) return false

        if (value != other.value) return false
        if (type != other.type) return false

        return true
    }

    override fun hashCode() = value.hashCode()

    override fun toString(): String {
        return toMSONString()
    }
}
