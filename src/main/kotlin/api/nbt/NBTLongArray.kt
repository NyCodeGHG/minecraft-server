package api.nbt

import de.nycode.mcserver.api.nbt.NBTTag
import de.nycode.mcserver.api.nbt.NBTType
import java.util.*

/**
 * The `TAG_Long_Array` tag.
 */
class NBTLongArray(longArray: LongArray) : NBTTag<LongArray>(longArray), Cloneable {

    constructor(numbers: Array<Number>) {
        value = LongArray(numbers.size)
        for (i in numbers.indices) value[i] = numbers[i].toLong()
    }

    /**
     * Returns the length of this array.
     *
     * @return the length of this array
     */
    fun length(): Int {
        return value.size
    }

    override val type = NBTType.LONG_ARRAY

    override fun clone() = NBTLongArray(value)

    override fun toMSONString(): String {
        val stringBuilder = StringBuilder("[I;")
        for (i in value.indices) {
            if (i != 0) {
                stringBuilder.append(',')
            }
            stringBuilder.append(value[i])
        }
        return stringBuilder.append(']').toString()
    }
}
