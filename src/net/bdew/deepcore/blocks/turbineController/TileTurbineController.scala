/*
 * Copyright (c) bdew, 2014
 * https://github.com/bdew/deepcore
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * https://raw.github.com/bdew/deepcore/master/MMPL-1.0.txt
 */

package net.bdew.deepcore.blocks.turbineController

import net.bdew.deepcore.multiblock.{CIFluidInput, TileCore}
import net.bdew.lib.data.DataSlotTank
import net.minecraft.entity.player.EntityPlayer
import net.bdew.deepcore.config.{Machines, Modules}
import net.minecraftforge.fluids.{FluidStack, Fluid}
import net.bdew.deepcore.Deepcore

class TileTurbineController extends TileCore with CIFluidInput {
  val canAccept = Map(
    "PowerOutput" -> 10,
    "Turbine" -> 20,
    "FluidInput" -> 5,
    "FuelTank" -> 10
  )

  val cfg = Machines.turbine

  val fuel = new DataSlotTank("fuel", this, 0)

  def inputFluid(resource: FluidStack, doFill: Boolean): Int =
    if (canInputFluid(resource.getFluid)) fuel.fill(resource, doFill) else 0

  def canInputFluid(fluid: Fluid) = cfg.getFuelValue(fluid.getName) > 0
  def getTankInfo = Array(fuel.getInfo)

  def onModulesChanged() {
    fuel.setCapacity(getNumOfMoudules("FuelTank") * Modules.FuelTank.capacity)
  }

  def onClick(player: EntityPlayer) = player.openGui(Deepcore, cfg.guiId, worldObj, xCoord, yCoord, zCoord)
}
