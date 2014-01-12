/*
 * Copyright (c) bdew, 2014
 * https://github.com/bdew/deepcore
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * https://raw.github.com/bdew/deepcore/master/MMPL-1.0.txt
 */

package net.bdew.deepcore.world

import net.minecraftforge.event.world.{WorldEvent, ChunkDataEvent}
import net.minecraftforge.event.ForgeSubscribe
import net.minecraftforge.common.MinecraftForge
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.world.chunk.Chunk
import net.bdew.deepcore.Deepcore
import net.minecraft.world.World

object ChunkDataManager {

  class ChunkData {
    var somecrap: String = ""

    def write(nbt: NBTTagCompound) {
      val tag = new NBTTagCompound()
      tag.setString("somecrap", somecrap)
      nbt.setCompoundTag("bdew.deepcore", tag)
    }

    def read(nbt: NBTTagCompound) {
      val tag = nbt.getCompoundTag("bdew.deepcore")
      somecrap = tag.getString("somecrap")
    }
  }

  var worlds = Map.empty[World, Map[Chunk, ChunkData]]

  def init() {
    MinecraftForge.EVENT_BUS.register(this)
  }

  def get(ch: Chunk): ChunkData = get(ch, ch.worldObj)
  def get(ch: Chunk, world: World): ChunkData = {
    val chunks = worlds(world)
    if (chunks.contains(ch)) chunks(ch)
    else {
      val cd = new ChunkData
      worlds += (world -> (worlds(world) + (ch -> cd)))
      cd
    }
  }

  def del(ch: Chunk, world: World) = worlds += (world -> (worlds(world) - ch))

  def has(ch: Chunk, world: World) = worlds.contains(world) && worlds(world).contains(ch)

  @ForgeSubscribe
  def onWorldLoad(ev: WorldEvent.Load) {
    if (ev.world.isRemote) return
    worlds += (ev.world -> Map.empty)
  }

  @ForgeSubscribe
  def onWorldUnLoad(ev: WorldEvent.Unload) {
    if (ev.world.isRemote) return
    Deepcore.logInfo("Unloding dimension %d - removing %d chunks", ev.world.provider.dimensionId, worlds(ev.world).size)
    worlds -= ev.world
  }

  @ForgeSubscribe
  def onChunkLoad(ev: ChunkDataEvent.Load) {
    val ch = ev.getChunk
    if (has(ch, ev.world)) del(ch, ev.world)
    get(ch, ev.world).read(ev.getData)
  }

  @ForgeSubscribe
  def onChunkSave(ev: ChunkDataEvent.Save) {
    val ch = ev.getChunk
    if (has(ch, ev.world)) {
      get(ch).write(ev.getData)
      if (!ch.isChunkLoaded)
        del(ch, ev.world)
    }
  }
}
