/*
 * Copyright (c) bdew, 2013 - 2014
 * https://github.com/bdew/deepcore
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * https://raw.github.com/bdew/deepcore/master/MMPL-1.0.txt
 */

package net.bdew.deepcore.items.scanner.overlay

import cpw.mods.fml.common.eventhandler.SubscribeEvent
import net.bdew.deepcore.items.scanner.Scanner
import net.bdew.deepcore.network.{MsgScannerSwitch, NetHandler}
import net.minecraft.client.Minecraft
import net.minecraftforge.client.event.MouseEvent
import net.minecraftforge.common.MinecraftForge

object ScannerMouseEventHandler {
  def init() {
    MinecraftForge.EVENT_BUS.register(this)
  }

  @SubscribeEvent
  def handleMouseEvent(ev: MouseEvent) {
    if (ev.dwheel == 0) return
    if (Minecraft.getMinecraft.currentScreen != null) return
    val player = Minecraft.getMinecraft.thePlayer
    if (player == null || !player.isSneaking) return
    val stack = player.inventory.getCurrentItem
    if (stack == null || stack.getItem == null || stack.getItem != Scanner) return
    ev.setCanceled(true)
    NetHandler.sendToServer(MsgScannerSwitch(ev.dwheel.signum))
    ScannerOverlay.resId = -2
  }
}
