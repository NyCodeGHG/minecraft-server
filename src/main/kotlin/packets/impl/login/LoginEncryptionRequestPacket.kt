package de.nycode.mcserver.packets.impl.login

import de.nycode.mcserver.ClientHandler
import de.nycode.mcserver.packets.Packet
import java.io.DataInputStream
import java.io.DataOutputStream

class LoginEncryptionRequestPacket(client: ClientHandler) : Packet(client) {
    override val packetId = 0x01

    override fun read(input: DataInputStream) {
        TODO("Not yet implemented")
    }

    override fun write(output: DataOutputStream) {
        TODO("Not yet implemented")
    }
}
