/*
 * Copyright (c) bdew, 2014
 * https://github.com/bdew/deepcore
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * https://raw.github.com/bdew/deepcore/master/MMPL-1.0.txt
 */

package net.bdew.deepcore.blocks.mjOutput

import net.bdew.deepcore.multiblock.block.BlockOutput
import net.minecraft.util.Icon
import net.minecraft.world.IBlockAccess
import net.minecraftforge.common.ForgeDirection
import cpw.mods.fml.relauncher.{Side, SideOnly}
import net.minecraft.client.renderer.texture.IconRegister

class BlockMjOutput extends BlockOutput("MJOutput", "PowerOutput", classOf[TileMjOutput]) {
  var enabledIcon: Icon = null

  override def getBlockTexture(world: IBlockAccess, x: Int, y: Int, z: Int, side: Int) =
    if (getTE(world, x, y, z).canConnectoToFace(ForgeDirection.values()(side)))
      enabledIcon
    else
      blockIcon

  @SideOnly(Side.CLIENT)
  override def registerIcons(ir: IconRegister) {
    blockIcon = ir.registerIcon("deepcore:mjoutput/disabled")
    enabledIcon = ir.registerIcon("deepcore:mjoutput/main")
  }
}