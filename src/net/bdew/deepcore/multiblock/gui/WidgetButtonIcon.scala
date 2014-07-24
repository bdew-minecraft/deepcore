/*
 * Copyright (c) bdew, 2013 - 2014
 * https://github.com/bdew/deepcore
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * https://raw.github.com/bdew/deepcore/master/MMPL-1.0.txt
 */

package net.bdew.deepcore.multiblock.gui

import net.bdew.lib.Client
import net.bdew.lib.gui.widgets.Widget
import net.bdew.lib.gui.{Texture, Rect, Point}
import net.bdew.deepcore.gui.Textures
import net.minecraft.client.Minecraft
import net.minecraft.client.audio.PositionedSoundRecord
import net.minecraft.util.ResourceLocation
import scala.collection.mutable

class WidgetButtonIcon(p: Point, clicked: WidgetButtonIcon => Unit) extends Widget {
  val rect = new Rect(p, 16, 16)
  val iconRect = new Rect(p +(1, 1), 14, 14)

  var icon: Texture = null
  var hover: String = null

  override def draw(mouse: Point) {
    if (rect.contains(mouse))
      parent.drawTexture(rect, Textures.Button16.hover)
    else
      parent.drawTexture(rect, Textures.Button16.base)

    if (icon != null)
      parent.drawTexture(iconRect, icon)
  }

  override def handleTooltip(p: Point, tip: mutable.MutableList[String]) {
    if (hover != null && hover > "")
      tip += hover
  }

  override def mouseClicked(p: Point, button: Int) {
    Minecraft.getMinecraft.getSoundHandler.playSound(PositionedSoundRecord.func_147674_a(new ResourceLocation("gui.button.press"), 1.0F))
    clicked(this)
  }
}
