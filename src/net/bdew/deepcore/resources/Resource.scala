/*
 * Copyright (c) bdew, 2013 - 2014
 * https://github.com/bdew/deepcore
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * https://raw.github.com/bdew/deepcore/master/MMPL-1.0.txt
 */

package net.bdew.deepcore.resources

import net.bdew.deepcore.resources.map.ResourceMapGen
import net.bdew.lib.Misc
import net.bdew.lib.gui.Texture
import net.bdew.lib.recipes.gencfg.ConfigSection
import net.minecraft.util.IIcon

class Resource(val name: String, definition: ConfigSection, amap: ResourceMapGen = null) {
  val id = definition.getInt("ID")
  val map = if (amap == null) ResourceManager.mapGenFromCfg(name, definition) else amap
  var moduleIcon: IIcon = null
  var resTexture: Texture = null

  val color1 = definition.getColor("Color1")
  val color2 = definition.getColor("Color2")

  val depthMin = definition.getIntList("Depth")(0)
  val depthMax = definition.getIntList("Depth")(1)

  val abundanceMin = definition.getIntList("Abundance")(0)
  val abundanceMax = definition.getIntList("Abundance")(1)

  def depthFromVal(v: Float) = (depthMin + (1 - v) * (depthMax - depthMin)).round
  def abundanceFromVal(v: Float) = (abundanceMin + v * (abundanceMax - abundanceMin)).round

  def getLocalizedName = Misc.toLocal("deepcore.resource." + name)
}

