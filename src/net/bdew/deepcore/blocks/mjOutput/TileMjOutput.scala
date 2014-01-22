/*
 * Copyright (c) bdew, 2014
 * https://github.com/bdew/deepcore
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * https://raw.github.com/bdew/deepcore/master/MMPL-1.0.txt
 */

package net.bdew.deepcore.blocks.mjOutput

import buildcraft.api.power.{PowerHandler, IPowerReceptor, IPowerEmitter}
import net.minecraftforge.common.ForgeDirection
import net.bdew.deepcore.multiblock.tile.TileModule
import net.bdew.deepcore.multiblock.Tools
import net.bdew.deepcore.multiblock.interact.{CIPowerProducer, MIOutput, CIOutputFaces}
import net.bdew.deepcore.multiblock.data.{OutputConfig, OutputConfigPower}

class TileMjOutput extends TileModule with IPowerEmitter with MIOutput {
  val kind: String = "PowerOutput"

  def canEmitPowerFrom(side: ForgeDirection): Boolean = true

  def makeCfgObject(face: ForgeDirection) = new OutputConfigPower

  def doOutput(face: ForgeDirection, cfg: OutputConfig) {
    if (connected :== null) return
    val tile = mypos.adjanced(face).getTile(worldObj, classOf[IPowerReceptor]).getOrElse(return)
    val core = connected.getTile(worldObj, classOf[CIPowerProducer]).getOrElse(return)
    val pr = tile.getPowerReceiver(face)
    if (pr != null) {
      val canExtract = core.extract(pr.getMaxEnergyReceived, true)
      if (canExtract >= pr.getMinEnergyReceived) {
        val injected = pr.receiveEnergy(PowerHandler.Type.ENGINE, canExtract, face.getOpposite)
        core.extract(injected, false)
        cfg.asInstanceOf[OutputConfigPower].updateAvg(injected)
        core.outputConfig.updated()
        return
      }
    }
    cfg.asInstanceOf[OutputConfigPower].updateAvg(0)
    core.outputConfig.updated()
  }

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

    Tools.updateOutputs(core, this, connections)
  }
}
