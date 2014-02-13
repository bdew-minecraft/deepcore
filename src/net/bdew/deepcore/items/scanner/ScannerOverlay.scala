/*
 * Copyright (c) bdew, 2013 - 2014
 * https://github.com/bdew/deepcore
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * https://raw.github.com/bdew/deepcore/master/MMPL-1.0.txt
 */

package net.bdew.deepcore.items.scanner

import net.minecraft.util.ResourceLocation
import net.bdew.deepcore.Deepcore
import net.bdew.lib.gui._
import net.bdew.lib.gui.widgets.WidgetSubcontainer
import org.lwjgl.opengl.GL11
import net.bdew.deepcore.noise.NoiseGen
import net.bdew.lib.gui.Rect

object ScannerOverlay extends WidgetSubcontainer(Rect(0, 0, 76, 106)) {
  val texture = new ResourceLocation(Deepcore.modId + ":textures/gui/scanner.png")
  val background = new TextureLocation(texture, 0, 0)

  def getScanVal(x: Int, y: Int) = NoiseGen.generate(x, y)

  add(new ScanMapWidget(Rect(6, 25, 67, 67), 6, 6, 0.5F, getScanVal, Color(0.4F, 0.4F, 0.4F), Color(1, 0, 0)))
  add(new IndicatorBarWidget(Rect(5, 94, 69, 8), Color(0.4F, 0.4F, 0.4F), Color(1, 0, 0), getScanVal))

  override def draw(mouse: Point) {
    GL11.glTranslatef(parent.rect.w - rect.w, parent.rect.h - rect.h - 40, 0)
    drawTexture(rect, background)
    super.draw(mouse)
    GL11.glTranslatef(-parent.rect.w + rect.w, -parent.rect.h + rect.h + 40, 0)
  }
}
