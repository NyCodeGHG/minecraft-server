package de.nycode.mcserver.api.nbt.io

import java.io.*

interface TextSerializer<T> : Serializer<T> {
    /**
     * Writes the object into a [Writer].
     *
     * @param object the object
     * @param writer the writer
     * @throws IOException if an I/O error occurs
     */
    @Throws(IOException::class)
    fun toWriter(`object`: T, writer: Writer)

    /**
     * Writes the object into a char array.
     *
     * @param object the object
     * @return the written char array
     * @throws IOException if an I/O error occurs
     */
    @Throws(IOException::class)
    fun toCharArray(`object`: T): CharArray {
        val writer = CharArrayWriter()
        toWriter(`object`, writer)
        return writer.toCharArray()
    }

    /**
     * Writes the object into a [String] using a [StringWriter].
     *
     * @param object the object
     * @throws IOException if an I/O error occurs
     */
    @Throws(IOException::class)
    fun toString(`object`: T): String {
        val writer: Writer = StringWriter()
        toWriter(`object`, writer)
        return writer.toString()
    }

    /**
     * Writes the object into an [OutputStream] using an [OutputStreamWriter].
     *
     * @param object the object
     * @param stream the stream
     * @throws IOException if an I/O error occurs
     */
    @Throws(IOException::class)
    override fun toStream(`object`: T, stream: OutputStream) {
        val writer: Writer = OutputStreamWriter(stream)
        toWriter(`object`, writer)
        writer.flush()
    }

    /**
     * Writes the object into a [File] using an [FileWriter].
     *
     * @param object the object
     * @param file the file
     * @throws IOException if an I/O error occurs
     */
    @Throws(IOException::class)
    override fun toFile(`object`: T, file: File) {
        FileWriter(file).use { writer -> toWriter(`object`, writer) }
    }
}
