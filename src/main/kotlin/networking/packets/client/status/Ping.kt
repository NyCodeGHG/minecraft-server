package de.nycode.minecraftserver.networking.packets.client.status

import de.nycode.minecraftserver.networking.packets.ClientPacket
import kotlinx.serialization.Serializable

@Serializable
data class Ping(
    val payload: Long
) : ClientPacket
