/*
 * Copyright (c) bdew, 2013 - 2014
 * https://github.com/bdew/deepcore
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * https://raw.github.com/bdew/deepcore/master/MMPL-1.0.txt
 */

package net.bdew.deepcore.items.scanner.overlay

import net.bdew.lib.gui.{Point, Color, Rect}
import net.bdew.lib.gui.widgets.Widget
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.Tessellator
import org.lwjgl.opengl.GL11
import net.bdew.lib.Misc
import net.bdew.deepcore.gui.Textures
import net.bdew.deepcore.resources.ResourceManager

class WidgetIndicatorBar(r: Rect, col1: Color, col2: Color, scanvals: (Int, Int) => Float) extends Widget {
  val rect = r
  val mc = Minecraft.getMinecraft
  val T = Tessellator.instance

  override def draw(mouse: Point) {
    GL11.glDisable(GL11.GL_TEXTURE_2D)
    GL11.glShadeModel(GL11.GL_SMOOTH)
    T.startDrawingQuads()
    T.setColorRGBA_F(col2.r, col2.g, col2.b, 1)
    T.addVertex(r.x + r.w - 1, r.y + 2, 0)
    T.setColorRGBA_F(col1.r, col1.g, col1.b, 1)
    T.addVertex(r.x + 1, r.y + 2, 0)
    T.addVertex(r.x + 1, r.y + r.h - 2, 0)
    T.setColorRGBA_F(col2.r, col2.g, col2.b, 1)
    T.addVertex(r.x + r.w - 1, r.y + r.h - 2, 0)
    T.draw()
    GL11.glShadeModel(GL11.GL_FLAT)
    GL11.glEnable(GL11.GL_TEXTURE_2D)

    val chunkX = mc.thePlayer.posX.toInt >> 4
    val chunkY = mc.thePlayer.posZ.toInt >> 4

    if (ResourceManager.isValid(ScannerOverlay.resId)) {
      val x = (Misc.clamp(scanvals(chunkX, chunkY), 0F, 1F) * rect.w).round
      parent.drawTexture(Rect(rect.x + x - 1, rect.y, 3, 8), Textures.indicator)
    }
  }
}
