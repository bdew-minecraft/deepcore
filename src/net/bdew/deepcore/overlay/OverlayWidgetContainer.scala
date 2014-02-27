/*
 * Copyright (c) bdew, 2013 - 2014
 * https://github.com/bdew/deepcore
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * https://raw.github.com/bdew/deepcore/master/MMPL-1.0.txt
 */

package net.bdew.deepcore.overlay

import net.bdew.lib.gui._
import net.bdew.lib.gui.Rect
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.ScaledResolution
import net.bdew.lib.gui.widgets.BaseWidget

class OverlayWidgetContainer(res: ScaledResolution) extends WidgetContainer with SimpleDrawTarget {
  val mc = Minecraft.getMinecraft
  val rect = Rect(0, 0, res.getScaledWidth, res.getScaledHeight)
  def getFontRenderer = mc.fontRenderer
  def getOffsetFromWindow = Point(0, 0)
  def getZLevel = 0

  def drawWidget(w: BaseWidget) {
    w.parent = this
    w.drawBackground(Point(0, 0))
    w.draw(Point(0, 0))
  }
}
