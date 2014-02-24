/*
 * Copyright (c) bdew, 2013 - 2014
 * https://github.com/bdew/deepcore
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * https://raw.github.com/bdew/deepcore/master/MMPL-1.0.txt
 */

package net.bdew.deepcore.network

import net.minecraft.network.packet.Packet250CustomPayload
import java.io.ByteArrayOutputStream
import java.io.DataOutputStream
import net.bdew.deepcore.Deepcore
import cpw.mods.fml.common.network.{Player, PacketDispatcher}
import net.minecraft.entity.player.EntityPlayerMP

class PktWriter(pkt: Packets.Value) extends DataOutputStream(new ByteArrayOutputStream) {
  writeInt(pkt.id)

  def toPacket: Packet250CustomPayload =
    new Packet250CustomPayload(Deepcore.channel, out.asInstanceOf[ByteArrayOutputStream].toByteArray)

  def sendToPlayer(player: EntityPlayerMP) =
    PacketDispatcher.sendPacketToPlayer(toPacket, player.asInstanceOf[Player])

  def sendToServer() =
    PacketDispatcher.sendPacketToServer(toPacket)

  def sendPacketToAllAround(x: Double, y: Double, z: Double, range: Double, dimensionId: Int) =
    PacketDispatcher.sendPacketToAllAround(x, y, z, range, dimensionId, toPacket)

  def sendPacketToAllInDimension(dimId: Int) =
    PacketDispatcher.sendPacketToAllInDimension(toPacket, dimId)

  def sendPacketToAllPlayers() =
    PacketDispatcher.sendPacketToAllPlayers(toPacket)
}