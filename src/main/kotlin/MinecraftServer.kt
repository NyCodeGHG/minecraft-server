package de.nycode.minecraftserver

import de.nycode.minecraftserver.networking.PacketContent
import de.nycode.minecraftserver.networking.minecraft
import de.nycode.minecraftserver.networking.packets.PacketState
import de.nycode.minecraftserver.networking.packets.client.Handshake
import de.nycode.minecraftserver.networking.packets.client.status.Ping
import de.nycode.minecraftserver.networking.packets.client.status.Request
import de.nycode.minecraftserver.networking.packets.server.status.Pong
import de.nycode.minecraftserver.networking.packets.server.status.ServerListPing
import de.nycode.minecraftserver.networking.readClientPacket
import de.nycode.minecraftserver.networking.writePacket
import io.ktor.network.selector.*
import io.ktor.network.sockets.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.intellij.lang.annotations.Language

object MinecraftServer {

    private val connectionScope = CoroutineScope(Dispatchers.IO)

    suspend operator fun invoke() {
        val serverSocket = aSocket(ActorSelectorManager(Dispatchers.IO))
            .tcp()
            .bind("127.0.0.1", 25565)
        println("Server Socket started.")


        while (true) {
            val connection = serverSocket.accept()
            connectionScope.launch {
                val input = connection.openReadChannel()
                val output = connection.openWriteChannel(autoFlush = false)

                println("Received a connection")

                // Handshake
                println("Start Handshake")
                val handshake =
                    input.minecraft.readClientPacket(PacketState.HANDSHAKE) as PacketContent.Found<Handshake>
                println(handshake)

                // request
                val request = input.minecraft.readClientPacket(PacketState.STATUS) as PacketContent.Found<Request>
                println(request)

                println("Sending response (server ping)")

                output.minecraft.writePacket(PacketState.STATUS, ServerListPing(STATUS_RESPONSE))

                println("ping start")
                val ping = input.minecraft.readClientPacket(PacketState.STATUS) as PacketContent.Found<Ping>
                println(ping)

                println("Sending Pong Back")

                output.minecraft.writePacket(PacketState.STATUS, Pong(ping.packet.payload))
            }
        }
    }

}

@Language("JSON")
val STATUS_RESPONSE = """
    {"version":{"name":"1.16.5","protocol":754},"players":{"max":1,"online":0,"sample":[]},"description":{"text":"NyCodeGHG/minecraft-server"}}
""".trimIndent()
