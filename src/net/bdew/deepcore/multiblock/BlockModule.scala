/*
 * Copyright (c) bdew, 2014
 * https://github.com/bdew/deepcore
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * https://raw.github.com/bdew/deepcore/master/MMPL-1.0.txt
 */

package net.bdew.deepcore.multiblock

import net.minecraft.block.Block
import net.minecraft.world.{IBlockAccess, World}
import net.bdew.lib.block.HasTE
import net.bdew.deepcore.connected.ConnectedTextureBlock
import net.minecraft.entity.EntityLivingBase
import net.minecraft.item.ItemStack

trait BlockModule[T <: TileModule] extends Block with HasTE[T] with ConnectedTextureBlock {
  val kind: String

  override def canPlaceBlockAt(world: World, x: Int, y: Int, z: Int): Boolean =
    Tools.findConnections(world, BlockPos(x, y, z), kind).size <= 1

  override def onBlockPlacedBy(world: World, x: Int, y: Int, z: Int, player: EntityLivingBase, stack: ItemStack) {
    val conns = Tools.findConnections(world, BlockPos(x, y, z), kind)
    if (conns.size > 0)
      getTE(world, x, y, z).connected := conns(0)
  }

  def canConnect(world: IBlockAccess, ox: Int, oy: Int, oz: Int, tx: Int, ty: Int, tz: Int): Boolean = {
    val t = getTE(world, ox, oy, oz)
    val t2 = world.getBlockTileEntity(tx, ty, tz)
    return t != null && t2 != null && t2.isInstanceOf[TileModule] && t2.asInstanceOf[TileModule].connected.cval == t.connected.cval
  }
}
