/*
 * Copyright (c) bdew, 2014
 * https://github.com/bdew/deepcore
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * https://raw.github.com/bdew/deepcore/master/MMPL-1.0.txt
 */

package net.bdew.deepcore.multiblock.block

import net.bdew.deepcore.config.Config
import net.minecraft.block.material.Material
import net.bdew.deepcore.multiblock.tile.TileCore
import net.bdew.lib.block.HasTE
import net.bdew.deepcore.connected.{IconCache, ConnectedTextureBlock}
import net.minecraft.world.{IBlockAccess, World}
import net.bdew.deepcore.multiblock.data.BlockPos
import net.minecraft.entity.player.EntityPlayer

class BlockController[T <: TileCore](val name: String, val TEClass: Class[T])
  extends BlockMBPart(Config.IDs.getBlockId(name), Material.iron)
  with HasTE[T] with ConnectedTextureBlock {
  def edgeIcon = IconCache.edgeIcon

  override def breakBlock(world: World, x: Int, y: Int, z: Int, blockId: Int, meta: Int) {
    getTE(world, x, y, z).onBreak()
    super.breakBlock(world, x, y, z, blockId, meta)
  }

  def canConnect(world: IBlockAccess, ox: Int, oy: Int, oz: Int, tx: Int, ty: Int, tz: Int): Boolean = {
    val t = getTE(world, ox, oy, oz)
    return t != null && t.modules.contains(BlockPos(tx, ty, tz))
  }

  override def onBlockActivated(world: World, x: Int, y: Int, z: Int, player: EntityPlayer, meta: Int, xoffs: Float, yoffs: Float, zoffs: Float): Boolean = {
    if (player.isSneaking) return false
    if (world.isRemote) return true
    getTE(world, x, y, z).onClick(player)
    return true
  }
}


