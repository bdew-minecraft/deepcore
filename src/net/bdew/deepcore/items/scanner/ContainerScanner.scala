/*
 * Copyright (c) bdew, 2013 - 2014
 * https://github.com/bdew/deepcore
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * https://raw.github.com/bdew/deepcore/master/MMPL-1.0.txt
 */

package net.bdew.deepcore.items.scanner

import net.minecraft.entity.player.EntityPlayer
import net.bdew.lib.items.inventory.{ContainerItemInventory, InventoryItemAdapter}
import net.bdew.lib.gui.SlotValidating

class ContainerScanner(inv: InventoryItemAdapter, player: EntityPlayer) extends ContainerItemInventory(inv, player) {
  for (i <- 0 until 3; j <- 0 until 6)
    addSlotToContainer(new SlotValidating(inv, j + i * 6, 8 + j * 18, 17 + i * 18))

  bindPlayerInventory(player.inventory, 8, 84, 142)
}
