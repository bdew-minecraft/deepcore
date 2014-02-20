/*
 * Copyright (c) bdew, 2013 - 2014
 * https://github.com/bdew/deepcore
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * https://raw.github.com/bdew/deepcore/master/MMPL-1.0.txt
 */

package net.bdew.deepcore.items.scanner

import net.bdew.lib.items.inventory.InventoryItemAdapter
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.bdew.deepcore.config.Items

class InventoryScanner(player: EntityPlayer, slot: Int, size: Int, tagName: String) extends InventoryItemAdapter(player, slot, size, tagName) {
  val moduleSlots = 0 until 18
  val upgradeSlots = 18 until 22

  override def getInventoryStackLimit = 1

  override def isItemValidForSlot(slot: Int, stack: ItemStack) =
    if (moduleSlots.contains(slot))
      stack.getItem == Items.scannerModule
    else
      false
}
