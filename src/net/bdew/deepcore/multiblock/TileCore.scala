/*
 * Copyright (c) bdew, 2014
 * https://github.com/bdew/deepcore
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * https://raw.github.com/bdew/deepcore/master/MMPL-1.0.txt
 */

package net.bdew.deepcore.multiblock

import net.bdew.lib.data.base.{UpdateKind, TileDataSlots}

trait TileCore extends TileDataSlots {
  val canAccept: Map[String, Int]
  val modules = new DataSlotPosSet("modules", this).setUpdate(UpdateKind.WORLD, UpdateKind.SAVE, UpdateKind.RENDER)

  var acceptNewModules = true
  var revalidateOnNextTick = true

  lazy val mypos = BlockPos(xCoord, yCoord, zCoord)

  def numConnected(kind: String) = modules.map(_.getTile(worldObj, classOf[TileModule])).flatten.count(_.kind == kind)

  def moduleConnected(module: TileModule): Boolean = {
    if (acceptNewModules) {
      modules.add(BlockPos(module.xCoord, module.yCoord, module.zCoord))
      worldObj.markBlockForUpdate(xCoord, yCoord, zCoord)
      return true
    } else false
  }

  def moduleRemoved(module: TileModule) {
    modules.remove(BlockPos(module.xCoord, module.yCoord, module.zCoord))
    worldObj.markBlockForUpdate(xCoord, yCoord, zCoord)
    revalidateOnNextTick = true
  }

  def onBreak() {
    acceptNewModules = false
    for (x <- modules.flatMap(_.getTile(worldObj, classOf[TileModule])))
      x.coreRemoved()
    modules.clear()
  }

  def validateModules() {
    revalidateOnNextTick = false
    val reachable = Tools.findReachableModules(worldObj, mypos)
    val toremove = modules.filter(!reachable.contains(_)).flatMap(_.getTile(worldObj, classOf[TileModule]))
    acceptNewModules = false
    toremove.foreach(moduleRemoved)
    toremove.foreach(_.coreRemoved())
    acceptNewModules = true
  }

  serverTick.listen(()=>{if (revalidateOnNextTick) validateModules()})
}
