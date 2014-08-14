/*
 * Copyright (c) bdew, 2013 - 2014
 * https://github.com/bdew/deepcore
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * https://raw.github.com/bdew/deepcore/master/MMPL-1.0.txt
 */

package net.bdew.deepcore.gui

import net.bdew.lib.gui.Color
import net.bdew.lib.multiblock.ResourceProvider

object DeepcoreResourceProvider extends ResourceProvider {
  override def edge = IconCache.edgeIcon
  override def output = IconCache.output
  override def disabled = IconCache.disabled

  override def arrowBottom = IconCache.arBottom
  override def arrowRight = IconCache.arRight
  override def arrowTop = IconCache.arTop
  override def arrowLeft = IconCache.arLeft

  override def btRsOff = Textures.Button16.rsOff
  override def btRsOn = Textures.Button16.rsOn
  override def btDisabled = Textures.Button16.disabled
  override def btEnabled = Textures.Button16.enabled

  override def btBase = Textures.Button16.base
  override def btHover = Textures.Button16.hover

  override val outputColors = Map(
    0 -> Color(1F, 0F, 0F),
    1 -> Color(0F, 1F, 0F),
    2 -> Color(0F, 0F, 1F),
    3 -> Color(1F, 1F, 0F),
    4 -> Color(0F, 1F, 1F),
    5 -> Color(1F, 0F, 1F)
  )
  override val unlocalizedOutputName = (outputColors.keys map (n => n -> "deepcore.output.%d".format(n))).toMap

  override def getModuleName(s: String) = "deepcore.module." + s + ".name"
}
