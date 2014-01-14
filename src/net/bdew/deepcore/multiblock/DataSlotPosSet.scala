/*
 * Copyright (c) bdew, 2014
 * https://github.com/bdew/deepcore
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * https://raw.github.com/bdew/deepcore/master/MMPL-1.0.txt
 */

package net.bdew.deepcore.multiblock

import net.bdew.lib.data.base.{UpdateKind, DataSlot, TileDataSlots}
import net.minecraft.nbt.{NBTTagIntArray, NBTTagList, NBTTagCompound}
import net.bdew.lib.Misc

case class DataSlotPosSet(name: String, parent: TileDataSlots) extends DataSlot {
  val set = collection.mutable.Set.empty[BlockPos]

  setUpdate(UpdateKind.SAVE, UpdateKind.WORLD)

  def save(t: NBTTagCompound, kind: UpdateKind.Value) {
    val lst = new NBTTagList()
    for (x <- set) lst.appendTag(new NBTTagIntArray("", x.asArray))
    t.setTag(name, lst)
  }

  def load(t: NBTTagCompound, kind: UpdateKind.Value) {
    set.clear()
    for (x <- Misc.iterNbtList[NBTTagIntArray](t.getTagList(name)))
      set.add(new BlockPos(x.intArray))
  }
}

object DataSlotPosSet {
  implicit def ds2set(v: DataSlotPosSet) = v.set
}
