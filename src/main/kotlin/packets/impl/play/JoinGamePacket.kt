package de.nycode.mcserver.packets.impl.play

import de.nycode.mcserver.ClientHandler
import de.nycode.mcserver.api.Identifier
import de.nycode.mcserver.packets.Packet
import de.nycode.mcserver.writeIdentifier
import de.nycode.mcserver.writeVarInt
import java.io.DataInputStream
import java.io.DataOutputStream
import kotlin.random.Random

class JoinGamePacket(client: ClientHandler) : Packet(client) {

    override val packetId = 0x24

    override fun read(input: DataInputStream) {
        TODO("Not yet implemented")
    }

    override fun write(output: DataOutputStream) {
        output.writeInt(Random.nextInt(0, 1000))
        output.writeBoolean(false)
        output.writeByte(1)
        output.writeByte(-1)
        output.writeVarInt(1)
        output.writeIdentifier(Identifier("overworld"))

    }
}
