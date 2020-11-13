package de.nycode.mcserver.api.nbt.io

import api.nbt.NBTLongArray
import de.nycode.mcserver.api.nbt.*
import de.nycode.mcserver.api.nbt.NBTType.*
import java.io.DataOutputStream
import java.io.IOException
import java.io.OutputStream
import java.nio.charset.Charset
import java.nio.charset.StandardCharsets
import java.util.*

/**
 * An NBTInputStream extends [DataOutputStream] by allowing to write named tags.
 */
class NBTOutputStream
/**
 * Creates a new `NBTOutputStream`, which will write data to the
 * specified underlying output stream.
 *
 * @param out the output stream
 */
    (out: OutputStream?) : DataOutputStream(out) {
    /**
     * Writes a tag.
     *
     * @param tag the tag to write
     * @throws IOException if an I/O error occurs
     */
    @Throws(IOException::class)
    fun writeNamedTag(name: String, tag: NBTTag<*>) {
        Objects.requireNonNull<Any>(tag)
        val typeId = tag.type.id.toInt()
        val nameBytes = name.toByteArray(UTF_8)
        writeByte(typeId)
        writeShort(nameBytes.size)
        write(nameBytes)
        if (typeId == END_ID) throw IOException("Named TAG_End not permitted.")
        writeTag(tag)
    }

    /**
     * Writes a tag.
     *
     * @param tag the tag to write
     * @throws IOException if an I/O error occurs
     */
    @Throws(IOException::class)
    fun writeNamedTag(tag: NBTNamedTag) {
        writeNamedTag(tag.name, tag.tag)
    }

    /**
     * Writes a tag payload.
     *
     * @param tag the tag
     * @throws IOException if an I/O error occurs
     */
    @Throws(IOException::class)
    fun writeTag(tag: NBTTag<*>) {
        when (tag.type) {
            END -> {
            }
            BYTE -> writeByte((tag as NBTByte).value.toInt())
            SHORT -> writeShort((tag as NBTShort).value.toInt())
            INT -> writeInt((tag as NBTInt).value)
            LONG -> writeLong((tag as NBTLong).value)
            FLOAT -> writeFloat((tag as NBTFloat).value)
            DOUBLE -> writeDouble((tag as NBTDouble).value)
            BYTE_ARRAY -> writeTagByteArray(tag as NBTByteArray)
            STRING -> writeTagString(tag as NBTString)
            LIST -> writeTagList(tag as NBTList<*>)
            COMPOUND -> writeTagCompound(tag as NBTCompound)
            INT_ARRAY -> writeTagIntArray(tag as NBTIntArray)
            LONG_ARRAY -> writeTagLongArray(tag as NBTLongArray)
        }
    }

    /**
     * Writes a `TAG_String` tag.
     *
     * @param tag the tag.
     * @throws IOException if an I/O error occurs
     */
    @Throws(IOException::class)
    fun writeTagString(tag: NBTString) {
        val bytes: ByteArray = tag.value.toByteArray(StandardCharsets.UTF_8)
        writeShort(bytes.size)
        write(bytes)
    }

    /**
     * Writes a `TAG_Byte_Array` tag.
     *
     * @param tag the tag
     * @throws IOException if an I/O error occurs
     */
    @Throws(IOException::class)
    fun writeTagByteArray(tag: NBTByteArray) {
        val bytes: ByteArray = tag.value
        writeInt(bytes.size)
        write(bytes)
    }

    /**
     * Writes a `TAG_List` tag.
     *
     * @param tag the tag.
     * @throws IOException if an I/O error occurs
     */
    @Throws(IOException::class)
    fun writeTagList(tag: NBTList<*>) {
        val type: NBTType = tag.type
        val tags = tag.value
        val size = tags.size
        writeByte(type.id.toInt())
        writeInt(size)
        for (element in tags) writeTag(element)
    }

    /**
     * Writes a `TAG_Compound` tag.
     *
     * @param tag the tag
     * @throws IOException if an I/O error occurs
     */
    @Throws(IOException::class)
    fun writeTagCompound(tag: NBTCompound) {
        for ((key, value) in tag.value.entries) {
            writeNamedTag(key, value)
        }
        writeByte(END_ID)
    }

    /**
     * Writes a `TAG_Int_Array` tag.
     *
     * @param tag the tag
     * @throws IOException if an I/O error occurs
     */
    @Throws(IOException::class)
    fun writeTagIntArray(tag: NBTIntArray) {
        writeInt(tag.size())
        for (aData in tag.value) writeInt(aData)
    }

    /**
     * Writes a `TAG_Long_Array` tag.
     *
     * @param tag the tag
     * @throws IOException if an I/O error occurs
     */
    @Throws(IOException::class)
    fun writeTagLongArray(tag: NBTLongArray) {
        writeInt(tag.length())
        for (aData in tag.value) writeLong(aData)
    }

    companion object {
        private val UTF_8 = Charset.forName("UTF-8")
        private val END_ID = END.id.toInt()
    }
}
