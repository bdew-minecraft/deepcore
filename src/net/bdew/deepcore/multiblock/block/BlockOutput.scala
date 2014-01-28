/*
 * Copyright (c) bdew, 2013 - 2014
 * https://github.com/bdew/deepcore
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * https://raw.github.com/bdew/deepcore/master/MMPL-1.0.txt
 */

package net.bdew.deepcore.multiblock.block

import net.bdew.deepcore.connected.{IconCache, IconColor, BlockAdditionalRender}
import net.minecraft.world.IBlockAccess
import net.minecraftforge.common.ForgeDirection
import net.bdew.deepcore.multiblock.interact.CIOutputFaces
import net.bdew.deepcore.multiblock.data.BlockFace
import net.bdew.deepcore.multiblock.Outputs
import net.bdew.deepcore.multiblock.tile.TileModule

class BlockOutput[T <: TileModule](name: String, kind: String, TEClass: Class[T]) extends BlockModule(name, kind, TEClass) with BlockAdditionalRender {
  def getOverlayIconAndColor(world: IBlockAccess, x: Int, y: Int, z: Int, face: ForgeDirection): IconColor = {
    val te = getTE(world, x, y, z)
    if (te == null || te.connected.cval == null) return null
    val core = te.connected.getTile(te.worldObj, classOf[CIOutputFaces]).getOrElse(return null)
    val bf = BlockFace(x, y, z, face)
    if (core.outputFaces.contains(bf))
      return new IconColor(IconCache.output, Outputs.color(core.outputFaces(bf)))
    else
      return null
  }

}
