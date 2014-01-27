/*
 * Copyright (c) bdew, 2014
 * https://github.com/bdew/deepcore
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * https://raw.github.com/bdew/deepcore/master/MMPL-1.0.txt
 */

package net.bdew.deepcore.config

import net.bdew.lib.config.BlockManager
import net.bdew.deepcore.blocks.BaseMBPart
import net.bdew.deepcore.blocks.fluidInput.BlockFluidInput
import net.bdew.deepcore.blocks.mjOutput.BlockMjOutput
import net.bdew.deepcore.compat.PowerProxy
import net.bdew.deepcore.blocks.turbine.BlockTurbine
import net.bdew.deepcore.blocks.fuelTank.BlockFuelTank
import net.bdew.deepcore.blocks.powerCapactor.BlockPowerCapacitor
import net.bdew.deepcore.blocks.euOutput.{BlockEuOutputLV, BlockEuOutputMV, BlockEuOutputHV}

object Blocks extends BlockManager(Config.IDs) {
  def regMBPart[T <: BaseMBPart](block: T): T = regBlock[T](block, block.name)

  regMBPart(new BlockFluidInput)

  if (PowerProxy.haveBC)
    regMBPart(new BlockMjOutput)

  if (PowerProxy.haveIC2) {
    regMBPart(new BlockEuOutputLV)
    regMBPart(new BlockEuOutputMV)
    regMBPart(new BlockEuOutputHV)
  }

  regMBPart(new BlockTurbine)
  regMBPart(new BlockFuelTank)
  regMBPart(new BlockPowerCapacitor)
}