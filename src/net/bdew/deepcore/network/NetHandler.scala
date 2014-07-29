/*
 * Copyright (c) bdew, 2013 - 2014
 * https://github.com/bdew/deepcore
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * https://raw.github.com/bdew/deepcore/master/MMPL-1.0.txt
 */

package net.bdew.deepcore.network

import net.bdew.deepcore.Deepcore
import net.bdew.deepcore.items.scanner.Scanner
import net.bdew.deepcore.multiblock.interact.ContainerOutputFaces
import net.bdew.lib.network.NetChannel
import net.minecraft.entity.player.EntityPlayerMP

object NetHandler extends NetChannel(Deepcore.modId) {
  regServerHandler {

    case (msg: MsgOutputCfg, p: EntityPlayerMP) =>
      val te = p.openContainer.asInstanceOf[ContainerOutputFaces].te
      te.outputConfig(msg.output).handleConfigPacket(msg)
      te.outputConfig.updated()

    case (msg: MsgScannerSwitch, p: EntityPlayerMP) =>
      val stack = p.inventory.getCurrentItem
      if (stack == null || stack.getItem == null || stack.getItem != Scanner) {
        Deepcore.logWarn("Player %s sent scanner switch with no scanner active", p.getDisplayName)
      } else {
        Scanner.switchModule(stack, msg.dir, p)
      }

  }
}