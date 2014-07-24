/*
 * Copyright (c) bdew, 2013 - 2014
 * https://github.com/bdew/deepcore
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * https://raw.github.com/bdew/deepcore/master/MMPL-1.0.txt
 */

package net.bdew.deepcore.blocks.euOutput

import net.minecraftforge.common.util.ForgeDirection
import net.bdew.deepcore.multiblock.interact.CIPowerProducer
import net.bdew.deepcore.multiblock.data.{OutputConfig, OutputConfigPower}
import ic2.api.energy.tile.{IEnergySource, IEnergyAcceptor}
import net.minecraft.tileentity.TileEntity
import net.bdew.deepcore.config.Tuning
import net.bdew.deepcore.compat.Ic2EnetRegister
import net.bdew.lib.rotate.RotateableTile
import scala.Predef._
import scala.Some
import net.bdew.deepcore.multiblock.tile.TileOutput

abstract class TileEuOutputBase(val maxOutput: Int) extends TileOutput with IEnergySource with Ic2EnetRegister with RotateableTile {
  val kind = "PowerOutput"
  val unit = "EU"
  val ratio = Tuning.getSection("Power").getFloat("EU_MJ_Ratio")
  var outThisTick = 0F

  def canConnectoToFace(d: ForgeDirection): Boolean = {
    if (rotation.cval != d) return false
    val tile = mypos.adjanced(d).getTile(worldObj, classOf[IEnergyAcceptor]).getOrElse(return false)
    return tile.acceptsEnergyFrom(this, d.getOpposite)
  }

  override def onConnectionsChanged(added: Set[ForgeDirection], removed: Set[ForgeDirection]) {
    sendUnload()
  }

  def emitsEnergyTo(receiver: TileEntity, direction: ForgeDirection) =
    getCore != null && rotation.cval == direction

  def getCfg: Option[OutputConfigPower] = {
    if (connected :== null) return None
    val core = connected.getTile(worldObj, classOf[CIPowerProducer]).getOrElse(return None)
    val onum = core.outputFaces.find(_._1.origin == mypos).getOrElse(return None)._2
    Some(core.outputConfig.getOrElse(onum, return None).asInstanceOf[OutputConfigPower])
  }

  def getOfferedEnergy: Double = {
    if (checkCanOutput(getCfg.getOrElse(return 0)))
      return getCore.asInstanceOf[CIPowerProducer].extract(maxOutput / ratio, true) * ratio
    else 0
  }

  def drawEnergy(amount: Double) {
    if (connected :== null) return
    val core = connected.getTile(worldObj, classOf[CIPowerProducer]).getOrElse(return)
    core.extract(amount.toFloat / ratio, false)
    outThisTick += amount.toFloat
  }

  def updateOutput() {
    getCfg.getOrElse(return).updateAvg(outThisTick)
    outThisTick = 0
    getCore.outputConfig.updated()
  }

  serverTick.listen(updateOutput)

  def doOutput(face: ForgeDirection, cfg: OutputConfig) {}
}

class TileEuOutputLV extends TileEuOutputBase(128)

class TileEuOutputMV extends TileEuOutputBase(512)

class TileEuOutputHV extends TileEuOutputBase(2048)
