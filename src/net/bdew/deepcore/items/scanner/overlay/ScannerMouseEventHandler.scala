/*
 * Copyright (c) bdew, 2013 - 2014
 * https://github.com/bdew/deepcore
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * https://raw.github.com/bdew/deepcore/master/MMPL-1.0.txt
 */

package net.bdew.deepcore.items.scanner.overlay

import net.minecraftforge.client.event.MouseEvent
import net.minecraftforge.event.ForgeSubscribe
import net.minecraftforge.common.MinecraftForge
import net.minecraft.client.Minecraft
import net.bdew.deepcore.config.Items
import net.bdew.deepcore.network.PacketHelper

object ScannerMouseEventHandler {
  def init() {
    MinecraftForge.EVENT_BUS.register(this)
  }

  @ForgeSubscribe
  def handleMouseEvent(ev: MouseEvent) {
    if (ev.dwheel == 0) return
    if (Minecraft.getMinecraft.currentScreen != null) return
    val player = Minecraft.getMinecraft.thePlayer
    if (player == null || !player.isSneaking) return
    val stack = player.inventory.getCurrentItem
    if (stack == null || stack.getItem == null || stack.getItem != Items.scanner) return
    ev.setCanceled(true)
    PacketHelper.sendScannerSwitch(ev.dwheel.signum)
    ScannerOverlay.resId = -2
  }
}
