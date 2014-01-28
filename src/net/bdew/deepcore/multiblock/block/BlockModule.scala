/*
 * Copyright (c) bdew, 2014
 * https://github.com/bdew/deepcore
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * https://raw.github.com/bdew/deepcore/master/MMPL-1.0.txt
 */

package net.bdew.deepcore.multiblock.block

import net.minecraft.world.{IBlockAccess, World}
import net.bdew.lib.block.HasTE
import net.bdew.deepcore.connected.{IconCache, ConnectedTextureBlock}
import net.minecraft.entity.EntityLivingBase
import net.minecraft.item.ItemStack
import net.minecraft.entity.player.EntityPlayer
import net.bdew.lib.Misc
import net.bdew.deepcore.multiblock.Tools
import net.bdew.deepcore.multiblock.data.BlockPos
import net.bdew.deepcore.multiblock.tile.TileModule
import net.bdew.deepcore.config.Config
import net.minecraft.block.material.Material

class BlockModule[T <: TileModule](val name: String, val kind: String, val TEClass: Class[T])
  extends BlockMBPart(Config.IDs.getBlockId(name), Material.iron) with HasTE[T] with ConnectedTextureBlock {
  def edgeIcon = IconCache.edgeIcon

  override def canPlaceBlockAt(world: World, x: Int, y: Int, z: Int): Boolean =
    Tools.findConnections(world, BlockPos(x, y, z), kind).size <= 1

  override def onBlockPlacedBy(world: World, x: Int, y: Int, z: Int, player: EntityLivingBase, stack: ItemStack) {
    getTE(world, x, y, z).tryConnect()
  }

  override def onNeighborBlockChange(world: World, x: Int, y: Int, z: Int, id: Int) {
    getTE(world, x, y, z).tryConnect()
  }

  override def breakBlock(world: World, x: Int, y: Int, z: Int, blockId: Int, meta: Int) {
    getTE(world, x, y, z).onBreak()
    super.breakBlock(world, x, y, z, blockId, meta)
  }

  def canConnect(world: IBlockAccess, ox: Int, oy: Int, oz: Int, tx: Int, ty: Int, tz: Int): Boolean = {
    val t = getTE(world, ox, oy, oz)
    if (t.connected :== null) return false
    val t2 = world.getBlockTileEntity(tx, ty, tz)
    if (t != null && t2 != null) {
      if (t.connected.cval ==(tx, ty, tz)) return true
      if (t2.isInstanceOf[TileModule])
        return t2.asInstanceOf[TileModule].connected.cval == t.connected.cval
    }
    return false
  }

  override def onBlockActivated(world: World, x: Int, y: Int, z: Int, player: EntityPlayer, meta: Int, xoffs: Float, yoffs: Float, zoffs: Float): Boolean = {
    if (player.isSneaking) return false
    if (world.isRemote) return true
    val te = getTE(world, x, y, z)
    val p = te.connected.cval
    if (p == null) {
      player.addChatMessage(Misc.toLocal("deepcore.message.notconnected"))
      return true
    } else {
      val bl = p.getBlock(world)
      if (bl == null) {
        te.connected.cval = null
        player.addChatMessage(Misc.toLocal("deepcore.message.notconnected"))
        return true
      } else {
        return bl.onBlockActivated(world, p.x, p.y, p.z, player, meta, 0, 0, 0)
      }
    }
  }
}
