/*
 * Copyright (c) bdew, 2014
 * https://github.com/bdew/deepcore
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * https://raw.github.com/bdew/deepcore/master/MMPL-1.0.txt
 */

package net.bdew.deepcore.blocks.turbineController

import net.bdew.lib.data.{DataSlotFloat, DataSlotTank}
import net.minecraft.entity.player.EntityPlayer
import net.bdew.deepcore.config.{Machines, Modules}
import net.minecraftforge.fluids.{FluidStack, Fluid}
import net.bdew.deepcore.Deepcore
import net.bdew.lib.power.DataSlotPower
import net.bdew.lib.Misc
import net.bdew.lib.data.base.UpdateKind
import net.bdew.deepcore.multiblock.interact.{CIPowerProducer, CIOutputFaces, CIFluidInput}
import net.bdew.deepcore.multiblock.tile.TileCore

class TileTurbineController extends TileCore with CIFluidInput with CIOutputFaces with CIPowerProducer {
  val cfg = Machines.turbine

  val fuel = new DataSlotTank("fuel", this, 0)
  val power = new DataSlotPower("power", this)

  val mjPerTick = new DataSlotFloat("mjPerTick", this).setUpdate(UpdateKind.GUI, UpdateKind.SAVE)
  val burnTime = new DataSlotFloat("burnTime", this).setUpdate(UpdateKind.SAVE)
  val mjPerTickAvg = new DataSlotFloat("mjAvg", this).setUpdate(UpdateKind.GUI)

  lazy val maxOutputs = 6

  def doUpdate() {
    val fuelPerMj = if (fuel.getFluidAmount > 0) 1 / cfg.getFuelValue(fuel.getFluid.getFluid.getName) * cfg.fuelConsumptionMultiplier else 0
    val fuelPerTick = fuelPerMj * mjPerTick

    if (burnTime < 5 && fuelPerMj > 0 && mjPerTick > 0) {
      val needFuel = Misc.clamp((10 * fuelPerTick).ceil, 0F, fuel.getFluidAmount.toFloat).floor.toInt
      burnTime += needFuel / fuelPerTick
      fuel.drain(needFuel, true)
    }

    if (burnTime > 1 && power.capacity - power.stored > mjPerTick) {
      burnTime -= 1
      power.stored += mjPerTick
      lastChange = worldObj.getTotalWorldTime
    }
  }

  serverTick.listen(doUpdate)

  def inputFluid(resource: FluidStack, doFill: Boolean): Int =
    if (canInputFluid(resource.getFluid)) fuel.fill(resource, doFill) else 0

  def canInputFluid(fluid: Fluid) = cfg.getFuelValue(fluid.getName) > 0
  def getTankInfo = Array(fuel.getInfo)

  def extract(v: Float, simulate: Boolean) = power.extract(v, simulate)

  def onModulesChanged() {
    fuel.setCapacity(getNumOfMoudules("FuelTank") * Modules.FuelTank.capacity + cfg.internalFuelCapacity)
    power.capacity = getNumOfMoudules("PowerCapacitor") * Modules.PowerCapacitor.capacity + cfg.internalPowerCapacity
    mjPerTick := getNumOfMoudules("Turbine") * cfg.mjPerTickPerTurbine
  }

  def onClick(player: EntityPlayer) = {
    val missing = cfg.required.filter({ case (mod, cnt) => getNumOfMoudules(mod) < cnt })
    if (missing.size > 0) {
      player.addChatMessage(Misc.toLocal("deepcore.message.incomplete"))
      for ((mod, cnt) <- missing)
        player.addChatMessage("- %d %s".format(cnt, Misc.toLocal("deepcore.module." + mod + ".name")))
    } else player.openGui(Deepcore, cfg.guiId, worldObj, xCoord, yCoord, zCoord)
  }
}
