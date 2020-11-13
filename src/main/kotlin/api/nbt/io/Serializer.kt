package de.nycode.mcserver.api.nbt.io

import java.io.*

/**
 * Throwaway object for writing objects to files.
 *
 *
 * The serializer interface is designed as a utility interface, giving each implementation several methods for
 * serializing to URL's, files, byte arrays and more.
 *
 *
 *
 * The only method to be implemented is [.toStream] which writes an object into
 * a generic output stream.
 *
 *
 * @param <T> the type of object which is to be serialized
</T> */
interface Serializer<T> {
    /**
     * Writes the object into a [OutputStream].
     *
     * @param object the object
     * @param stream the output stream
     * @throws IOException if an I/O error occurs
     */
    @Throws(IOException::class)
    fun toStream(`object`: T, stream: OutputStream)

    /**
     * Writes the object into a [File] using a [FileOutputStream].
     *
     * @param object the object
     * @param file the file
     * @throws IOException if an I/O error occurs
     */
    @Throws(IOException::class)
    fun toFile(`object`: T, file: File) {
        FileOutputStream(file).use { stream ->
            BufferedOutputStream(stream).use { buffStream ->
                toStream(
                    `object`,
                    buffStream
                )
            }
        }
    }

    /**
     * Writes the object into bytes `byte[]` using a [ByteArrayOutputStream] with set capacity.
     *
     * @param object the object
     * @param capacity the byte buffer capacity used by the stream
     * @return a byte array containing the serialized object
     * @throws IOException if an I/O error occurs
     * @see ByteArrayOutputStream.ByteArrayOutputStream
     */
    @Throws(IOException::class)
    fun toBytes(`object`: T, capacity: Int): ByteArray {
        val stream = ByteArrayOutputStream(capacity)
        toStream(`object`, stream)
        stream.close()
        return stream.toByteArray()
    }

    /**
     * Writes the object into bytes `byte[]` using a [ByteArrayOutputStream] with unset capacity.
     *
     * @param object the object
     * @return a byte array containing the serialized object
     * @throws IOException if an I/O error occurs
     * @see ByteArrayOutputStream.ByteArrayOutputStream
     */
    @Throws(IOException::class)
    fun toBytes(`object`: T): ByteArray {
        val stream = ByteArrayOutputStream()
        toStream(`object`, stream)
        stream.close()
        return stream.toByteArray()
    }
}
