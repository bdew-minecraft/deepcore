/*
 * Copyright (c) bdew, 2014
 * https://github.com/bdew/deepcore
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * https://raw.github.com/bdew/deepcore/master/MMPL-1.0.txt
 */

package net.bdew.deepcore.blocks.turbineController

import net.bdew.lib.machine.Machine
import net.bdew.lib.gui.GuiProvider
import net.minecraft.entity.player.EntityPlayer
import cpw.mods.fml.relauncher.{Side, SideOnly}

class MachineTurbine extends Machine("TurbineController", x => new BlockTurbineController) with GuiProvider {
  def guiId: Int = 1
  type TEClass = TileTurbineController

  lazy val fuelVals = tuning.getSection("FuelValues")
  lazy val mjPerTickPerTurbine = tuning.getFloat("MJPerTickPerTurbine")
  lazy val fuelConsumptionMultiplier = tuning.getFloat("FuelConsumptionMultiplier")
  lazy val internalPowerCapacity = tuning.getInt("InternalPowerCapacity")
  lazy val internalFuelCapacity = tuning.getInt("InternalFuelCapacity")

  def getFuelValue(fluid: String) =
    if (fuelVals.hasValue(fluid)) fuelVals.getFloat(fluid) else 0F

  @SideOnly(Side.CLIENT)
  def getGui(te: TileTurbineController, player: EntityPlayer) = new GuiTurbine(te, player)
  def getContainer(te: TileTurbineController, player: EntityPlayer) = new ContainerTurbine(te, player)
}
