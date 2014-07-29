/*
 * Copyright (c) bdew, 2014
 * https://github.com/bdew/deepcore
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * https://raw.github.com/bdew/deepcore/master/MMPL-1.0.txt
 */

package net.bdew.deepcore.blocks.turbineController

import net.bdew.deepcore.Deepcore
import net.bdew.deepcore.config.Modules
import net.bdew.deepcore.multiblock.interact.{CIFluidInput, CIOutputFaces, CIPowerProducer}
import net.bdew.deepcore.multiblock.tile.TileCore
import net.bdew.lib.Misc
import net.bdew.lib.data.base.UpdateKind
import net.bdew.lib.data.{DataSlotFloat, DataSlotInt, DataSlotTank}
import net.bdew.lib.power.DataSlotPower
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.util.{ChatComponentTranslation, ChatStyle, EnumChatFormatting}
import net.minecraftforge.fluids.{Fluid, FluidStack}

class TileTurbineController extends TileCore with CIFluidInput with CIOutputFaces with CIPowerProducer {
  val cfg = MachineTurbine

  val fuel = new DataSlotTank("fuel", this, 0)
  val power = new DataSlotPower("power", this)

  val mjPerTick = new DataSlotFloat("mjPerTick", this).setUpdate(UpdateKind.GUI, UpdateKind.SAVE)
  val burnTime = new DataSlotFloat("burnTime", this).setUpdate(UpdateKind.SAVE)

  val mjPerTickAvg = new DataSlotFloat("mjAvg", this).setUpdate(UpdateKind.GUI)
  val numTurbines = new DataSlotInt("turbines", this).setUpdate(UpdateKind.GUI)
  val fuelPerTick = new DataSlotFloat("fuelPerTick", this).setUpdate(UpdateKind.GUI)

  lazy val maxOutputs = 6

  final val decay = 0.5F

  def updateAvg(v: Float) {
    mjPerTickAvg := mjPerTickAvg * decay + (1 - decay) * v
  }

  def doUpdate() {
    val fuelPerMj = if (fuel.getFluidAmount > 0) 1 / cfg.getFuelValue(fuel.getFluid.getFluid.getName) * cfg.fuelConsumptionMultiplier else 0
    fuelPerTick := fuelPerMj * mjPerTick

    if (burnTime < 5 && fuelPerMj > 0 && mjPerTick > 0) {
      val needFuel = Misc.clamp((10 * fuelPerTick).ceil, 0F, fuel.getFluidAmount.toFloat).floor.toInt
      burnTime += needFuel / fuelPerTick
      fuel.drain(needFuel, true)
    }

    if (burnTime > 1 && power.capacity - power.stored > mjPerTick) {
      burnTime -= 1
      power.stored += mjPerTick
      updateAvg(mjPerTick)
      lastChange = worldObj.getTotalWorldTime
    } else {
      updateAvg(0)
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
    numTurbines := getNumOfMoudules("Turbine")
    mjPerTick := numTurbines * cfg.mjPerTickPerTurbine
  }

  def onClick(player: EntityPlayer) = {
    val missing = cfg.required.filter({ case (mod, cnt) => getNumOfMoudules(mod) < cnt })
    if (missing.size > 0) {
      player.addChatMessage(new ChatComponentTranslation("deepcore.message.incomplete")
        .setChatStyle(new ChatStyle().setColor(EnumChatFormatting.RED)))
      for ((mod, cnt) <- missing)
        player.addChatMessage(
          new ChatComponentTranslation("- %s %s", Integer.valueOf(cnt),
            new ChatComponentTranslation("deepcore.module." + mod + ".name"))
            .setChatStyle(new ChatStyle().setColor(EnumChatFormatting.RED)))
    } else player.openGui(Deepcore, cfg.guiId, worldObj, xCoord, yCoord, zCoord)
  }
}
