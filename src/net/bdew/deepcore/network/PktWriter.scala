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

class PktWriter(pkt: Packets.Value) extends DataOutputStream(new ByteArrayOutputStream) {
  writeInt(pkt.id)
  def toPacket: Packet250CustomPayload = {
    return new Packet250CustomPayload(Deepcore.channel, out.asInstanceOf[ByteArrayOutputStream].toByteArray)
  }
}