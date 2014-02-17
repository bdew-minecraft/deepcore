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
import net.minecraft.inventory.Slot
import net.bdew.lib.items.inventory.{ContainerItemInventory, InventoryItemAdapter}

class ContainerScanner(inv: InventoryItemAdapter, player: EntityPlayer) extends ContainerItemInventory(inv, player) {
  for (i <- 0 until 2; j <- 0 until 9)
    addSlotToContainer(new Slot(inv, j + i * 9, 8 + j * 18, 8 + i * 18))

  bindPlayerInventory(player.inventory, 8, 106, 164)
}
