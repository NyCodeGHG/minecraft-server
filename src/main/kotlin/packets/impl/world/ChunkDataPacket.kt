package de.nycode.mcserver.packets.impl.world

import de.nycode.mcserver.ClientHandler
import de.nycode.mcserver.packets.Packet
import java.io.DataInputStream
import java.io.DataOutputStream

class ChunkDataPacket(client: ClientHandler): Packet(client) {

    override val packetId: Int = 0x20

    override fun read(input: DataInputStream) {
        TODO("Not yet implemented")
    }

    override fun write(output: DataOutputStream) {
        TODO("Not yet implemented")
    }


}