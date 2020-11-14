package de.nycode.mcserver.world

interface Chunk {

    fun getChunkX();
    fun getChunkY();

    fun getData(): ChunkData;

}