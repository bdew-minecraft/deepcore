/*
 * Copyright (c) bdew, 2013 - 2014
 * https://github.com/bdew/deepcore
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * https://raw.github.com/bdew/deepcore/master/MMPL-1.0.txt
 */

package net.bdew.deepcore.overlay

import cpw.mods.fml.common.{TickType, ITickHandler}
import java.util
import net.minecraft.client.Minecraft
import org.lwjgl.opengl.GL11
import cpw.mods.fml.common.registry.TickRegistry
import cpw.mods.fml.relauncher.Side

object OverlayTickHandler extends ITickHandler {
  def init() {
    TickRegistry.registerTickHandler(this, Side.CLIENT)
  }

  def tickStart(kind: util.EnumSet[TickType], tickData: AnyRef*) {
  }

  def tickEnd(kind: util.EnumSet[TickType], tickData: AnyRef*) {
    if (kind.contains(TickType.RENDER))
      onRenderEnd(tickData(0).asInstanceOf[Float])
  }

  def onRenderEnd(f: Float) {
    if (Minecraft.getMinecraft.currentScreen != null) return
    val player = Minecraft.getMinecraft.thePlayer
    if (player == null) return
    val stack = player.inventory.getCurrentItem
    if (stack == null || stack.getItem == null) return
    if (stack.getItem.isInstanceOf[ItemWithOverlay]) {
      GL11.glPushAttrib(GL11.GL_ENABLE_BIT)
      GL11.glDisable(GL11.GL_LIGHTING)
      GL11.glDisable(GL11.GL_DEPTH_TEST)
      //RenderHelper.disableStandardItemLighting()

      stack.getItem.asInstanceOf[ItemWithOverlay].getOverlay(stack).draw(0, 0)

      GL11.glPopAttrib()
      //RenderHelper.enableGUIStandardItemLighting()
    }
  }

  def ticks() = util.EnumSet.of(TickType.RENDER)
  def getLabel = "BDew Item Overlay Handler"
}
