package de.nycode.mcserver.packets.impl.status

import de.nycode.mcserver.ClientHandler
import de.nycode.mcserver.packets.Packet
import java.io.DataInputStream
import java.io.DataOutputStream

class PongPacket(client: ClientHandler, val payload: Long) : Packet(client) {

    override val packetId = 0x01

    override fun read(input: DataInputStream) {
    }

    override fun write(output: DataOutputStream) {
        output.writeLong(payload)
    }
}
