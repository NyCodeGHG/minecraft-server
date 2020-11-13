package de.nycode.mcserver.api.nbt.io

import de.nycode.mcserver.api.nbt.NBTNamedTag
import java.io.IOException
import java.io.OutputStream
import java.util.zip.GZIPOutputStream

class NBTSerializer
/**
 * Constructs a new NBT-Serializer with enabled gzip compression.
 */ @JvmOverloads constructor(private val compress: Boolean = true) : Serializer<NBTNamedTag> {
    @Throws(IOException::class)
    override fun toStream(tag: NBTNamedTag, stream: OutputStream) {
        if (compress) {
            val gzipStream = GZIPOutputStream(stream)
            val nbtStream = NBTOutputStream(gzipStream)
            nbtStream.writeNamedTag(tag)
            gzipStream.finish()
        } else {
            NBTOutputStream(stream).writeNamedTag(tag)
        }
    }
    /**
     * Constructs a new NBT-Serializer.
     *
     * @param compress whether to use gzip compression.
     */
}
