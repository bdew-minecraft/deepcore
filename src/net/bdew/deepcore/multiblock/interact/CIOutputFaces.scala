/*
 * Copyright (c) bdew, 2014
 * https://github.com/bdew/deepcore
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * https://raw.github.com/bdew/deepcore/master/MMPL-1.0.txt
 */

package net.bdew.deepcore.multiblock.interact

import net.bdew.deepcore.multiblock.tile.TileCore
import net.bdew.deepcore.multiblock.data.{BlockFace, BlockPos, DataSlotBlockFaceMap}
import net.minecraftforge.common.ForgeDirection
import net.bdew.lib.Misc

trait CIOutputFaces extends TileCore {
  val outputFaces = new DataSlotBlockFaceMap("outputs", this)
  val maxOutputs: Int

  def newOutput(bp: BlockPos, face: ForgeDirection): Int = {
    val bf = new BlockFace(bp, face)
    if (outputFaces.contains(bf)) {
      println("Adding already registered output??? " + bf.toString)
      return outputFaces(bf)
    }
    val rv = outputFaces.inverted
    for (i <- 0 until maxOutputs if !rv.contains(i)) {
      outputFaces += (bf -> i)
      outputFaces.updated()
      return i
    }
    val pl = worldObj.getClosestPlayer(bf.x, bf.y, bf.z, 10)
    if (pl != null) pl.addChatMessage(Misc.toLocal("deepcore.message.toomanyoutputs"))
    return -1
  }

  serverTick.listen(doOutputs)

  def doOutputs() {
    for (x<-outputFaces.keys) {
      val t = x.origin.getTile(worldObj, classOf[MIOutput])
      if (t.isDefined) t.get.doOutput(x.face)
    }
  }

  def removeOutput(bp: BlockPos, face: ForgeDirection) {
    val bf = new BlockFace(bp, face)
    outputFaces -= bf
    outputFaces.updated()
  }
}
