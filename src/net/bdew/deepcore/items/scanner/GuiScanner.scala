/*
 * Copyright (c) bdew, 2013 - 2014
 * https://github.com/bdew/deepcore
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * https://raw.github.com/bdew/deepcore/master/MMPL-1.0.txt
 */

package net.bdew.deepcore.items.scanner

import net.bdew.lib.gui.BaseScreen
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.util.ResourceLocation
import net.bdew.deepcore.Deepcore
import net.bdew.lib.items.inventory.InventoryItemAdapter

class GuiScanner(inv: InventoryItemAdapter, player: EntityPlayer) extends BaseScreen(new ContainerScanner(inv, player), 176, 188) {
  val texture = new ResourceLocation(Deepcore.modId + ":textures/gui/scanner_inv.png")
}
