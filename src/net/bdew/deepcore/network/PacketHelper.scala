/*
 * Copyright (c) bdew, 2013 - 2014
 * https://github.com/bdew/deepcore
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * https://raw.github.com/bdew/deepcore/master/MMPL-1.0.txt
 */

package net.bdew.deepcore.network

import net.minecraft.nbt.{CompressedStreamTools, NBTTagCompound}

object PacketHelper {
  def sendOutputConfig(output: Int, data: NBTTagCompound) = {
    val out = new PktWriter(Packets.OUTPUT_CONFIG)
    out.writeByte(output)
    CompressedStreamTools.write(data, out)
    out.sendToServer()
  }

  def sendScannerSwitch(dir: Int) {
    val out = new PktWriter(Packets.SCANNER_SWITCH)
    out.writeByte(dir.toByte)
    out.sendToServer()
  }
}