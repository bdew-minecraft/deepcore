/*
 * Copyright (c) bdew, 2014
 * https://github.com/bdew/deepcore
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * https://raw.github.com/bdew/deepcore/master/MMPL-1.0.txt
 */

package net.bdew.deepcore.blocks.fluidInput

import net.minecraftforge.fluids.{Fluid, FluidTankInfo, IFluidHandler, FluidStack}
import net.minecraftforge.common.ForgeDirection
import net.bdew.deepcore.multiblock.interact.CIFluidInput
import net.bdew.deepcore.multiblock.tile.TileModule

class TileFluidInput extends TileModule with IFluidHandler {
  val kind: String = "FluidInput"

  def getCore = if (connected.cval == null) None else connected.getTile(worldObj, classOf[CIFluidInput])

  def fill(from: ForgeDirection, resource: FluidStack, doFill: Boolean): Int =
    getCore.getOrElse(return 0).inputFluid(resource, doFill)

  def canFill(from: ForgeDirection, fluid: Fluid): Boolean =
    getCore.getOrElse(return false).canInputFluid(fluid)

  def getTankInfo(from: ForgeDirection): Array[FluidTankInfo] =
    getCore.getOrElse(return Array.empty).getTankInfo

  def canDrain(from: ForgeDirection, fluid: Fluid): Boolean = false
  def drain(from: ForgeDirection, resource: FluidStack, doDrain: Boolean): FluidStack = null
  def drain(from: ForgeDirection, maxDrain: Int, doDrain: Boolean): FluidStack = null
}
