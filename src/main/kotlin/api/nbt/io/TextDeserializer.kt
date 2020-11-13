package de.nycode.mcserver.api.nbt.io

import java.io.*

/**
 * Throwaway object only meant to perform one deserialization of a reader.
 *
 *
 * The parser interface is designed as a utility interface which is an extension of the [Deserializer]
 * interface specifically for readable files.
 *
 *
 *
 * The only method to be implemented is [.fromReader] which reads an object from a generic reader.
 *
 *
 * @param <T> the type of object which is to be parsed
</T> */
interface TextDeserializer<T> : Deserializer<T> {
    /**
     * Deserializes an object from a [Reader].
     *
     * @param reader the reader
     * @return the deserialized object
     * @throws IOException if the deserialization fails
     */
    @Throws(IOException::class)
    fun fromReader(reader: Reader): T

    /**
     * Deserializes an object from a char array using a [CharArrayReader].
     *
     * @param chars the char array
     * @return the deserialized object
     * @throws IOException if the deserialization fails
     */
    @Throws(IOException::class)
    fun fromCharArray(chars: CharArray): T {
        return fromReader(CharArrayReader(chars))
    }

    /**
     * Deserializes an object from a [String] using a [StringReader].
     *
     * @param str the string
     * @return the deserialized object
     * @throws IOException if the deserialization fails
     */
    @Throws(IOException::class)
    fun fromString(str: String): T {
        return fromReader(StringReader(str))
    }

    @Throws(IOException::class)
    override fun fromStream(stream: InputStream): T {
        InputStreamReader(stream).use { reader -> return fromReader(reader) }
    }

    @Throws(IOException::class)
    override fun fromFile(file: File): T {
        FileReader(file).use { reader -> return fromReader(reader) }
    }
}
