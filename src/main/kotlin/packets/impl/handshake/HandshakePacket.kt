package de.nycode.mcserver.packets.impl.handshake

import de.nycode.mcserver.*
import de.nycode.mcserver.packets.Packet
import de.nycode.mcserver.packets.State
import java.io.DataInputStream
import java.io.DataOutputStream
import kotlin.properties.Delegates

class HandshakePacket(client: ClientHandler) : Packet(client) {

    override val packetId = 0x00

    private var protocolVersion by Delegates.notNull<Version>()
    private var serverAddress by Delegates.notNull<String>()
    private var serverPort by Delegates.notNull<Int>()
    private var nextState by Delegates.notNull<State>()

    override fun read(input: DataInputStream) {

        val protocolNumber = input.readVarInt()
        this.protocolVersion =
            ProtocolVersionParser.getVersions()[protocolNumber] ?: error("Invalid connection Version: $protocolNumber")
        this.serverAddress = input.readString()
        this.serverPort = input.readUnsignedShort()
        this.nextState = State.values()[input.readVarInt()]

        println("Client connection request!")
        println("on $serverAddress:$serverPort")
        println("with game version: ${protocolVersion.name} ($protocolNumber)")
        println("Next State is $nextState")

        client.currentState = nextState
        client.protocolVersion = protocolVersion
        client.connectionAddress = serverAddress
    }

    override fun write(output: DataOutputStream) {
        throw UnsupportedOperationException("Cannot send HandshakePacket")
    }
}
