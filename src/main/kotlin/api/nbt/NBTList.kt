package de.nycode.mcserver.api.nbt

class NBTList<T : NBTTag<*>>(value: MutableList<T> = ArrayList()) : NBTTag<MutableList<T>>(value), Iterable<T>,
    Cloneable {

    override val type = NBTType.LIST

    override fun toMSONString(): String {
        val builder = StringBuilder("[")
        val iter: Iterator<NBTTag<*>> = iterator()

        var first = true
        while (iter.hasNext()) {
            if (first) first = false else builder.append(',')
            builder.append(iter.next().toMSONString())
        }

        return builder.append("]").toString()
    }

    fun size() = value.size

    operator fun get(index: Int) = value[index]

    fun isEmpty() = value.isEmpty()

    fun add(tag: T) {
        value.add(tag)
    }

    fun add(index: Int, tag: T) {
        if (index < 0 || index >= value.size)
            throw IndexOutOfBoundsException(index.toString())
        value.add(tag)
    }

    override fun iterator(): Iterator<T> {
        return value.iterator()
    }

    override fun clone() = NBTList(value)

}
