/*
 * Copyright (c) bdew, 2014
 * https://github.com/bdew/deepcore
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * https://raw.github.com/bdew/deepcore/master/MMPL-1.0.txt
 */

package net.bdew.deepcore.blocks.turbineController

import cpw.mods.fml.relauncher.{Side, SideOnly}
import net.bdew.deepcore.connected.IconCache
import net.bdew.deepcore.multiblock.block.BlockController
import net.minecraft.client.renderer.texture.IIconRegister
import net.minecraft.util.IIcon
import net.minecraftforge.common.util.ForgeDirection

object BlockTurbineController extends BlockController("TurbineController", classOf[TileTurbineController]) {
  var topIcon: IIcon = null
  var bottomIcon: IIcon = null

  def edgeIcon = IconCache.edgeIcon

  @SideOnly(Side.CLIENT)
  override def regIcons(ir: IIconRegister) {
    topIcon = ir.registerIcon("deepcore:" + name.toLowerCase + "/top")
    bottomIcon = ir.registerIcon("deepcore:" + name.toLowerCase + "/bottom")
  }

  override def getIcon(side: Int, meta: Int) =
    if (side == ForgeDirection.UP.ordinal())
      topIcon
    else if (side == ForgeDirection.DOWN.ordinal())
      bottomIcon
    else
      blockIcon

}
