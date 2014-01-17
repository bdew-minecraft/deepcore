/*
 * Copyright (c) bdew, 2014
 * https://github.com/bdew/deepcore
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * https://raw.github.com/bdew/deepcore/master/MMPL-1.0.txt
 */

package net.bdew.deepcore.blocks.turbine

import net.bdew.deepcore.blocks.BaseModule
import net.minecraft.client.renderer.texture.IconRegister
import cpw.mods.fml.relauncher.{SideOnly, Side}
import net.minecraft.util.Icon

class BlockTurbine extends BaseModule("Turbine", "Turbine", classOf[TileTurbine]) {
  var topIcon: Icon = null

  @SideOnly(Side.CLIENT)
  override def regIcons(ir: IconRegister) {
    topIcon = ir.registerIcon("deepcore:modules/" + name.toLowerCase + "/top")
  }
}