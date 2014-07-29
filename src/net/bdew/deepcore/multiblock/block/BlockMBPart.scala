/*
 * Copyright (c) bdew, 2014
 * https://github.com/bdew/deepcore
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * https://raw.github.com/bdew/deepcore/master/MMPL-1.0.txt
 */

package net.bdew.deepcore.multiblock.block

import cpw.mods.fml.relauncher.{Side, SideOnly}
import net.minecraft.block.Block
import net.minecraft.block.material.Material
import net.minecraft.client.renderer.texture.IIconRegister

abstract class BlockMBPart(material: Material) extends Block(material) {
  val name: String

  setBlockName("deepcore." + name)
  setHardness(2)

  @SideOnly(Side.CLIENT)
  override def registerBlockIcons(ir: IIconRegister) {
    blockIcon = ir.registerIcon("deepcore:" + name.toLowerCase + "/main")
    regIcons(ir)
  }

  @SideOnly(Side.CLIENT)
  def regIcons(ir: IIconRegister) {}
}
