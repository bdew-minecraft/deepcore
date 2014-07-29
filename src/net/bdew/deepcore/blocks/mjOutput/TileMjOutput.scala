/*
 * Copyright (c) bdew, 2014
 * https://github.com/bdew/deepcore
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * https://raw.github.com/bdew/deepcore/master/MMPL-1.0.txt
 */

package net.bdew.deepcore.blocks.mjOutput

import buildcraft.api.power.{IPowerEmitter, IPowerReceptor, PowerHandler}
import net.bdew.deepcore.multiblock.data.{OutputConfig, OutputConfigPower}
import net.bdew.deepcore.multiblock.interact.CIPowerProducer
import net.bdew.deepcore.multiblock.tile.TileOutput
import net.minecraftforge.common.util.ForgeDirection

class TileMjOutput extends TileOutput with IPowerReceptor with IPowerEmitter {
  val kind = "PowerOutput"
  val unit = "MJ"

  def canEmitPowerFrom(side: ForgeDirection): Boolean = true
  def getPowerReceiver(side: ForgeDirection) = null
  def doWork(workProvider: PowerHandler) {}
  def getWorld = worldObj

  def canConnectoToFace(d: ForgeDirection): Boolean = {
    val tile = mypos.adjanced(d).getTile(worldObj, classOf[IPowerReceptor]).getOrElse(return false)
    val pr = tile.getPowerReceiver(d)
    return pr != null
  }

  def doOutput(face: ForgeDirection, cfg: OutputConfig) {
    if (connected :== null) return
    val core = connected.getTile(worldObj, classOf[CIPowerProducer]).getOrElse(return)
    if (checkCanOutput(cfg.asInstanceOf[OutputConfigPower])) {
      val tile = mypos.adjanced(face).getTile(worldObj, classOf[IPowerReceptor]).getOrElse(return)
      val pr = tile.getPowerReceiver(face)
      if (pr != null) {
        val canExtract = core.extract(pr.getMaxEnergyReceived.toFloat, true)
        if (canExtract >= pr.getMinEnergyReceived) {
          val injected = pr.receiveEnergy(PowerHandler.Type.ENGINE, canExtract, face.getOpposite).toFloat
          core.extract(injected, false)
          cfg.asInstanceOf[OutputConfigPower].updateAvg(injected)
          core.outputConfig.updated()
          return
        }
      }
    }
    cfg.asInstanceOf[OutputConfigPower].updateAvg(0)
    core.outputConfig.updated()
  }
}
