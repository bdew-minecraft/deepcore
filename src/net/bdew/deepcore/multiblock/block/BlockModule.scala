/*
 * Copyright (c) bdew, 2014
 * https://github.com/bdew/deepcore
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * https://raw.github.com/bdew/deepcore/master/MMPL-1.0.txt
 */

package net.bdew.deepcore.multiblock.block

import net.bdew.deepcore.connected.{ConnectedTextureBlock, IconCache}
import net.bdew.deepcore.multiblock.Tools
import net.bdew.deepcore.multiblock.tile.TileModule
import net.bdew.lib.block.{BlockRef, HasTE}
import net.minecraft.block.Block
import net.minecraft.block.material.Material
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.util.ChatComponentTranslation
import net.minecraft.world.{IBlockAccess, World}

class BlockModule[T <: TileModule](val name: String, val kind: String, val TEClass: Class[T])
  extends BlockMBPart(Material.iron) with HasTE[T] with ConnectedTextureBlock {
  def edgeIcon = IconCache.edgeIcon

  override def canPlaceBlockAt(world: World, x: Int, y: Int, z: Int): Boolean =
    Tools.findConnections(world, BlockRef(x, y, z), kind).size <= 1

  override def onBlockPlacedBy(world: World, x: Int, y: Int, z: Int, player: EntityLivingBase, stack: ItemStack) {
    getTE(world, x, y, z).tryConnect()
  }

  override def onNeighborBlockChange(world: World, x: Int, y: Int, z: Int, block: Block) {
    getTE(world, x, y, z).tryConnect()
  }

  override def breakBlock(world: World, x: Int, y: Int, z: Int, block: Block, meta: Int) {
    getTE(world, x, y, z).onBreak()
    super.breakBlock(world, x, y, z, block, meta)
  }

  def canConnect(world: IBlockAccess, ox: Int, oy: Int, oz: Int, tx: Int, ty: Int, tz: Int): Boolean = {
    val t = getTE(world, ox, oy, oz)
    if (t.connected :== null) return false
    val t2 = world.getTileEntity(tx, ty, tz)
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
    (for {
      p <- te.connected.cval
      bl <- p.block(world)
    } yield {
      bl.onBlockActivated(world, p.x, p.y, p.z, player, meta, 0, 0, 0)
    }) getOrElse {
      player.addChatMessage(new ChatComponentTranslation("deepcore.message.notconnected"))
    }
    true
  }
}
