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
import net.minecraft.network.INetworkManager
import net.minecraft.network.packet.Packet250CustomPayload
import java.io.ByteArrayInputStream
import java.io.DataInputStream
import net.bdew.deepcore.Deepcore
import net.bdew.deepcore.items.scanner.ScannerOverlay

class ClientPacketHandler extends IPacketHandler {
  def onPacketData(manager: INetworkManager, packet: Packet250CustomPayload, player: Player) {
    val din = new DataInputStream(new ByteArrayInputStream(packet.data))
    try {
      val op = din.readInt
      Packets(op) match {
        case Packets.SCANNER_UPDATE => ScannerOverlay.readUpdatePacket(din)
        case _ =>
          Deepcore.logWarn("Unknown command from server: %d", op.asInstanceOf[Integer])
      }
    } catch {
      case e: Throwable =>
        Deepcore.logWarn("Error handling packet from server")
        e.printStackTrace()
    }
  }
}