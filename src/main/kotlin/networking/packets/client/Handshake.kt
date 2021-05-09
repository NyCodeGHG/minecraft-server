package de.nycode.minecraftserver.networking.packets.client

import de.nycode.minecraftserver.networking.MinecraftNumber
import de.nycode.minecraftserver.networking.MinecraftNumberType
import de.nycode.minecraftserver.networking.MinecraftString
import de.nycode.minecraftserver.networking.SerialOrdinal
import de.nycode.minecraftserver.networking.packets.ClientPacket
import kotlinx.serialization.Serializable

@Serializable
enum class HandshakeNextState {
    @SerialOrdinal(1)
    STATUS,

    @SerialOrdinal(2)
    LOGIN
}

@Serializable
data class Handshake(
    @MinecraftNumber(MinecraftNumberType.VAR)
    val version: Int,
    @MinecraftString(255)
    val address: String,
    @MinecraftNumber(MinecraftNumberType.UNSIGNED)
    val port: Short,
    val nextState: HandshakeNextState
) : ClientPacket
