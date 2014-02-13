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

  def drawWidget(w: BaseWidget) {
    w.parent = this
    w.draw(Point(0, 0))
  }

  final val F = 1 / 256F

  def doDrawTexture(x: Float, y: Float, w: Float, h: Float, minU: Float, maxU: Float, minV: Float, maxV: Float) {
    val t = Tessellator.instance
    t.startDrawingQuads()
    t.addVertexWithUV(x, y + h, 0, minU, maxV)
    t.addVertexWithUV(x + w, y + h, 0, maxU, maxV)
    t.addVertexWithUV(x + w, y, 0, maxU, minV)
    t.addVertexWithUV(x, y, 0, minU, minV)
    t.draw()
  }

  def drawTextureScaled(r: Rect, l: TextureLocationScaled, color: Color = Color.white) {
    mc.renderEngine.bindTexture(l.resource)
    color.activate()
    doDrawTexture(r.x, r.y, r.w, r.h, l.r.x * F, (l.r.x + l.r.w) * F, l.r.y * F, (l.r.y + l.r.h) * F)
  }

  def drawTexture(r: Rect, uv: Point, color: Color = Color.white) {
    color.activate()
    doDrawTexture(r.x, r.y, r.w, r.h, uv.x * F, (uv.x + r.w) * F, uv.y * F, (uv.y + r.h) * F)
  }

  def drawIcon(r: Rect, i: Icon, color: Color) {
    color.activate()
    doDrawTexture(r.x, r.y, r.w, r.h, i.getMinU, i.getMaxU, i.getMinV, i.getMaxV)
  }
}
