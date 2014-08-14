/*
 * Copyright (c) bdew, 2014
 * https://github.com/bdew/deepcore
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * https://raw.github.com/bdew/deepcore/master/MMPL-1.0.txt
 */

package net.bdew.deepcore.multiblock.interact

import net.bdew.deepcore.multiblock.tile.TileController
import net.minecraftforge.fluids.{Fluid, FluidStack, FluidTankInfo}

trait CIFluidInput extends TileController {
  def inputFluid(resource: FluidStack, doFill: Boolean): Int
  def canInputFluid(fluid: Fluid): Boolean
  def getTankInfo: Array[FluidTankInfo]
}
