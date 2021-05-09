package de.nycode.minecraftserver.networking.types

import de.nycode.minecraftserver.networking.MinecraftNumber
import de.nycode.minecraftserver.networking.MinecraftNumberType
import kotlinx.serialization.Serializable

@Serializable
data class Location(
    val x: Double,
    val y: Double,
    val z: Double,

    @MinecraftNumber(MinecraftNumberType.UNSIGNED)
    val pitch: Byte,
    @MinecraftNumber(MinecraftNumberType.UNSIGNED)
    val yaw: Byte
)
