/*
 * Copyright (c) bdew, 2014
 * https://github.com/bdew/deepcore
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * https://raw.github.com/bdew/deepcore/master/MMPL-1.0.txt
 */

package net.bdew.deepcore.blocks.turbineController

import net.minecraft.entity.player.EntityPlayer
import net.bdew.lib.gui.NoInvContainer
import net.bdew.lib.data.base.ContainerDataSlots

class ContainerTurbine(val te: TileTurbineController, player: EntityPlayer) extends NoInvContainer with ContainerDataSlots {
  lazy val dataSource = te
  bindPlayerInventory(player.inventory, 8, 84, 142)

  def canInteractWith(entityplayer: EntityPlayer) = true
}
