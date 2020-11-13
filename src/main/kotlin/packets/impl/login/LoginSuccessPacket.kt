package de.nycode.mcserver.packets.impl.login

import de.nycode.mcserver.ClientHandler
import de.nycode.mcserver.packets.Packet
import de.nycode.mcserver.packets.State
import de.nycode.mcserver.writeString
import de.nycode.mcserver.writeUUID
import java.io.DataInputStream
import java.io.DataOutputStream
import java.util.*

class LoginSuccessPacket(val uuid: UUID, val username: String, client: ClientHandler) : Packet(client) {

    init {
        if (username.length > 16) {
            throw IllegalArgumentException("Username is too long!")
        }
    }

    override val packetId = 0x02

    override fun read(input: DataInputStream) {
        TODO("Not yet implemented")
    }

    override fun write(output: DataOutputStream) {
        output.writeUUID(this.uuid)
        output.writeString(this.username)
        client.currentState = State.PLAY
    }
}
