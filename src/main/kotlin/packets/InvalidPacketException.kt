package de.nycode.mcserver.packets

class InvalidPacketException(val packetId: Int, val state: State): Exception()
