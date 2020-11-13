package de.nycode.mcserver.api.nbt

import api.nbt.NBTLongArray
import de.nycode.mcserver.api.nbt.NBTEnd.toMSONString
import java.util.function.BiConsumer
import java.util.regex.Pattern

class NBTCompound(value: Map<String, NBTTag<*>>) : NBTTag<LinkedHashMap<String, NBTTag<*>>>(LinkedHashMap(value)) {
    override val type = NBTType.COMPOUND

    constructor() : this(emptyMap())

    constructor(vararg tags: NBTNamedTag) {
        for (tag in tags) {
            value[tag.name] = tag.tag
        }
    }

    fun size() = value.size

    fun <T : NBTTag<*>> getTag(key: String): T? {
        return value[key] as T
    }

    fun getByte(key: String): Byte {
        val tag = value[key]
        if (tag !is NBTByte)
            throw NoSuchElementException(key)

        return tag.value
    }

    fun getShort(key: String): Short {
        val tag = value[key]
        if (tag !is NBTShort)
            throw NoSuchElementException(key)

        return tag.value
    }

    fun getInt(key: String): Int {
        val tag = value[key]
        if (tag !is NBTInt)
            throw NoSuchElementException(key)

        return tag.value
    }

    fun getLong(key: String): Long {
        val tag = value[key]
        if (tag !is NBTLong)
            throw NoSuchElementException(key)

        return tag.value
    }

    fun getFloat(key: String): Float {
        val tag = value[key]
        if (tag !is NBTFloat)
            throw NoSuchElementException(key)

        return tag.value
    }

    fun getDouble(key: String): Double {
        val tag = value[key]
        if (tag !is NBTDouble)
            throw NoSuchElementException(key)

        return tag.value
    }

    fun getByteArray(key: String): ByteArray {
        val tag = value[key]
        if (tag !is NBTByteArray)
            throw NoSuchElementException()

        return tag.value
    }

    fun getString(key: String): String {
        val tag = value[key]
        if (tag !is NBTString)
            throw NoSuchElementException(key)

        return tag.value
    }

    fun getTagList(key: String): NBTList<*> {
        val tag = value[key]
        if (tag !is NBTList<*>)
            throw NoSuchElementException(key)

        return tag
    }

    fun getCompound(key: String): Map<String, NBTTag<*>> {
        return getCompoundTag(key).value
    }

    fun getCompoundTag(key: String): NBTCompound {
        val tag = value[key]
        if (tag !is NBTCompound)
            throw NoSuchElementException(key)

        return tag
    }

    fun getIntArray(key: String): IntArray {
        val tag = value[key]
        if (tag !is NBTIntArray)
            throw NoSuchElementException(key)

        return tag.value
    }

    fun getLongArray(key: String): LongArray {
        val tag = value[key]
        if (tag !is NBTLongArray)
            throw NoSuchElementException(key)

        return tag.value
    }

    fun getKeys() = value.keys.toSet()

    fun isEmpty() = value.isEmpty()

    fun hasKey(key: String) = value.containsKey(key)

    fun hasKeyOfType(key: String, type: NBTType) = value.containsKey(key) && type == value[key]?.type

    fun put(name: String, value: NBTTag<*>) {
        this.value[name] = value
    }

    fun putByteArray(name: String, value: ByteArray) {
        this.value[name] = NBTByteArray(value)
    }

    fun putByte(name: String, value: Byte) {
        this.value[name] = NBTByte(value)
    }

    fun putDouble(name: String, value: Double) {
        this.value[name] = NBTDouble(value)
    }

    fun putFloat(name: String, value: Float) {
        this.value[name] = NBTFloat(value)
    }

    fun putIntArray(name: String, value: IntArray) {
        this.value[name] = NBTIntArray(value)
    }

    fun putLongArray(name: String, value: LongArray) {
        this.value[name] = NBTLongArray(value)
    }

    fun putInt(name: String, value: Int) {
        this.value[name] = NBTInt(value)
    }

    fun putLong(name: String, value: Long) {
        this.value[name] = NBTLong(value)
    }

    fun putShort(name: String, value: Short) {
        this.value[name] = NBTShort(value)
    }

    fun putString(name: String, value: String) {
        this.value[name] = NBTString(value)
    }

    fun forEach(action: BiConsumer<String, in NBTTag<*>>) {
        value.forEach(action::accept)
    }

    private val SIMPLE_STRING = Pattern.compile("[A-Za-z0-9._+-]+")

    override fun toMSONString(): String {
        val builder = StringBuilder("{")
        val keys: Set<String> = value.keys

        for (key in keys) {
            if (builder.length > 1) {
                builder.append(',')
            }
            builder
                .append(if (SIMPLE_STRING.matcher(key).matches()) key else NBTString.toMSONString(key))
                .append(':')
                .append(value[key]!!.toMSONString())
        }

        return builder.append("}").toString()
    }
}
