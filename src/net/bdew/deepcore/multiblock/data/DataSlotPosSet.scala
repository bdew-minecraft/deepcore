/*
 * Copyright (c) bdew, 2014
 * https://github.com/bdew/deepcore
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * https://raw.github.com/bdew/deepcore/master/MMPL-1.0.txt
 */

package net.bdew.deepcore.multiblock.data

import net.bdew.lib.Misc
import net.bdew.lib.block.BlockRef
import net.bdew.lib.data.base.{DataSlot, TileDataSlots, UpdateKind}
import net.minecraft.nbt.{NBTTagCompound, NBTTagList}

case class DataSlotPosSet(name: String, parent: TileDataSlots) extends DataSlot {
  val set = collection.mutable.Set.empty[BlockRef]

  setUpdate(UpdateKind.SAVE, UpdateKind.WORLD)

  def save(t: NBTTagCompound, kind: UpdateKind.Value) {
    val lst = new NBTTagList()
    for (x <- set) lst.appendTag(Misc.applyMutator(x.writeToNBT, new NBTTagCompound))
    t.setTag(name, lst)
  }

  def load(t: NBTTagCompound, kind: UpdateKind.Value) {
    set.clear()
    set ++= Misc.iterNbtCompoundList(t, name) map BlockRef.fromNBT
  }

  def updated() = parent.dataSlotChanged(this)

}

object DataSlotPosSet {

  import scala.language.implicitConversions

  implicit def dsps2set(v: DataSlotPosSet) = v.set
}
