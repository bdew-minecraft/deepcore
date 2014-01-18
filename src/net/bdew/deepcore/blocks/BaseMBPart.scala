/*
 * Copyright (c) bdew, 2014
 * https://github.com/bdew/deepcore
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * https://raw.github.com/bdew/deepcore/master/MMPL-1.0.txt
 */

package net.bdew.deepcore.blocks

import cpw.mods.fml.relauncher.{Side, SideOnly}
import net.minecraft.client.renderer.texture.IconRegister
import net.minecraft.block.Block
import net.minecraft.block.material.Material

abstract class BaseMBPart(id: Int, material: Material) extends Block(id, material) {
  val name: String

  setUnlocalizedName("deepcore."+name)
  setHardness(2)

  @SideOnly(Side.CLIENT)
  override final def registerIcons(ir: IconRegister) {
    blockIcon = ir.registerIcon("deepcore:" + name.toLowerCase + "/main")
    regIcons(ir)
  }

  @SideOnly(Side.CLIENT)
  def regIcons(ir: IconRegister) {}

}
