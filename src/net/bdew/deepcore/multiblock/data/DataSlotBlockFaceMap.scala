/*
 * Copyright (c) bdew, 2014
 * https://github.com/bdew/deepcore
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * https://raw.github.com/bdew/deepcore/master/MMPL-1.0.txt
 */

package net.bdew.deepcore.multiblock.data

import net.bdew.lib.data.base.{UpdateKind, DataSlot, TileDataSlots}
import net.minecraft.nbt.{NBTTagIntArray, NBTTagList, NBTTagCompound}
import net.bdew.lib.Misc
import net.minecraftforge.common.util.ForgeDirection

case class DataSlotBlockFaceMap(name: String, parent: TileDataSlots) extends DataSlot {
  val map = collection.mutable.Map.empty[BlockFace, Int]

  setUpdate(UpdateKind.SAVE, UpdateKind.WORLD)

  def inverted = map.map(_.swap)

  def ent2arr(x: (BlockFace, Int)) = Array(x._2, x._1.x, x._1.y, x._1.z, x._1.face.ordinal())
  def arr2ent(x: Array[Int]) = BlockFace(x(1), x(2), x(3), ForgeDirection.values()(x(4))) -> x(0)

  def save(t: NBTTagCompound, kind: UpdateKind.Value) {
    val lst = new NBTTagList()
    for (x <- map) lst.appendTag(new NBTTagIntArray(ent2arr(x)))
    t.setTag(name, lst)
  }

  def load(t: NBTTagCompound, kind: UpdateKind.Value) {
    map.clear()
    map ++= Misc.iterNbtIntArray(t, name) map arr2ent
  }

  def updated() = parent.dataSlotChanged(this)
}

object DataSlotBlockFaceMap {

  import language.implicitConversions

  implicit def dsbfm2map(v: DataSlotBlockFaceMap) = v.map
}
