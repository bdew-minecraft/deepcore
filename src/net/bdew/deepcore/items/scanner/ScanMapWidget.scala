/*
 * Copyright (c) bdew, 2013 - 2014
 * https://github.com/bdew/deepcore
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * https://raw.github.com/bdew/deepcore/master/MMPL-1.0.txt
 */

package net.bdew.deepcore.items.scanner

import net.bdew.lib.gui.{Point, Color, Rect}
import net.bdew.lib.gui.widgets.Widget
import net.minecraft.client.renderer.Tessellator
import net.minecraft.client.Minecraft
import net.bdew.lib.Misc
import org.lwjgl.opengl.GL11
import net.bdew.deepcore.gui.Textures

class ScanMapWidget(r: Rect, rad: Int, size: Float, border: Float, scanvals: (Int, Int) => Float, col1: Color, col2: Color) extends Widget {
  val rect = r

  val T = Tessellator.instance
  val mc = Minecraft.getMinecraft

  def colorInterpolate(col1: Color, col2: Color, v: Float) =
    Color(col2.r * v + (col1.r * (1 - v)), col2.g * v + (col1.g * (1 - v)), col2.b * v + (col1.b * (1 - v)))

  def quadClamped(x1: Float, y1: Float, x2: Float, y2: Float, color: Color) {
    val cx1 = Misc.clamp[Float](x1, rect.x, rect.x + rect.w)
    val cx2 = Misc.clamp[Float](x2, rect.x, rect.x + rect.w)
    val cy1 = Misc.clamp[Float](y1, rect.y, rect.y + rect.h)
    val cy2 = Misc.clamp[Float](y2, rect.y, rect.y + rect.h)
    T.setColorRGBA_F(color.r, color.g, color.b, 1)
    T.addVertex(cx2, cy1, 0)
    T.addVertex(cx1, cy1, 0)
    T.addVertex(cx1, cy2, 0)
    T.addVertex(cx2, cy2, 0)
  }

  override def draw(mouse: Point) {
    val chunkX = mc.thePlayer.posX.toInt >> 4
    val chunkY = mc.thePlayer.posZ.toInt >> 4
    val offsX = ((mc.thePlayer.posX - chunkX * 16) / 16 * size).toFloat
    val offsY = ((mc.thePlayer.posZ - chunkY * 16) / 16 * size).toFloat

    GL11.glDisable(GL11.GL_TEXTURE_2D)
    T.startDrawingQuads()
    quadClamped(r.x, r.y, r.x + r.w, r.y + r.h, Color(0, 0, 0))
    for (x <- -rad to rad; y <- -rad to rad) {
      val v = scanvals(chunkX + x, chunkY + y)
      val x1 = rect.x + rect.w / 2 + x * size - offsX + border
      val y1 = rect.y + rect.h / 2 + y * size - offsY + border
      val x2 = x1 + size - border * 2
      val y2 = y1 + size - border * 2
      quadClamped(x1, y1, x2, y2, colorInterpolate(col1, col2, v))
    }
    T.draw()

    GL11.glPushMatrix()
    GL11.glTranslatef(rect.x + rect.w / 2, rect.y + rect.h / 2, 0)
    GL11.glScalef(size, size, 1)
    GL11.glRotatef(Minecraft.getMinecraft.thePlayer.getRotationYawHead + 180, 0, 0, 1)
    GL11.glEnable(GL11.GL_TEXTURE_2D)
    parent.drawTextureScaled(Rect(-1, -1, 2, 2), Textures.arrow)
    GL11.glPopMatrix()
  }
}
