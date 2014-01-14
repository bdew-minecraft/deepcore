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
import net.minecraft.world.IBlockAccess
import net.bdew.lib.block.HasTE
import net.bdew.deepcore.connected.ConnectedTextureBlock

trait BlockCore[T <: TileCore] extends Block with HasTE[T] with ConnectedTextureBlock {
  def canConnect(world: IBlockAccess, ox: Int, oy: Int, oz: Int, tx: Int, ty: Int, tz: Int): Boolean = {
    val t = getTE(world, ox, oy, oz)
    return t != null && t.modules.contains(BlockPos(tx, ty, tz))
  }
}
