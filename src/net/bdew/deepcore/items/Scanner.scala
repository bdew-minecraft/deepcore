/*
 * Copyright (c) bdew, 2014
 * https://github.com/bdew/deepcore
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * https://raw.github.com/bdew/deepcore/master/MMPL-1.0.txt
 */

package net.bdew.deepcore.items

import net.minecraft.item.ItemStack
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.world.World
import net.bdew.deepcore.world.ChunkDataManager
import net.bdew.lib.items.SimpleItem
import net.bdew.deepcore.overlay.ItemWithOverlay
import cpw.mods.fml.relauncher.{Side, SideOnly}

class Scanner(id: Int) extends SimpleItem(id, "Scanner") with ItemWithOverlay {

  @SideOnly(Side.CLIENT)
  def getOverlay(stack: ItemStack) = {
    new ScannerOverlay
  }

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
}
