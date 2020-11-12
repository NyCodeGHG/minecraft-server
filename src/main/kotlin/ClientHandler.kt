package de.nycode.mcserver

import de.nycode.mcserver.packets.InvalidPacketException
import de.nycode.mcserver.packets.Packet
import de.nycode.mcserver.packets.PacketRegistry
import de.nycode.mcserver.packets.State
import org.slf4j.Logger
import java.io.*
import java.net.Socket

class ClientHandler(private val socket: Socket, private val logger: Logger) {

    var connectionAddress: String? = null
    var protocolVersion: Version? = null
    var currentState: State = State.HANDSHAKE

    private val writer = DataOutputStream(socket.getOutputStream())
    private val reader = DataInputStream(socket.getInputStream())

    init {
        socket.soTimeout = 3000
    }

    fun run() {

        while (!socket.isClosed) {
            if (reader.available() > 0) {
                try {
                    PacketRegistry.handleIncomingData(reader, this)
                } catch (e: InvalidPacketException) {
                    logger.warn("Invalid packet id: ${"%x".format(e.packetId)} with state ${e.state}")
                }
            }
        }
    }

    fun sendPacket(packet: Packet) {
        logger.info("Sending packet ${packet::class.simpleName}")
        val output = ByteArrayOutputStream()
        val stream = DataOutputStream(output)

        stream.writeVarInt(packet.packetId)
        packet.write(stream)
        val size = stream.size()

        writer.writeVarInt(size)
        writer.write(output.toByteArray())
        writer.flush()
    }

    fun sendRawPacket(id: Int, content: ByteArrayOutputStream) {
        val output = ByteArrayOutputStream()
        val stream = DataOutputStream(output)

        stream.writeVarInt(id)
        stream.write(content.toByteArray())
        val size = stream.size()

        writer.writeVarInt(size)
        writer.write(output.toByteArray())
        writer.flush()
    }

    fun close() {
        this.socket.close()
    }

}
