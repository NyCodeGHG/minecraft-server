package de.nycode.minecraftserver.networking.serialization

import de.nycode.minecraftserver.networking.MinecraftEnumType
import de.nycode.minecraftserver.networking.MinecraftNumberType

data class ProtocolDesc(
    val type: MinecraftNumberType,
    val stringMaxLength: Int
)

data class ProtocolEnumDesc(
    val type: MinecraftEnumType,
    val stringMaxLength: Int
)

data class ProtocolEnumElementDesc(
    val ordinal: Int
)
