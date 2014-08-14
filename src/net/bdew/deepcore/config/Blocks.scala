/*
 * Copyright (c) bdew, 2014
 * https://github.com/bdew/deepcore
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * https://raw.github.com/bdew/deepcore/master/MMPL-1.0.txt
 */

package net.bdew.deepcore.config

import net.bdew.deepcore.CreativeTabsDeepcore
import net.bdew.deepcore.blocks.euOutput.{BlockEuOutputHV, BlockEuOutputLV, BlockEuOutputMV}
import net.bdew.deepcore.blocks.fluidInput.BlockFluidInput
import net.bdew.deepcore.blocks.fuelTank.BlockFuelTank
import net.bdew.deepcore.blocks.mjOutput.BlockMjOutput
import net.bdew.deepcore.blocks.powerCapacitor.BlockPowerCapacitor
import net.bdew.deepcore.blocks.rfOutput.BlockRfOutput
import net.bdew.deepcore.blocks.turbine.BlockTurbine
import net.bdew.deepcore.compat.PowerProxy
import net.bdew.lib.config.BlockManager

object Blocks extends BlockManager(CreativeTabsDeepcore.main) {
  regBlock(BlockFluidInput)

  if (PowerProxy.haveBC)
    regBlock(BlockMjOutput)

  if (PowerProxy.haveIC2) {
    regBlock(BlockEuOutputLV)
    regBlock(BlockEuOutputMV)
    regBlock(BlockEuOutputHV)
  }

  if (PowerProxy.haveTE)
    regBlock(BlockRfOutput)

  regBlock(BlockTurbine)
  regBlock(BlockFuelTank)
  regBlock(BlockPowerCapacitor)
}