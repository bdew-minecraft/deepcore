/*
 * Copyright (c) bdew, 2014
 * https://github.com/bdew/deepcore
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * https://raw.github.com/bdew/deepcore/master/MMPL-1.0.txt
 */

package net.bdew.deepcore.connected

import net.minecraft.block.material.Material
import net.minecraft.block.Block
import net.minecraft.client.renderer.texture.IconRegister
import cpw.mods.fml.relauncher.{Side, SideOnly}
import net.minecraft.util.Icon
import net.minecraft.world.IBlockAccess

class ConnectedBlock(id: Int) extends Block(id, Material.iron) with ConnectedTextureBlock {
  var edgeIcon: Icon = null

  setUnlocalizedName("connected")

  def canConnect(world: IBlockAccess, ox: Int, oy: Int, oz: Int, tx: Int, ty: Int, tz: Int): Boolean = world.getBlockId(tx, ty, tz) == blockID

  @SideOnly(Side.CLIENT)
  override def registerIcons(ir: IconRegister) {
    blockIcon = ir.registerIcon("deepcore:connected/side")
    edgeIcon = ir.registerIcon("deepcore:connected/edge")
  }

  override def getRenderType = ConnectedRenderer.id
}
