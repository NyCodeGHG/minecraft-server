package de.nycode.mcserver.packets.impl.play

import de.nycode.mcserver.ClientHandler
import de.nycode.mcserver.api.Identifier
import de.nycode.mcserver.api.nbt.NBTCompound
import de.nycode.mcserver.packets.Packet
import de.nycode.mcserver.writeCompound
import de.nycode.mcserver.writeIdentifier
import de.nycode.mcserver.writeVarInt
import org.apache.commons.codec.digest.DigestUtils
import java.io.DataInputStream
import java.io.DataOutput
import java.io.DataOutputStream
import kotlin.random.Random

class JoinGamePacket(client: ClientHandler) : Packet(client) {

    override val packetId = 0x24

    override fun read(input: DataInputStream) {
        TODO("Not yet implemented")
    }

    override fun write(output: DataOutputStream) {
        output.writeInt(Random.nextInt(0, 1000))
        output.writeBoolean(false)
        output.writeByte(1)
        output.writeByte(-1)
        output.writeVarInt(1)
        output.writeIdentifier(Identifier("overworld"))

        val compound = NBTCompound()
        /*compound.putString("name", "minecraft:overworld")
        compound.putByte("piglin_safe", 1)
        compound.putByte("natural", 1)
        compound.putFloat("ambient_light", 1f)
        compound.putLong("fixed_time", 6000)
        compound.putString("infiniburn", "")
        compound.putByte("respawn_anchor_works", 0)
        compound.putByte("has_skylight", 1)
        compound.putByte("bed_works", 1)
        compound.putString("effects", "minecraft:overworld")
        compound.putByte("has_raids", 1)
        compound.putInt("logical_height", 256)
        compound.putFloat("coordinate_scale", 0f)
        compound.putByte("ultrawarm", 0)
        compound.putByte("has_ceiling", 0)*/
        output.writeCompound(compound)
        output.writeCompound(NBTCompound())
        output.writeIdentifier(Identifier("world"))
        output.write(DigestUtils.sha256("123456789").take(8).toByteArray())
        output.writeVarInt(100)
        output.writeVarInt(10)
        output.writeBoolean(false)
        output.writeBoolean(true)
        output.writeBoolean(false)
        output.writeBoolean(false)
    }
}
