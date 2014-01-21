/*
 * Copyright (c) bdew, 2014
 * https://github.com/bdew/deepcore
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * https://raw.github.com/bdew/deepcore/master/MMPL-1.0.txt
 */

package net.bdew.deepcore.gui

import net.bdew.lib.gui.TextureLocation
import net.minecraft.util.ResourceLocation
import net.bdew.deepcore.Deepcore

object Textures {
  val texture = new ResourceLocation(Deepcore.modId + ":textures/gui/widgets.png")
  val tankOverlay = new TextureLocation(texture, 10, 0)
  val powerFill = new TextureLocation(texture, 0, 0)
  val slotSelect = new TextureLocation(texture, 20, 0)
  def greenProgress(width: Int) = new TextureLocation(texture, 136 - width, 59)
  def whiteProgress(width: Int) = new TextureLocation(texture, 136 - width, 74)
}
