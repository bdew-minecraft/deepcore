/*
 * Copyright (c) bdew, 2013 - 2014
 * https://github.com/bdew/deepcore
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * https://raw.github.com/bdew/deepcore/master/MMPL-1.0.txt
 */

package net.bdew.deepcore.blocks.euOutput

import ic2.api.energy.tile.{IEnergyAcceptor, IEnergySource}
import net.bdew.deepcore.compat.Ic2EnetRegister
import net.bdew.deepcore.config.Tuning
import net.bdew.deepcore.multiblock.data.{OutputConfig, OutputConfigPower}
import net.bdew.deepcore.multiblock.interact.{CIOutputFaces, CIPowerProducer}
import net.bdew.deepcore.multiblock.tile.TileOutput
import net.bdew.lib.rotate.RotateableTile
import net.minecraft.tileentity.TileEntity
import net.minecraftforge.common.util.ForgeDirection

abstract class TileEuOutputBase(val maxOutput: Int, val tier: Int) extends TileOutput with IEnergySource with Ic2EnetRegister with RotateableTile {
  val kind = "PowerOutput"
  val unit = "EU"
  val ratio = Tuning.getSection("Power").getFloat("EU_MJ_Ratio")
  var outThisTick = 0F

  override def canConnectoToFace(d: ForgeDirection): Boolean = {
    if (rotation.cval != d) return false
    val tile = mypos.neighbour(d).getTile[IEnergyAcceptor](worldObj).getOrElse(return false)
    return tile.acceptsEnergyFrom(this, d.getOpposite)
  }

  override def onConnectionsChanged(added: Set[ForgeDirection], removed: Set[ForgeDirection]) {
    sendUnload()
  }

  override def emitsEnergyTo(receiver: TileEntity, direction: ForgeDirection) =
    getCore != null && rotation.cval == direction

  def getCfg: Option[OutputConfigPower] = {
    if (connected :== null) return None
    val core =  getCoreAs[CIPowerProducer].getOrElse(return None)
    val onum = core.outputFaces.find(_._1.origin == mypos).getOrElse(return None)._2
    Some(core.outputConfig.getOrElse(onum, return None).asInstanceOf[OutputConfigPower])
  }

  override def getOfferedEnergy: Double = {
    if (checkCanOutput(getCfg.getOrElse(return 0)))
      return getCore.asInstanceOf[CIPowerProducer].extract(maxOutput / ratio, true) * ratio
    else 0
  }

  override def drawEnergy(amount: Double) {
    if (connected :== null) return
    val core =  getCoreAs[CIPowerProducer].getOrElse(return)
    core.extract(amount.toFloat / ratio, false)
    outThisTick += amount.toFloat
  }

  override def getSourceTier = tier

  def updateOutput() {
    getCfg.getOrElse(return).updateAvg(outThisTick)
    outThisTick = 0
    getCore map (_.outputConfig.updated())
  }

  serverTick.listen(updateOutput)

  def doOutput(face: ForgeDirection, cfg: OutputConfig) {}
}

class TileEuOutputLV extends TileEuOutputBase(128, 1)

class TileEuOutputMV extends TileEuOutputBase(512, 2)

class TileEuOutputHV extends TileEuOutputBase(2048, 3)
