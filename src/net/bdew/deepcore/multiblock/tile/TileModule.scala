/*
 * Copyright (c) bdew, 2014
 * https://github.com/bdew/deepcore
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * https://raw.github.com/bdew/deepcore/master/MMPL-1.0.txt
 */

package net.bdew.deepcore.multiblock.tile

import net.bdew.lib.data.base.{UpdateKind, TileDataSlots}
import net.bdew.deepcore.multiblock.data.{DataSlotPos, BlockPos}
import net.bdew.deepcore.multiblock.Tools

trait TileModule extends TileDataSlots {
  val kind: String
  val connected = new DataSlotPos("connected", this).setUpdate(UpdateKind.WORLD, UpdateKind.SAVE, UpdateKind.RENDER)

  lazy val mypos = BlockPos(xCoord, yCoord, zCoord)

  def connect(target: TileCore) {
    if (target.moduleConnected(this)) {
      connected := target.mypos

      worldObj.markBlockForUpdate(xCoord, yCoord, zCoord)
      worldObj.notifyBlocksOfNeighborChange(xCoord, yCoord, zCoord, getBlockType.blockID)
    }
  }

  def coreRemoved() {
    connected := null
    worldObj.markBlockForUpdate(xCoord, yCoord, zCoord)
    worldObj.notifyBlocksOfNeighborChange(xCoord, yCoord, zCoord, getBlockType.blockID)
  }

  def onBreak() {
    if (connected.cval != null)
      connected.getTile(worldObj, classOf[TileCore]).getOrElse(return).moduleRemoved(this)
  }

  def tryConnect() {
    if (connected :== null) {
      val r = Tools.findConnections(worldObj, mypos, kind)
      if (r.size > 0) connect(r(0).getTile(worldObj, classOf[TileCore]).getOrElse(return))
    }
  }
}
