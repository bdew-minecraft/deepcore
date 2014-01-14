/*
 * Copyright (c) bdew, 2014
 * https://github.com/bdew/deepcore
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * https://raw.github.com/bdew/deepcore/master/MMPL-1.0.txt
 */

package net.bdew.deepcore.multiblock

import net.bdew.lib.data.base.TileDataSlots

trait TileCore extends TileDataSlots {
  val canAccept: Map[String, Int]
  val modules = new DataSlotPosSet("modules", this)

  def numConnected(kind: String) = modules.map(_.getTile(worldObj, classOf[TileModule])).flatten.count(_.kind == kind)
}
