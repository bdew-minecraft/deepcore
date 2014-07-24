/*
 * Copyright (c) bdew, 2014
 * https://github.com/bdew/deepcore
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * https://raw.github.com/bdew/deepcore/master/MMPL-1.0.txt
 */

package net.bdew.deepcore.multiblock.interact

import net.bdew.deepcore.multiblock.tile.TileModule
import net.minecraftforge.common.util.ForgeDirection
import net.bdew.deepcore.multiblock.data.OutputConfig

trait MIOutput extends TileModule {
  def doOutput(face: ForgeDirection, cfg: OutputConfig)
  def makeCfgObject(face: ForgeDirection): OutputConfig
}
