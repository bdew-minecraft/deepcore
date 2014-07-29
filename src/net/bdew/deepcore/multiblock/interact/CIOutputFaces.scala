/*
 * Copyright (c) bdew, 2014
 * https://github.com/bdew/deepcore
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * https://raw.github.com/bdew/deepcore/master/MMPL-1.0.txt
 */

package net.bdew.deepcore.multiblock.interact

import net.bdew.deepcore.multiblock.data.{BlockFace, BlockPos, _}
import net.bdew.deepcore.multiblock.tile.{TileCore, TileModule}
import net.minecraft.util.ChatComponentTranslation
import net.minecraftforge.common.util.ForgeDirection

trait CIOutputFaces extends TileCore {
  val maxOutputs: Int

  val outputFaces = new DataSlotBlockFaceMap("outputs", this)
  val outputConfig = new DataSlotOutputConfig("cfg", this, maxOutputs)

  def newOutput(bp: BlockPos, face: ForgeDirection, cfg: OutputConfig): Int = {
    val bf = new BlockFace(bp, face)
    if (outputFaces.contains(bf)) {
      println("Adding already registered output??? " + bf.toString)
      return outputFaces(bf)
    }
    val rv = outputFaces.inverted
    for (i <- 0 until maxOutputs if !rv.contains(i)) {
      outputFaces += (bf -> i)
      outputConfig += i -> cfg
      outputFaces.updated()
      return i
    }
    val pl = getWorldObj.getClosestPlayer(bf.x, bf.y, bf.z, 10)
    if (pl != null) pl.addChatMessage(new ChatComponentTranslation("deepcore.message.toomanyoutputs"))
    return -1
  }

  serverTick.listen(doOutputs)

  override def moduleRemoved(module: TileModule) {
    for ((bf, n) <- outputFaces if bf.origin == module.mypos) {
      outputFaces -= bf
      outputConfig -= n
    }
    outputFaces.updated()
    outputConfig.updated()
    super.moduleRemoved(module)
  }

  def doOutputs() {
    for ((x, n) <- outputFaces) {
      val t = x.origin.getTile(getWorldObj, classOf[MIOutput])
      if (t.isDefined) {
        if (!outputConfig.isDefinedAt(n) || outputConfig(n).isInstanceOf[OutputConfigInvalid])
          outputConfig(n) = t.get.makeCfgObject(x.face)
        t.get.doOutput(x.face, outputConfig(n))
      }
    }
  }

  def removeOutput(bp: BlockPos, face: ForgeDirection) {
    val bf = new BlockFace(bp, face)
    outputConfig -= outputFaces(bf)
    outputFaces -= bf
    outputFaces.updated()
  }
}
