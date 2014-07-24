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
import net.bdew.deepcore.items.Canister
import net.minecraft.item.ItemStack
import net.minecraftforge.fluids.{FluidRegistry, FluidStack}

class NEIDeepcoreConfig extends IConfigureNEI {
  def loadConfig() {
    import scala.collection.JavaConversions._
    for ((name, fluid) <- FluidRegistry.getRegisteredFluids) {
      val stack = new ItemStack(Canister)
      Canister.fill(stack, new FluidStack(fluid, Canister.capacity), true)
      API.addItemListEntry(stack)
    }
    val stack = new ItemStack(Canister)
    API.addItemListEntry(stack)
  }

  def getName: String = "Deepcore"
  def getVersion: String = "DEEPCORE_VER"
}
