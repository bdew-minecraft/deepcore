/*
 * Copyright (c) bdew, 2014
 * https://github.com/bdew/deepcore
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * https://raw.github.com/bdew/deepcore/master/MMPL-1.0.txt
 */

package net.bdew.deepcore.multiblock.data

import net.minecraftforge.common.ForgeDirection

case class BlockFace(x: Int, y: Int, z: Int, face: ForgeDirection) {
  def this(bp: BlockPos, face: ForgeDirection) = this(bp.x, bp.y, bp.z, face)
  def origin = BlockPos(x, y, z)
  def target = origin.adjanced(face)
  def opposite = face.getOpposite
}
