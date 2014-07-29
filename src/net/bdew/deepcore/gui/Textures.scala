/*
 * Copyright (c) bdew, 2014
 * https://github.com/bdew/deepcore
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * https://raw.github.com/bdew/deepcore/master/MMPL-1.0.txt
 */

package net.bdew.deepcore.gui

import net.bdew.deepcore.Deepcore
import net.bdew.lib.gui._

object Textures {
  val sheet = new ScaledResourceLocation(Deepcore.modId, "textures/gui/widgets.png")
  val tankOverlay = Texture(sheet, 10, 0, 9, 58)
  val powerFill = Texture(sheet, 0, 0, 9, 58)
  val slotSelect = Texture(sheet, 20, 0, 18, 18)

  object Button16 {
    val base = Texture(sheet, 20, 18, 16, 16)
    val hover = Texture(sheet, 36, 18, 16, 16)
    val rsOn = Texture(sheet, 37, 35, 14, 14)
    val rsOff = Texture(sheet, 21, 35, 14, 14)
    val enabled = Texture(sheet, 53, 35, 14, 14)
    val disabled = Texture(sheet, 69, 35, 14, 14)
  }

  object Icons {
    val turbine = Texture(sheet, 0, 90, 32, 32)
    val out = Texture(sheet, 32, 90, 16, 16)
    val peak = Texture(sheet, 32, 106, 16, 16)
    val power = Texture(sheet, 48, 90, 16, 16)
    val fluid = Texture(sheet, 64, 90, 32, 32)
  }

  val arrow = Texture(sheet, 56, 1, 16, 16)
  val indicator = Texture(sheet, 20, 50, 3, 8)

  def greenProgress(width: Int) = Texture(sheet, 136 - width, 59, width, 16)
  def whiteProgress(width: Int) = Texture(sheet, 136 - width, 74, width, 16)
}
