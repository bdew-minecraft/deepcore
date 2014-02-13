/*
 * Copyright (c) bdew, 2013 - 2014
 * https://github.com/bdew/deepcore
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * https://raw.github.com/bdew/deepcore/master/MMPL-1.0.txt
 */

package net.bdew.deepcore.blocks.rfOutput

import net.minecraftforge.common.ForgeDirection
import net.bdew.deepcore.multiblock.interact.CIPowerProducer
import net.bdew.deepcore.multiblock.data.{OutputConfig, OutputConfigPower}
import net.bdew.deepcore.multiblock.tile.TileOutput
import cofh.api.energy.IEnergyHandler
import net.bdew.deepcore.config.Tuning

class TileRfOutput extends TileOutput with IEnergyHandler {
  val kind = "PowerOutput"
  val unit = "RF"

  val ratio = Tuning.getSection("Power").getFloat("RF_MJ_Ratio")

  def receiveEnergy(from: ForgeDirection, maxReceive: Int, simulate: Boolean): Int = 0
  def extractEnergy(from: ForgeDirection, maxExtract: Int, simulate: Boolean): Int = 0
  def canInterface(from: ForgeDirection): Boolean = true
  def getEnergyStored(from: ForgeDirection): Int = 0
  def getMaxEnergyStored(from: ForgeDirection): Int = 0

  def canConnectoToFace(d: ForgeDirection): Boolean = {
    val tile = mypos.adjanced(d).getTile(worldObj, classOf[IEnergyHandler]).getOrElse(return false)
    return tile.canInterface(d.getOpposite)
  }

  def doOutput(face: ForgeDirection, cfg: OutputConfig) {
    if (connected :== null) return
    val core = connected.getTile(worldObj, classOf[CIPowerProducer]).getOrElse(return)
    if (checkCanOutput(cfg.asInstanceOf[OutputConfigPower])) {
      val tile = mypos.adjanced(face).getTile(worldObj, classOf[IEnergyHandler]).getOrElse(return)
      val canExtract = core.extract(Int.MaxValue, true)
      val injected = tile.receiveEnergy(face.getOpposite, (canExtract * ratio).toInt, false)
      core.extract(injected / ratio, false)
      cfg.asInstanceOf[OutputConfigPower].updateAvg(injected)
      core.outputConfig.updated()
      return
    }
    cfg.asInstanceOf[OutputConfigPower].updateAvg(0)
    core.outputConfig.updated()
  }
}
