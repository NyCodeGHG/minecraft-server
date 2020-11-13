package de.nycode.mcserver.api.nbt.io

import api.nbt.NBTLongArray
import de.nycode.mcserver.api.nbt.*
import de.nycode.mcserver.api.nbt.NBTType.*
import java.io.DataInputStream
import java.io.IOException
import java.io.InputStream
import java.nio.charset.Charset
import java.util.*

/**
 * An NBTInputStream extends [DataInputStream] by allowing to read named tags.
 */
class NBTInputStream
/**
 * Creates a new `NBTInputStream`, which will source its data from the specified input stream.
 *
 * @param in the input stream
 */
    (`in`: InputStream?) : DataInputStream(`in`) {
    /**
     *
     *
     * Reads a tag and its name from the stream.
     *
     *
     *
     * Should the tag be of type [NBTType.COMPOUND] or [NBTType.LIST] will the full content (all
     * elements in the compounds or list) be read.
     *
     *
     *
     * Null may be returned if the id of the tag can not be read due to the stream ending (expected end).
     * If however the stream ends while reading either the tag name or the tag payload, an [IOException] is
     * thrown (unexpected end).
     *
     *
     * @return the tag that was read or null if EOF is reached
     * @throws IOException if an I/O error occurs
     */
    @Throws(IOException::class)
    fun readNamedTag(): NBTNamedTag? {
        return readNamedTag(0)
    }

    /**
     *
     *
     * Reads a tag and its name from the stream.
     *
     *
     *
     * Should the tag be of type [NBTType.COMPOUND] or [NBTType.LIST] will the full content (all
     * elements in the compounds or list) be read.
     *
     *
     *
     * Null may be returned if the id of the tag can not be read due to the stream ending (expected end).
     * If however the stream ends while reading either the tag name or the tag payload, an [IOException] is
     * thrown (unexpected end).
     *
     *
     * @param depth the depth (used for recursive reading of lists or compounds)
     * @return the tag that was read or null if EOF is reached
     * @throws IOException if an I/O error occurs
     */
    @Throws(IOException::class)
    fun readNamedTag(depth: Int): NBTNamedTag? {
        val id = read()
        if (id == -1) return null
        val type: NBTType = NBTType.getById(id.toByte())
        val name = if (type !== END) readString() else ""
        return NBTNamedTag(name, readTag(type, depth))
    }

    /**
     *
     *
     * Reads the payload of a tag given the type.
     *
     *
     *
     * This method accepts a depth parameter which in necessary for recursive reading of compounds and lists.
     *
     *
     *
     * The depth parameter indicates what depth the currently called function has, starting with 0 if this method
     * in being called initially.
     *
     *
     *
     * Should this method be called while reading a compound or list, the depth will be 1. Should these compounds
     * or lists contain further compounds and lists will the depth be 2 (and so on).
     *
     *
     * @param type the type
     * @param depth the depth (used for recursive reading of lists or compounds)
     * @return the tag
     * @throws IOException if an I/O error occurs.
     */
    @Throws(IOException::class)
    fun readTag(type: NBTType, depth: Int): NBTTag<*> {
        return when (type) {
            END -> readTagEnd(depth)
            BYTE -> NBTByte(readByte())
            SHORT -> NBTShort(readShort())
            INT -> NBTInt(readInt())
            LONG -> NBTLong(readLong())
            FLOAT -> NBTFloat(readFloat())
            DOUBLE -> NBTDouble(readDouble())
            BYTE_ARRAY -> readTagByteArray()
            STRING -> readTagString()
            LIST -> readTagList(depth)
            COMPOUND -> readTagCompound(depth)
            INT_ARRAY -> readTagIntArray()
            LONG_ARRAY -> readTagLongArray()
        }
    }

    @Throws(IOException::class)
    fun readTagEnd(depth: Int): NBTEnd {
        if (depth == 0) throw IOException("TAG_End found without a TAG_Compound/TAG_List tag preceding it.")
        return NBTEnd
    }

    @Throws(IOException::class)
    fun readTagByteArray(): NBTByteArray {
        val length = readInt()
        val bytes = ByteArray(length)
        readFully(bytes)
        return NBTByteArray(bytes)
    }

    @Throws(IOException::class)
    fun readTagString(): NBTString {
        return NBTString(readString())
    }

    @Throws(IOException::class)
    fun readTagList(depth: Int): NBTList<*> {
        val elementType: NBTType = NBTType.getById(readByte())
        val length = readInt()
        if (elementType === END && length > 0) throw IOException("List is of type TAG_End but not empty")
        val tagList: MutableList<NBTTag<*>> = ArrayList()
        for (i in 0 until length) {
            val tag: NBTTag<*> = readTag(elementType, depth + 1)
            tagList.add(tag)
        }
        return NBTList(tagList)
    }

    @Throws(IOException::class)
    fun readTagCompound(depth: Int): NBTCompound {
        val tagMap: MutableMap<String, NBTTag<*>> = HashMap<String, NBTTag<*>>()
        while (true) {
            val namedTag: NBTNamedTag = readNamedTag(depth + 1) ?: throw IOException("NBT ends inside a list")
            val tag: NBTTag<*> = namedTag.tag
            if (tag is NBTEnd) break else tagMap[namedTag.name] = tag
        }
        return NBTCompound(tagMap)
    }

    @Throws(IOException::class)
    fun readTagIntArray(): NBTIntArray {
        val length = readInt()
        val data = IntArray(length)
        for (i in 0 until length) data[i] = readInt()
        return NBTIntArray(data)
    }

    @Throws(IOException::class)
    fun readTagLongArray(): NBTLongArray {
        val length = readInt()
        val data = LongArray(length)
        for (i in 0 until length) data[i] = readLong()
        return NBTLongArray(data)
    }

    @Throws(IOException::class)
    fun readString(): String {
        val length = readUnsignedShort()
        val bytes = ByteArray(length)
        readFully(bytes)
        return String(bytes, UTF_8)
    }

    companion object {
        private val UTF_8 = Charset.forName("UTF-8")
    }
}
