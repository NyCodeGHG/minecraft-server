package de.nycode.mcserver.packets

import de.nycode.mcserver.ClientHandler
import java.io.DataInputStream
import java.io.DataOutputStream

abstract class Packet(val client: ClientHandler) {

    abstract val packetId: Int

    abstract fun read(input: DataInputStream)

    abstract fun write(output: DataOutputStream)

}
