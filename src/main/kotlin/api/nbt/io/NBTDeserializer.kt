package de.nycode.mcserver.api.nbt.io

import de.nycode.mcserver.api.nbt.NBTNamedTag
import java.io.IOException
import java.io.InputStream
import java.util.zip.GZIPInputStream

class NBTDeserializer
/**
 * Constructs a new NBT-Deserializer with enabled g-zip decompression.
 */ @JvmOverloads constructor(private val compressed: Boolean = true) : Deserializer<NBTNamedTag> {
    @Throws(IOException::class)
    override fun fromStream(stream: InputStream): NBTNamedTag {
        val nbtStream: NBTInputStream =
            if (compressed) NBTInputStream(GZIPInputStream(stream)) else NBTInputStream(stream)
        return nbtStream.readNamedTag() ?: throw IOException("failed to read NBT tag due to EOS")
    }
    /**
     * Constructs a new NBT-Deserializer.
     *
     * @param compressed whether the input is g-zip compressed
     */
}
