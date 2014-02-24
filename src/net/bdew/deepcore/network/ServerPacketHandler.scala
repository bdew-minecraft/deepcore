/*
 * Copyright (c) bdew, 2013 - 2014
 * https://github.com/bdew/deepcore
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * https://raw.github.com/bdew/deepcore/master/MMPL-1.0.txt
 */

package net.bdew.deepcore.network

import cpw.mods.fml.common.network.IPacketHandler
import cpw.mods.fml.common.network.Player
import net.minecraft.entity.player.EntityPlayerMP
import net.minecraft.network.INetworkManager
import net.minecraft.network.packet.Packet250CustomPayload
import java.io.ByteArrayInputStream
import java.io.DataInputStream
import net.bdew.deepcore.Deepcore
import net.bdew.deepcore.multiblock.interact.ContainerOutputFaces
import net.minecraft.nbt.{NBTTagCompound, CompressedStreamTools}
import net.bdew.deepcore.config.Items

class ServerPacketHandler extends IPacketHandler {
  def onPacketData(manager: INetworkManager, packet: Packet250CustomPayload, player: Player) {
    val din: DataInputStream = new DataInputStream(new ByteArrayInputStream(packet.data))
    val playermp: EntityPlayerMP = player.asInstanceOf[EntityPlayerMP]
    try {
      val op: Int = din.readInt
      Packets(op) match {
        case Packets.OUTPUT_CONFIG =>
          val output = din.readByte()
          val data = CompressedStreamTools.read(din)
          doOutputConfig(output, data, playermp)
        case Packets.SCANNER_SWITCH =>
          val dir = din.readByte()
          doScannerSwitch(dir, playermp)
        case _ =>
          Deepcore.logWarn("Unknown command from user '%s': %d", playermp.username, op.asInstanceOf[Integer])
      }
    } catch {
      case e: Throwable =>
        Deepcore.logWarn("Error handling packet from user '%s'", playermp.username)
        e.printStackTrace()
    }
  }

  private def doOutputConfig(output: Int, data: NBTTagCompound, player: EntityPlayerMP) {
    if (player.openContainer != null && player.openContainer.isInstanceOf[ContainerOutputFaces]) {
      val te = player.openContainer.asInstanceOf[ContainerOutputFaces].te
      te.outputConfig(output).handleConfigPacket(data)
      te.outputConfig.updated()
    }
  }

  private def doScannerSwitch(dir: Int, player: EntityPlayerMP) {
    val stack = player.inventory.getCurrentItem
    if (stack == null || stack.getItem == null || stack.getItem != Items.scanner) {
      Deepcore.logWarn("Player %s sent scanner switch with no scanner active", player.username)
    }
    Items.scanner.switchModule(stack, dir, player)
  }
}