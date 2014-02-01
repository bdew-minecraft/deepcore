/*
 * Copyright (c) bdew, 2013 - 2014
 * https://github.com/bdew/deepcore
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * https://raw.github.com/bdew/deepcore/master/MMPL-1.0.txt
 */

package net.bdew.deepcore.multiblock.gui

import net.bdew.lib.gui.{Rect, TextureLocationScaled, Point}
import net.bdew.lib.gui.widgets.Widget
import scala.collection.mutable

class WidgetInfo(val rect: Rect, icon: TextureLocationScaled, text: => String, tooltip: => String) extends Widget {
  final val iconRect = Rect(1, 1, 8, 8)

  override def handleTooltip(p: Point, tip: mutable.MutableList[String]) {
    if (iconRect.contains(p) && tooltip != null) tip += tooltip
  }

  override def draw(mouse: Point) {
    parent.drawTextureScaled(iconRect + rect.origin, icon)
    parent.getFontRenderer.drawString(text, rect.x + 12, rect.y + 1, 0x404040)
  }
}
