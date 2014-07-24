/*
 * Copyright (c) bdew, 2013 - 2014
 * https://github.com/bdew/deepcore
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * https://raw.github.com/bdew/deepcore/master/MMPL-1.0.txt
 */

package net.bdew.deepcore.overlay

import cpw.mods.fml.common.eventhandler.SubscribeEvent
import net.minecraft.client.Minecraft
import net.minecraftforge.client.event.RenderGameOverlayEvent
import net.minecraftforge.common.MinecraftForge
import org.lwjgl.opengl.GL11

object OverlayRenderHandler {
  var cont: OverlayWidgetContainer = null
  var lastW, lastH = 0

  @SubscribeEvent
  def postRenderGameOverlay(ev: RenderGameOverlayEvent.Post) {
    if (ev.`type` != RenderGameOverlayEvent.ElementType.ALL) return
    if (Minecraft.getMinecraft.currentScreen != null) return
    val player = Minecraft.getMinecraft.thePlayer
    if (player == null) return
    val stack = player.inventory.getCurrentItem
    if (stack == null || stack.getItem == null || !stack.getItem.isInstanceOf[ItemWithOverlay]) return
    GL11.glPushAttrib(GL11.GL_ENABLE_BIT)
    GL11.glDisable(GL11.GL_LIGHTING)
    GL11.glDisable(GL11.GL_DEPTH_TEST)
    //RenderHelper.disableStandardItemLighting()

    if (ev.resolution.getScaledHeight != lastH || ev.resolution.getScaledWidth != lastW)
      cont = new OverlayWidgetContainer(ev.resolution)

    cont.drawWidget(stack.getItem.asInstanceOf[ItemWithOverlay].getOverlay(stack))

    //RenderHelper.enableGUIStandardItemLighting()
    GL11.glPopAttrib()

  }

  def init() {
    MinecraftForge.EVENT_BUS.register(this)
  }
}
