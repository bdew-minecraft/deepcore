/*
 * Copyright (c) bdew, 2014
 * https://github.com/bdew/deepcore
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * https://raw.github.com/bdew/deepcore/master/MMPL-1.0.txt
 */

package net.bdew.deepcore.nei

import codechicken.nei.api.{API, IConfigureNEI}
import net.minecraftforge.fluids.{FluidStack, FluidRegistry}
import net.minecraft.item.ItemStack
import net.bdew.deepcore.config.Items

class NEIDeepcoreConfig extends IConfigureNEI {
  def loadConfig() {
    import scala.collection.JavaConversions._
    for ((name, fluid) <- FluidRegistry.getRegisteredFluids) {
      val stack = new ItemStack(Items.canister)
      Items.canister.fill(stack, new FluidStack(fluid, Items.canister.capacity), true)
      API.addNBTItem(stack)
    }
    val stack = new ItemStack(Items.canister)
    API.addNBTItem(stack)
  }

  def getName: String = "Deepcore"
  def getVersion: String = "DEEPCORE_VER"
}
