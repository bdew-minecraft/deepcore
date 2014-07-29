/*
 * Copyright (c) bdew, 2014
 * https://github.com/bdew/deepcore
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * https://raw.github.com/bdew/deepcore/master/MMPL-1.0.txt
 */

package net.bdew.deepcore.blocks.turbine

import cpw.mods.fml.relauncher.{Side, SideOnly}
import net.bdew.deepcore.multiblock.block.BlockModule
import net.minecraft.client.renderer.texture.IIconRegister
import net.minecraft.util.IIcon
import net.minecraftforge.common.util.ForgeDirection

object BlockTurbine extends BlockModule("Turbine", "Turbine", classOf[TileTurbine]) {
  var topIcon: IIcon = null

  @SideOnly(Side.CLIENT)
  override def regIcons(ir: IIconRegister) {
    topIcon = ir.registerIcon("deepcore:" + name.toLowerCase + "/top")
  }

  override def getIcon(side: Int, meta: Int) =
    if (side == ForgeDirection.UP.ordinal() || side == ForgeDirection.DOWN.ordinal()) topIcon else blockIcon
}