package de.nycode.mcserver.api.nbt.io

import java.io.*
import java.net.URL

/**
 * Throwaway object only meant to perform one deserialization of a stream.
 *
 *
 * The deserializer interface is designed as a utility interface, giving each implementation several methods for
 * reading from URL's, files, byte arrays and more.
 *
 *
 *
 * The only method to be implemented is [.fromStream] which reads an object from a generic
 * input stream.
 *
 *
 * @param <T> the type of object which is to be deserialized
</T> */
interface Deserializer<T> {
    /**
     * Deserializes an object from an [InputStream].
     *
     * @param stream the stream
     * @return the deserialized object
     * @throws IOException if the deserialization fails
     */
    @Throws(IOException::class)
    fun fromStream(stream: InputStream): T

    /**
     * Deserializes an object from a [File] using a [FileInputStream].
     *
     * @param file the file
     * @return the deserialized object
     * @throws IOException if the deserialization fails
     */
    @Throws(IOException::class)
    fun fromFile(file: File): T {
        FileInputStream(file).use { stream ->
            BufferedInputStream(stream).use { buffStream ->
                return fromStream(
                    buffStream
                )
            }
        }
    }

    /**
     * Deserializes an object from a `byte[]` using a [ByteArrayInputStream].
     *
     * @param bytes the byte array
     * @return the deserialized object
     * @throws IOException if the deserialization fails
     */
    @Throws(IOException::class)
    fun fromBytes(bytes: ByteArray): T {
        val stream = ByteArrayInputStream(bytes)
        return fromStream(stream)
    }

    /**
     * Deserializes an object from a [Class] and a resource path by opening a stream to the resource via the
     * [ClassLoader].
     *
     * @param clazz the class which's class loader is to be used
     * @param resPath the path to the resource
     * @return the deserialized object
     * @throws IOException if the deserialization fails
     */
    @Throws(IOException::class)
    fun fromResource(clazz: Class<*>, resPath: String): T {
        clazz.classLoader.getResourceAsStream(resPath).use { stream ->
            if (stream == null) throw IOException("resource \"$resPath\" could not be found")
            return fromStream(stream)
        }
    }

    /**
     * Deserializes an object from a [URL] by opening a stream to it.
     *
     * @param url the url
     * @return the deserialized object
     * @throws IOException if the deserialization fails
     */
    @Throws(IOException::class)
    fun fromURL(url: URL): T {
        url.openStream().use { stream -> return fromStream(stream) }
    }
}
