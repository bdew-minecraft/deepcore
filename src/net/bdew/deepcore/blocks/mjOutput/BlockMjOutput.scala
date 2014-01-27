/*
 * Copyright (c) bdew, 2014
 * https://github.com/bdew/deepcore
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * https://raw.github.com/bdew/deepcore/master/MMPL-1.0.txt
 */

package net.bdew.deepcore.blocks.mjOutput

import net.bdew.deepcore.blocks.{BaseOutputBlock, BaseModule}
import net.bdew.deepcore.connected.{IconCache, IconColor, BlockAdditionalRender}
import net.minecraft.world.IBlockAccess
import net.minecraftforge.common.ForgeDirection
import net.bdew.deepcore.multiblock.interact.CIOutputFaces
import net.bdew.deepcore.multiblock.data.BlockFace
import net.bdew.deepcore.multiblock.Outputs

class BlockMjOutput extends BaseOutputBlock("MJOutput", "PowerOutput", classOf[TileMjOutput])