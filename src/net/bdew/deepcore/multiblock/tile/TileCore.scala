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
import net.minecraft.entity.player.EntityPlayer
import net.bdew.deepcore.multiblock.block.BlockModule
import net.bdew.deepcore.multiblock.data.{DataSlotPosSet, BlockPos}
import net.bdew.deepcore.multiblock.{MachineCore, Tools}
import net.bdew.deepcore.Deepcore

trait TileCore extends TileDataSlots {
  val modules = new DataSlotPosSet("modules", this).setUpdate(UpdateKind.WORLD, UpdateKind.SAVE, UpdateKind.RENDER)

  def cfg: MachineCore

  var acceptNewModules = true
  var revalidateOnNextTick = true
  var modulesChanged = true

  lazy val mypos = BlockPos(xCoord, yCoord, zCoord)

  def getNumOfMoudules(kind: String) = modules.map(_.getTile(worldObj, classOf[TileModule])).flatten.count(_.kind == kind)

  def onModulesChanged()
  def onClick(player: EntityPlayer)

  def moduleConnected(module: TileModule): Boolean = {
    if (acceptNewModules) {
      modules.add(BlockPos(module.xCoord, module.yCoord, module.zCoord))
      worldObj.markBlockForUpdate(xCoord, yCoord, zCoord)
      modulesChanged = true
      return true
    } else false
  }

  def moduleRemoved(module: TileModule) {
    modules.remove(BlockPos(module.xCoord, module.yCoord, module.zCoord))
    worldObj.markBlockForUpdate(xCoord, yCoord, zCoord)
    revalidateOnNextTick = true
    modulesChanged = true
  }

  def onBreak() {
    acceptNewModules = false
    for (x <- modules.flatMap(_.getTile(worldObj, classOf[TileModule])))
      x.coreRemoved()
    modules.clear()
  }

  def validateModules() {
    modules.filter(x => {
      !x.getTile(worldObj, classOf[TileModule]).isDefined || !x.getBlock(worldObj, classOf[BlockModule[TileModule]]).isDefined
    }).foreach(x => {
      Deepcore.logWarn("Block at %s is not a valid module, removing from machine %s at %d,%d,%d", x, this.getClass.getSimpleName, xCoord, yCoord, zCoord)
      modules.remove(x)
    })
    val reachable = Tools.findReachableModules(worldObj, mypos)
    val toremove = modules.filter(!reachable.contains(_)).flatMap(_.getTile(worldObj, classOf[TileModule]))
    acceptNewModules = false
    toremove.foreach(moduleRemoved)
    toremove.foreach(_.coreRemoved())
    acceptNewModules = true
    modulesChanged = true
    modules.map(x =>
      (x.getBlock(worldObj, classOf[BlockModule[TileModule]]).getOrElse(null), x.getTile(worldObj, classOf[TileModule]).getOrElse(null))
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
      lastChange = worldObj.getTotalWorldTime
      worldObj.markBlockForUpdate(xCoord, yCoord, zCoord)
    }
  })
}
