package de.nycode.minecraftserver.networking.packets.server.status

import de.nycode.minecraftserver.networking.packets.ServerPacket
import kotlinx.serialization.Serializable

@Serializable
data class Pong(
    val payload: Long
) : ServerPacket
