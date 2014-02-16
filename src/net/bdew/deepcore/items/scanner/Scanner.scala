/*
 * Copyright (c) bdew, 2014
 * https://github.com/bdew/deepcore
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * https://raw.github.com/bdew/deepcore/master/MMPL-1.0.txt
 */

package net.bdew.deepcore.items.scanner

import net.minecraft.item.ItemStack
import net.minecraft.entity.player.{EntityPlayerMP, EntityPlayer}
import net.minecraft.world.World
import net.bdew.deepcore.world.ChunkDataManager
import net.bdew.lib.items.SimpleItem
import net.bdew.deepcore.overlay.ItemWithOverlay
import cpw.mods.fml.relauncher.{Side, SideOnly}
import net.minecraft.entity.Entity
import net.bdew.deepcore.Deepcore
import net.bdew.deepcore.config.Tuning
import net.bdew.deepcore.network.{Packets, PktWriter}
import net.bdew.deepcore.noise.NoiseGen
import cpw.mods.fml.common.network.{Player, PacketDispatcher}

class Scanner(id: Int) extends SimpleItem(id, "Scanner") with ItemWithOverlay {
  lazy val cfg = Tuning.getSection("Items").getSection(name)
  lazy val radius = cfg.getInt("Radius")

  PlayerChunkTracker.init()

  @SideOnly(Side.CLIENT)
  def getOverlay(stack: ItemStack) = ScannerOverlay

  override def onItemUse(stack: ItemStack, player: EntityPlayer, world: World, x: Int, y: Int, z: Int, side: Int, xOff: Float, yOff: Float, zOff: Float): Boolean = {
    if (world.isRemote) return true
    val chunk = world.getChunkFromBlockCoords(x, z)
    val cdata = ChunkDataManager.get(chunk)
    if (cdata.somecrap > "") {
      player.addChatMessage("Somecrap: " + cdata.somecrap)
    } else {
      cdata.somecrap = "%d/%d".format(chunk.xPosition, chunk.zPosition)
      chunk.setChunkModified()
      player.addChatMessage("Generating Somecrap: " + cdata.somecrap)
    }
    true
  }
  override def onUpdate(stack: ItemStack, world: World, player: Entity, slot: Int, active: Boolean) {
    if (!active || world.isRemote || !player.isInstanceOf[EntityPlayerMP]) return
    val pp = player.asInstanceOf[EntityPlayerMP]
    val cx = player.posX.floor.toInt >> 4
    val cy = player.posZ.floor.toInt >> 4
    if (PlayerChunkTracker.update(pp, cx, cy)) {
      val pkt = new PktWriter(Packets.SCANNER_UPDATE)
      pkt.writeInt(radius)
      pkt.writeInt(cx)
      pkt.writeInt(cy)
      for (x <- -radius to radius; y <- -radius to radius) {
        pkt.writeFloat(NoiseGen.generate(cx + x, cy + y))
      }
      PacketDispatcher.sendPacketToPlayer(pkt.toPacket, pp.asInstanceOf[Player])
      Deepcore.logInfo("Player %s moved to (%d,%d)", pp.getDisplayName, cx, cy)
    }
  }
}
