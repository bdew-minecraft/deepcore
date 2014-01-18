/*
 * Copyright (c) bdew, 2014
 * https://github.com/bdew/deepcore
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * https://raw.github.com/bdew/deepcore/master/MMPL-1.0.txt
 */

package net.bdew.deepcore.blocks

import net.bdew.deepcore.config.Config
import net.minecraft.block.material.Material
import net.bdew.deepcore.multiblock.block.BlockModule
import net.bdew.deepcore.multiblock.tile.TileModule

class BaseModule[T <: TileModule](val name: String, val kind: String, val TEClass: Class[T])
  extends BaseMBPart(Config.IDs.getBlockId(name), Material.iron)
  with BlockModule[T]
