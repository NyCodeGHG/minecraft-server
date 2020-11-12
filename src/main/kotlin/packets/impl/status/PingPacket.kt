package de.nycode.mcserver.packets.impl.status

import de.nycode.mcserver.ClientHandler
import de.nycode.mcserver.packets.Packet
import java.io.DataInputStream
import java.io.DataOutputStream

class PingPacket(client: ClientHandler) : Packet(client) {

    override val packetId = 0x01

    override fun read(input: DataInputStream) {
        val payload = input.readLong()
        client.sendPacket(PongPacket(client, payload))
        client.close()
    }

    override fun write(output: DataOutputStream) {
    }
}
