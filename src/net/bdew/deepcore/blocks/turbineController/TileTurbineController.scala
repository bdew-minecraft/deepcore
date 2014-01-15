/*
 * Copyright (c) bdew, 2014
 * https://github.com/bdew/deepcore
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * https://raw.github.com/bdew/deepcore/master/MMPL-1.0.txt
 */

package net.bdew.deepcore.blocks.turbineController

import net.bdew.deepcore.multiblock.TileCore

class TileTurbineController extends TileCore {
  val canAccept = Map(
    "PowerOutput" -> 10,
    "Turbine" -> 20,
    "FluidInput" -> 5
  )
}
