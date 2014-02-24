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
import net.minecraft.util.Icon
import net.bdew.lib.gui.Rect
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.ScaledResolution
import net.minecraft.client.renderer.Tessellator
import net.bdew.lib.gui.widgets.BaseWidget

class OverlayWidgetContainer(res: ScaledResolution) extends WidgetContainer {
  val mc = Minecraft.getMinecraft
  val rect = Rect(0, 0, res.getScaledWidth, res.getScaledHeight)
  def getFontRenderer = mc.fontRenderer
  def getOffsetFromWindow = Point(0, 0)
  val T = Tessellator.instance

  def drawWidget(w: BaseWidget) {
    w.parent = this
    w.draw(Point(0, 0))
  }

  final val F = 1 / 256F

  def doDrawTexture(x1: Float, y1: Float, x2: Float, y2: Float, u1: Float, v1: Float, u2: Float, v2: Float, color: Color) {
    color.activate()
    T.startDrawingQuads()
    T.addVertexWithUV(x1, y2, 0, u1, v2)
    T.addVertexWithUV(x2, y2, 0, u2, v2)
    T.addVertexWithUV(x2, y1, 0, u2, v1)
    T.addVertexWithUV(x1, y1, 0, u1, v1)
    T.draw()
  }

  def drawTextureScaled(r: Rect, l: TextureLocationScaled, color: Color = Color.white) {
    mc.renderEngine.bindTexture(l.resource)
    doDrawTexture(r.x1, r.y1, r.x2, r.y2, l.r.x1 * F, l.r.y1 * F, l.r.x2 * F, l.r.y2 * F, color)
  }

  def drawTexture(r: Rect, uv: Point, color: Color = Color.white) {
    doDrawTexture(r.x1, r.y1, r.x2, r.y2, uv.x * F, uv.y * F, (uv.x + r.w) * F, (uv.y + r.h) * F, color)
  }

  def drawIcon(r: Rect, i: Icon, color: Color) {
    doDrawTexture(r.x1, r.y1, r.x2, r.y2, i.getMinU, i.getMinV, i.getMaxU, i.getMaxV, color)
  }
}
