/*
 * Copyright (c) bdew, 2013 - 2014
 * https://github.com/bdew/deepcore
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * https://raw.github.com/bdew/deepcore/master/MMPL-1.0.txt
 */

package net.bdew.deepcore.items.scanner

import net.bdew.lib.gui.{Color, Texture, BaseScreen}
import net.minecraft.entity.player.EntityPlayer
import net.bdew.deepcore.Deepcore
import net.bdew.lib.items.inventory.InventoryItemAdapter
import net.bdew.lib.gui.widgets.WidgetLabel
import net.bdew.lib.Misc
import net.bdew.deepcore.resources.IconLoader

class GuiScanner(inv: InventoryItemAdapter, player: EntityPlayer) extends BaseScreen(new ContainerScanner(inv, player), 176, 166) {
  val background = Texture(Deepcore.modId, "textures/gui/scannerinv.png", rect)
  override def initGui() {
    super.initGui()
    for (i <- 0 until 18) inventorySlots.getSlot(i).setBackgroundIcon(IconLoader.scannerModuleHint)
    widgets.add(new WidgetLabel(Misc.toLocal("item.deepcore.Scanner.name"), 8, 6, Color.darkgray))
    widgets.add(new WidgetLabel(Misc.toLocal("container.inventory"), 8, 72, Color.darkgray))
  }
}
