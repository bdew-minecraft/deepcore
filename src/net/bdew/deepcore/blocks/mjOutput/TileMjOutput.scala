/*
 * Copyright (c) bdew, 2014
 * https://github.com/bdew/deepcore
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * https://raw.github.com/bdew/deepcore/master/MMPL-1.0.txt
 */

package net.bdew.deepcore.blocks.mjOutput

import buildcraft.api.power.{IPowerReceptor, IPowerEmitter}
import net.minecraftforge.common.ForgeDirection
import net.bdew.deepcore.multiblock.tile.TileModule
import net.bdew.deepcore.multiblock.Tools
import net.bdew.deepcore.multiblock.interact.CIOutputFaces

class TileMjOutput extends TileModule with IPowerEmitter {
  val kind: String = "PowerOutput"

  def canEmitPowerFrom(side: ForgeDirection): Boolean = true

  var rescanFaces = false

  def update() {
    if (rescanFaces) {
      rescanFaces = false
      doRescanFaces()
    }
  }

  serverTick.listen(update)

  override def tryConnect() {
    super.tryConnect()
    if (connected :!= null) rescanFaces = true
  }


  def doRescanFaces() {
    if (connected :== null) return
    val core = connected.getTile(worldObj, classOf[CIOutputFaces]).getOrElse(return)

    val connections = (for (dir <- ForgeDirection.VALID_DIRECTIONS) yield {
      val tile = mypos.adjanced(dir).getTile(worldObj, classOf[IPowerReceptor]).getOrElse(null)
      if (tile != null) {
        val pr = tile.getPowerReceiver(dir)
        if (pr != null)
          Some(dir)
        else None
      } else None
    }).flatten.toSet

    Tools.updateOutputs(core, mypos, connections)
  }
}
