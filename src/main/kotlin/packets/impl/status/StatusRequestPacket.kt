package de.nycode.mcserver.packets.impl.status

import de.nycode.mcserver.ClientHandler
import de.nycode.mcserver.packets.Packet
import java.io.DataInputStream
import java.io.DataOutputStream

class StatusRequestPacket(client: ClientHandler) : Packet(client) {

    override val packetId = 0x00

    override fun read(input: DataInputStream) {
        client.sendPacket(StatusResponsePacket(client))
    }

    override fun write(output: DataOutputStream) {
    }
}
