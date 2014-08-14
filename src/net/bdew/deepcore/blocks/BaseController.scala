/*
 * Copyright (c) bdew, 2013 - 2014
 * https://github.com/bdew/deepcore
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * https://raw.github.com/bdew/deepcore/master/MMPL-1.0.txt
 */

package net.bdew.deepcore.blocks

import cpw.mods.fml.relauncher.{Side, SideOnly}
import net.bdew.deepcore.connected.IconCache
import net.bdew.deepcore.multiblock.block.BlockController
import net.bdew.deepcore.multiblock.tile.TileController
import net.bdew.lib.Misc
import net.bdew.lib.block.NamedBlock
import net.minecraft.block.material.Material
import net.minecraft.client.renderer.texture.IIconRegister

class BaseController[T <: TileController](name: String, TEClass: Class[T])
  extends BlockController(name, Material.iron, TEClass) with NamedBlock {
  val mod = Misc.getActiveModId
  def edgeIcon = IconCache.edgeIcon

  setBlockName(mod + "." + name)
  setHardness(1)

  @SideOnly(Side.CLIENT)
  override def registerBlockIcons(ir: IIconRegister) {
    blockIcon = ir.registerIcon(mod + ":" + name.toLowerCase + "/main")
    regIcons(ir)
  }

  @SideOnly(Side.CLIENT)
  def regIcons(ir: IIconRegister) {}
}
