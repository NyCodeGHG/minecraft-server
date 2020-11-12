package de.nycode.mcserver

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.slf4j.LoggerFactory
import java.net.ServerSocket

fun main() {
    val logger = LoggerFactory.getLogger("Server")
    logger.info("Starting minecraft-server by NyCode")

    logger.info("Fetching protocol versions...")
    val versions = runBlocking {
        ProtocolVersionParser.fetchVersions()
    }
    logger.info("Ready!")

    val port = 25565
    val server = ServerSocket(port)

    while (true) {
        val socket = server.accept()
        logger.info("Client connected: ${socket.inetAddress.hostAddress}")

        GlobalScope.launch {
            ClientHandler(socket, logger).run()
        }
    }

}
