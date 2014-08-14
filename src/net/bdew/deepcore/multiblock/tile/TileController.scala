/*
 * Copyright (c) bdew, 2014
 * https://github.com/bdew/deepcore
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * https://raw.github.com/bdew/deepcore/master/MMPL-1.0.txt
 */

package net.bdew.deepcore.multiblock.tile

import net.bdew.deepcore.Deepcore
import net.bdew.deepcore.multiblock.block.BlockModule
import net.bdew.deepcore.multiblock.data.DataSlotPosSet
import net.bdew.deepcore.multiblock.{MachineCore, Tools}
import net.bdew.lib.block.BlockRef
import net.bdew.lib.data.base.{TileDataSlots, UpdateKind}
import net.minecraft.entity.player.EntityPlayer

trait TileController extends TileDataSlots {
  val modules = new DataSlotPosSet("modules", this).setUpdate(UpdateKind.WORLD, UpdateKind.SAVE, UpdateKind.RENDER)

  def cfg: MachineCore

  var acceptNewModules = true
  var revalidateOnNextTick = true
  var modulesChanged = true

  lazy val mypos = BlockRef(xCoord, yCoord, zCoord)

  def getNumOfMoudules(kind: String) = modules.map(_.getTile[TileModule](getWorldObj)).flatten.count(_.kind == kind)

  def onModulesChanged()
  def onClick(player: EntityPlayer)

  def moduleConnected(module: TileModule): Boolean = {
    if (acceptNewModules) {
      modules.add(BlockRef(module.xCoord, module.yCoord, module.zCoord))
      getWorldObj.markBlockForUpdate(xCoord, yCoord, zCoord)
      modulesChanged = true
      return true
    } else false
  }

  def moduleRemoved(module: TileModule) {
    modules.remove(BlockRef(module.xCoord, module.yCoord, module.zCoord))
    getWorldObj.markBlockForUpdate(xCoord, yCoord, zCoord)
    revalidateOnNextTick = true
    modulesChanged = true
  }

  def onBreak() {
    acceptNewModules = false
    for (x <- modules.flatMap(_.getTile[TileModule](getWorldObj)))
      x.coreRemoved()
    modules.clear()
  }

  def validateModules() {
    modules.filter(x => {
      !x.getTile[TileModule](getWorldObj).isDefined || !x.getBlock[BlockModule[TileModule]](getWorldObj).isDefined
    }).foreach(x => {
      Deepcore.logWarn("Block at %s is not a valid module, removing from machine %s at %d,%d,%d", x, this.getClass.getSimpleName, xCoord, yCoord, zCoord)
      modules.remove(x)
    })
    val reachable = Tools.findReachableModules(getWorldObj, mypos)
    val toremove = modules.filterNot(reachable.contains).flatMap(_.getTile[TileModule](getWorldObj))
    acceptNewModules = false
    toremove.foreach(moduleRemoved)
    toremove.foreach(_.coreRemoved())
    acceptNewModules = true
    modulesChanged = true
    modules.map(x =>
      (x.getBlock[BlockModule[TileModule]](getWorldObj).orNull, x.getTile[TileModule](getWorldObj).orNull)
    ).filter({ case (a, b) => a.kind != b.kind }).foreach({ case (a, b) => sys.error("Type mismatch between Block/Tile Block=%s(%s) Tile=%s(%s)".format(a.kind, a, b.kind, b)) })
  }

  serverTick.listen(() => {
    if (revalidateOnNextTick) {
      revalidateOnNextTick = false
      validateModules()
    }
    if (modulesChanged) {
      modulesChanged = false
      onModulesChanged()
      lastChange = getWorldObj.getTotalWorldTime
      getWorldObj.markBlockForUpdate(xCoord, yCoord, zCoord)
    }
  })
}
