/*
 * Copyright (c) bdew, 2013 - 2014
 * https://github.com/bdew/deepcore
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * https://raw.github.com/bdew/deepcore/master/MMPL-1.0.txt
 */

package net.bdew.deepcore.blocks.rfOutput

import net.bdew.deepcore.multiblock.block.BlockOutput
import cpw.mods.fml.relauncher.{Side, SideOnly}
import net.minecraft.client.renderer.texture.IconRegister
import net.minecraft.util.Icon
import net.minecraft.world.IBlockAccess
import net.minecraftforge.common.ForgeDirection

class BlockRfOutput extends BlockOutput("RFOutput", "PowerOutput", classOf[TileRfOutput]) {
  var enabledIcon: Icon = null

  override def getBlockTexture(world: IBlockAccess, x: Int, y: Int, z: Int, side: Int) =
    if (getTE(world, x, y, z).canConnectoToFace(ForgeDirection.values()(side)))
      enabledIcon
    else
      blockIcon

  @SideOnly(Side.CLIENT)
  override def registerIcons(ir: IconRegister) {
    blockIcon = ir.registerIcon("deepcore:rfoutput/disabled")
    enabledIcon = ir.registerIcon("deepcore:rfoutput/main")
  }
}