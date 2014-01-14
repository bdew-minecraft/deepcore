/*
 * Copyright (c) bdew, 2014
 * https://github.com/bdew/deepcore
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * https://raw.github.com/bdew/deepcore/master/MMPL-1.0.txt
 */

package net.bdew.deepcore.connected.test

import net.minecraft.block.Block
import net.minecraft.block.material.Material
import net.bdew.deepcore.connected.{ConnectedRenderer, ConnectedTextureBlock}
import net.minecraft.util.Icon
import net.minecraft.world.IBlockAccess
import cpw.mods.fml.relauncher.{Side, SideOnly}
import net.minecraft.client.renderer.texture.IconRegister
import net.bdew.deepcore.multiblock.BlockModule
import net.bdew.lib.block.HasTE
import net.minecraft.tileentity.TileEntity

class BlockTestModule1(id: Int) extends Block(id, Material.iron) with BlockModule[TileTestModule1] {
  val TEClass = classOf[TileTestModule1]
  val kind: String = "TestMod1"

  var edgeIcon: Icon = null

  setUnlocalizedName("testmod1")

  @SideOnly(Side.CLIENT)
  override def registerIcons(ir: IconRegister) {
    blockIcon = ir.registerIcon("deepcore:connected/module1")
    edgeIcon = ir.registerIcon("deepcore:connected/edge")
  }

  override def getRenderType = ConnectedRenderer.id
}
