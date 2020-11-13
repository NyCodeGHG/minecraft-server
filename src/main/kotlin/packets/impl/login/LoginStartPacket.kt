package de.nycode.mcserver.packets.impl.login

import de.nycode.mcserver.ClientHandler
import de.nycode.mcserver.packets.Packet
import de.nycode.mcserver.readString
import java.io.DataInputStream
import java.io.DataOutputStream
import java.util.*
import kotlin.properties.Delegates

class LoginStartPacket(client: ClientHandler) : Packet(client) {
    override val packetId = 0x00

    private var username by Delegates.notNull<String>()

    override fun read(input: DataInputStream) {
        username = input.readString()

        // val packet = LoginDisconnectPacket(client, "{\"text\":\"§cThere is no actual world etc lol\"}")
        // client.sendPacket(packet)
        val packet = LoginSuccessPacket(UUID.fromString("ec6a3dab-6b35-4596-9d7f-f9bdd773874f"), "NyCode", client)
        client.sendPacket(packet)
    }

    override fun write(output: DataOutputStream) {
        TODO("Not yet implemented")
    }
}
