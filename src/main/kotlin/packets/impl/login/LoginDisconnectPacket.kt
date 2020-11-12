package de.nycode.mcserver.packets.impl.login

import de.nycode.mcserver.ClientHandler
import de.nycode.mcserver.packets.Packet
import de.nycode.mcserver.writeString
import java.io.DataInputStream
import java.io.DataOutputStream

class LoginDisconnectPacket(client: ClientHandler, private val message: String) : Packet(client) {
    override val packetId: Int = 0x00

    override fun read(input: DataInputStream) {
    }

    override fun write(output: DataOutputStream) {
        output.writeString(message)
    }
}
