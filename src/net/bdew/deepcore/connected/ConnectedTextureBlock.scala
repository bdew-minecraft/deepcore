/*
 * Copyright (c) bdew, 2014
 * https://github.com/bdew/deepcore
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * https://raw.github.com/bdew/deepcore/master/MMPL-1.0.txt
 */

package net.bdew.deepcore.connected

import net.bdew.lib.block.BlockRef
import net.minecraft.block.Block
import net.minecraft.util.IIcon
import net.minecraft.world.IBlockAccess

trait ConnectedTextureBlock extends Block {
  def edgeIcon: IIcon
  def canConnect(world: IBlockAccess, origin: BlockRef, target: BlockRef): Boolean

  override def getRenderType = ConnectedRenderer.id
}
