/*
 * Copyright (c) bdew, 2014
 * https://github.com/bdew/deepcore
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * https://raw.github.com/bdew/deepcore/master/MMPL-1.0.txt
 */

package net.bdew.deepcore.multiblock.data

import net.minecraft.nbt.NBTTagCompound

object RSMode extends Enumeration {
  val ALWAYS = Value(0, "ALWAYS")
  val RS_ON = Value(1, "RS_ON")
  val RS_OFF = Value(2, "RS_OFF")
  val NEVER = Value(3, "NEVER")
}

class OutputConfigPower extends OutputConfig {
  var avg = 0F
  var rsMode = RSMode.ALWAYS
  var unit = "MJ"

  final val decay = 0.2F

  def updateAvg(v: Float) {
    avg = avg * decay + (1 - decay) * v
  }

  def read(t: NBTTagCompound) {
    avg = t.getFloat("avg")
    rsMode = RSMode(t.getInteger("rsMode"))
    unit = t.getString("unit")
  }

  def write(t: NBTTagCompound) {
    t.setFloat("avg", avg)
    t.setInteger("rsMode", rsMode.id)
    t.setString("unit", unit)
  }
}
