/*
 * Copyright (c) bdew, 2014
 * https://github.com/bdew/deepcore
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * https://raw.github.com/bdew/deepcore/master/MMPL-1.0.txt
 */

package net.bdew.deepcore.blocks

import net.minecraft.block.Block
import net.bdew.deepcore.config.Config
import net.minecraft.block.material.Material
import net.bdew.deepcore.multiblock.{BlockCore, TileCore, BlockModule, TileModule}

class BaseController[T <: TileCore](val name: String, val TEClass: Class[T])
  extends BaseMBPart(Config.IDs.getBlockId(name), Material.iron)
  with BlockCore[T]


