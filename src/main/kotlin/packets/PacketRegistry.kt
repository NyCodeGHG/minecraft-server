package de.nycode.mcserver.packets

import de.nycode.mcserver.ClientHandler
import de.nycode.mcserver.packets.impl.handshake.HandshakePacket
import de.nycode.mcserver.packets.impl.login.LoginStartPacket
import de.nycode.mcserver.packets.impl.status.PingPacket
import de.nycode.mcserver.packets.impl.status.StatusRequestPacket
import de.nycode.mcserver.readVarInt
import org.slf4j.LoggerFactory
import java.io.ByteArrayInputStream
import java.io.DataInputStream
import kotlin.reflect.KClass

object PacketRegistry {

    private val packets = mutableMapOf<Pair<Int, State>, KClass<out Packet>>()
    private val logger = LoggerFactory.getLogger(PacketRegistry::class.java)

    init {
        // Register incoming packets
        registerPacket(0x00, State.HANDSHAKE, HandshakePacket::class)
        registerPacket(0x00, State.STATUS, StatusRequestPacket::class)
        registerPacket(0x01, State.STATUS, PingPacket::class)
        registerPacket(0x00, State.LOGIN, LoginStartPacket::class)
    }

    private fun registerPacket(id: Int, state: State, packet: KClass<out Packet>) {
        this.packets[id to state] = packet
    }

    fun handleIncomingData(inputStream: DataInputStream, client: ClientHandler) {
        val length = inputStream.readVarInt()

        val dataAndPacketId = inputStream.readNBytes(length)
        val stream = DataInputStream(ByteArrayInputStream(dataAndPacketId))

        val packetId = stream.readVarInt()
        val data = stream.readAllBytes()

        val packetClasses =
            packets.filter { packetId == it.key.first && (client.currentState == it.key.second) || it.key.second == State.ANY }.values

        if (packetClasses.isEmpty()) {
            throw InvalidPacketException(packetId, client.currentState)
        }

        val packet = packetClasses.first().constructors.first().call(client)

        logger.info("Receiving ${packet::class.simpleName} from client")
        packet.read(DataInputStream(ByteArrayInputStream(data)))

    }
}
