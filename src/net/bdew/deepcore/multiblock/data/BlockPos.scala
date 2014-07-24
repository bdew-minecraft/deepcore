/*
 * Copyright (c) bdew, 2014
 * https://github.com/bdew/deepcore
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * https://raw.github.com/bdew/deepcore/master/MMPL-1.0.txt
 */

package net.bdew.deepcore.multiblock.data

import net.bdew.lib.Misc
import net.minecraft.block.Block
import net.minecraft.tileentity.TileEntity
import net.minecraft.world.World
import net.minecraftforge.common.util.ForgeDirection

case class BlockPos(x: Int, y: Int, z: Int) {
  def this(l: Array[Int]) = this(l(0), l(1), l(2))
  def asArray = Array(x, y, z)

  def ==(tx: Int, ty: Int, tz: Int) = x == tx && y == ty && z == tz

  def adjanced(d: ForgeDirection) = BlockPos(x + d.offsetX, y + d.offsetY, z + d.offsetZ)

  def getBlock(w: World): Block = getBlock(w, classOf[Block]).orNull

  def getBlock[T](w: World, cl: Class[T]): Option[T] =
    Option(w.getBlock(x, y, z)) flatMap (x => Misc.asInstanceOpt(x, cl))

  def getTile(w: World): TileEntity = getTile(w, classOf[TileEntity]).orNull

  def getTile[T](w: World, cl: Class[T]): Option[T] =
    Option(w.getTileEntity(x, y, z)) flatMap (x => Misc.asInstanceOpt(x, cl))
}



