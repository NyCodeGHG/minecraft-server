package de.nycode.mcserver.api.nbt


/**
 *
 *
 * The type of an NBTTag.
 *
 *
 *
 * This enum may be prone to further additions, such as the [.LONG_ARRAY] which has been added by Mojang
 * in NBT Version 19133. (second NBT version)
 *
 *
 *
 * For a community maintained documentation of the NBT format and its types, visit the
 * [Minecraft Wiki](https://minecraft.gamepedia.com/NBT_format)
 *
 */
enum class NBTType(
    /**
     * Returns the name of this type.
     *
     * @return the name
     */
    name: String, primitive: Boolean, numeric: Boolean, array: Boolean
) {
    /**
     * Used to mark the end of compounds tags. May also be the type of empty list tags.
     * @since NBT Version 19132
     */
    END("TAG_End", false, false, false),

    /**
     * A signed integer (8 bits). Sometimes used for booleans. (-128 to 127)
     * @since NBT Version 19132
     */
    BYTE("TAG_Byte", true, true, false),

    /**
     * A signed integer (16 bits). (-2<sup>15</sup> to 2<sup>15</sup>-1)
     * @since NBT Version 19132
     */
    SHORT("TAG_Short", true, true, false),

    /**
     * A signed integer (32 bits). (-2<sup>31</sup> to 2<sup>31</sup>-1)
     * @since NBT Version 19132
     */
    INT("TAG_Int", true, true, false),

    /**
     * A signed integer (64 bits). (-2<sup>63</sup> to 2<sup>63</sup>-1)
     * @since NBT Version 19132
     */
    LONG("TAG_Long", true, true, false),

    /**
     * A signed (IEEE 754-2008) floating point number (32 bits).
     * @since NBT Version 19132
     */
    FLOAT("TAG_Float", true, true, false),

    /**
     * A signed (IEEE 754-2008) floating point number (64 bits).
     * @since NBT Version 19132
     */
    DOUBLE("TAG_Double", true, true, false),

    /**
     * An array of [.BYTE] with maximum length of [Integer.MAX_VALUE].
     * @since NBT Version 19132
     */
    BYTE_ARRAY("TAG_Byte_Array", false, false, true),

    /**
     * UTF-8 encoded string.
     * @since NBT Version 19132
     */
    STRING("TAG_String", true, false, false),

    /**
     * A list of unnamed tags of equal type.
     * @since NBT Version 19132
     */
    LIST("TAG_List", false, false, false),

    /**
     * Compound of named tags followed by [.END].
     * @since NBT Version 19132
     */
    COMPOUND("TAG_Compound", false, false, false),

    /**
     * An array of [.BYTE] with maximum length of [Integer.MAX_VALUE].
     * @since NBT Version 19132
     */
    INT_ARRAY("TAG_Int_Array", false, false, true),

    /**
     * An array of [.LONG] with maximum length of [Integer.MAX_VALUE].
     * @since NBT Version 19133
     */
    LONG_ARRAY("TAG_Long_Array", false, false, true);

    /**
     *
     *
     * Returns whether this tag type is numeric.
     *
     *
     *
     * All tag types with payloads that are representable as a [Number] are compliant with this definition.
     *
     *
     * @return whether this type is numeric
     */
    val isNumeric: Boolean

    /**
     * Returns whether this tag type is primitive, meaning that it is not a [NBTByteArray], [NBTIntArray],
     * [NBTList], [NBTCompound] or [NBTEnd].
     *
     * @return whether this type is numeric
     */
    val isPrimitive: Boolean

    /**
     * Returns whether this tag type is is an array type such as [NBTByteArray] or [NBTIntArray].
     *
     * @return whether this type is an array type
     */
    val isArray: Boolean

    /**
     *
     *
     * Returns the id of this tag type.
     *
     *
     *
     * Although this method is currently equivalent to [.ordinal], it should always be used in its stead,
     * since it is not guaranteed that this behavior will remain consistent.
     *
     *
     * @return the id
     */
    val id: Byte = ordinal.toByte()

    override fun toString(): String {
        return name
    }

    companion object {
        /**
         * Returns the type with the given id.
         *
         * @param id the id
         * @return the type
         */
        fun getById(id: Byte): NBTType {
            return values()[id.toInt()]
        }
    }

    init {
        isNumeric = numeric
        isPrimitive = primitive
        isArray = array
    }
}
