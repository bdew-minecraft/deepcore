/*
 * Copyright (c) bdew, 2014
 * https://github.com/bdew/deepcore
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * https://raw.github.com/bdew/deepcore/master/MMPL-1.0.txt
 */

package net.bdew.deepcore.gui

import net.bdew.lib.gui.{TextureLocationScaled, TextureLocation}
import net.minecraft.util.ResourceLocation
import net.bdew.deepcore.Deepcore

object Textures {
  val texture = new ResourceLocation(Deepcore.modId + ":textures/gui/widgets.png")
  val tankOverlay = new TextureLocation(texture, 10, 0)
  val powerFill = new TextureLocation(texture, 0, 0)
  val slotSelect = new TextureLocation(texture, 20, 0)

  object Button16 {
    val base = new TextureLocation(texture, 20, 18)
    val hover = new TextureLocation(texture, 36, 18)
    val rsOn = new TextureLocation(texture, 37, 35)
    val rsOff = new TextureLocation(texture, 21, 35)
    val enabled = new TextureLocation(texture, 53, 35)
    val disabled = new TextureLocation(texture, 69, 35)
  }

  object Icons {
    val turbine = new TextureLocationScaled(texture, 0, 90, 32, 32)
    val out = new TextureLocationScaled(texture, 32, 90, 16, 16)
    val peak = new TextureLocationScaled(texture, 32, 106, 16, 16)
    val power = new TextureLocationScaled(texture, 48, 90, 16, 16)
    val fluid = new TextureLocationScaled(texture, 64, 90, 32, 32)
  }

  def greenProgress(width: Int) = new TextureLocation(texture, 136 - width, 59)
  def whiteProgress(width: Int) = new TextureLocation(texture, 136 - width, 74)
}
