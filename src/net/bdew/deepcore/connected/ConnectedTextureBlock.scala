/*
 * Copyright (c) bdew, 2014
 * https://github.com/bdew/deepcore
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * https://raw.github.com/bdew/deepcore/master/MMPL-1.0.txt
 */

package net.bdew.deepcore.connected

import net.minecraft.block.Block
import net.minecraft.util.Icon
import net.minecraft.world.IBlockAccess

trait ConnectedTextureBlock extends Block {
  def edgeIcon: Icon
  def canConnect(world: IBlockAccess, ox: Int, oy: Int, oz: Int, tx: Int, ty: Int, tz: Int): Boolean

  override def getRenderType = ConnectedRenderer.id
}
