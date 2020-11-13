package de.nycode.mcserver.api.nbt

object NBTEnd : NBTTag<Unit>(Unit), Cloneable {

    override val type = NBTType.END

    override fun toMSONString() = "END"

    override fun equals(other: Any?): Boolean {
        return other is NBTEnd
    }
}
