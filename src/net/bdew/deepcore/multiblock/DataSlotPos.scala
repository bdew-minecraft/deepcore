/*
 * Copyright (c) bdew, 2014
 * https://github.com/bdew/deepcore
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * https://raw.github.com/bdew/deepcore/master/MMPL-1.0.txt
 */

package net.bdew.deepcore.multiblock

import net.bdew.lib.data.base.{UpdateKind, DataSlotVal, TileDataSlots}
import net.minecraft.nbt.NBTTagCompound

case class DataSlotPos(name: String, parent: TileDataSlots) extends DataSlotVal[BlockPos] {
  var cval: BlockPos = null

  setUpdate(UpdateKind.SAVE, UpdateKind.WORLD)

  def save(t: NBTTagCompound, kind: UpdateKind.Value) {
    if (cval!=null)
      t.setIntArray(name, cval.asArray)
  }

  def load(t: NBTTagCompound, kind: UpdateKind.Value) {
    if (t.hasKey(name))
      cval = new BlockPos(t.getIntArray(name))
    else
      cval = null
  }
}
