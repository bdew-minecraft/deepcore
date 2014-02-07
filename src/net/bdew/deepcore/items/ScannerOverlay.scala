/*
 * Copyright (c) bdew, 2013 - 2014
 * https://github.com/bdew/deepcore
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * https://raw.github.com/bdew/deepcore/master/MMPL-1.0.txt
 */

package net.bdew.deepcore.items

import net.bdew.deepcore.overlay.OverlayWidgetContainer
import net.minecraft.util.ResourceLocation
import net.bdew.deepcore.Deepcore
import net.bdew.lib.gui.{Color, Rect, TextureLocation, Point}
import scala.util.Random
import net.bdew.lib.gui.widgets.{WidgetSubcontainer, Widget}
import org.lwjgl.opengl.GL11
import net.minecraft.client.renderer.Tessellator
import net.bdew.deepcore.gui.Textures
import net.minecraft.client.Minecraft

class ScanMapWidget(p: Point, chunks: Int, size: Float, spacing: Float, scanvals: (Int, Int) => Float, col1: Color, col2: Color) extends Widget {
  val sz = (chunks * (size + spacing) + spacing).ceil.toInt
  val rect = new Rect(p, sz, sz)
  override def draw(mouse: Point) {
    val t = Tessellator.instance
    GL11.glDisable(GL11.GL_TEXTURE_2D)
    t.startDrawingQuads()
    for (x <- 0 until chunks; y <- 0 until chunks) {
      val v = scanvals(x, y)
      val x1 = rect.x + x * (size + spacing)
      val y1 = rect.y + y * (size + spacing)
      val x2 = x1 + size
      val y2 = y1 + size

      val r = col2.r * v + (col1.r * (1 - v))
      val g = col2.g * v + (col1.g * (1 - v))
      val b = col2.b * v + (col1.b * (1 - v))

      t.setColorRGBA_F(r, g, b, 1)
      t.addVertex(x2, y1, 0)
      t.addVertex(x1, y1, 0)
      t.addVertex(x1, y2, 0)
      t.addVertex(x2, y2, 0)
    }
    t.draw()

    val o = (chunks / 2F).floor * (size + spacing) + size / 2F

    GL11.glPushMatrix()
    GL11.glTranslatef(rect.x + o, rect.y + o, 0)
    GL11.glScalef(size / 2F, size / 2F, 1)
    GL11.glRotatef(Minecraft.getMinecraft.thePlayer.getRotationYawHead + 180, 0, 0, 1)
    GL11.glEnable(GL11.GL_TEXTURE_2D)
    parent.drawTextureScaled(Rect(-1, -1, 2, 2), Textures.arrow)
    GL11.glPopMatrix()
  }
}

class ScannerOverlay extends OverlayWidgetContainer {
  val texture = new ResourceLocation(Deepcore.modId + ":textures/gui/scanner.png")
  val background = new TextureLocation(texture, 0, 0)

  val width = 76
  val height = 106

  val sub = add(new WidgetSubcontainer(Rect(rect.w - width, rect.h - height - 40, width, height)))

  val rand = new Random()
  def getScanVal(x: Int, y: Int) = rand.nextFloat()

  sub.add(new ScanMapWidget(Point(7, 26), 11, 5, 1, getScanVal, Color(1, 0, 0), Color(0, 0, 1)))

  override def draw(mouse: Point) {
    drawTexture(sub.rect, background)
    super.draw(mouse)
  }
}
