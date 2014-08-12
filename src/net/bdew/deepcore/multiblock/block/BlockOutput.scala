/*
 * Copyright (c) bdew, 2013 - 2014
 * https://github.com/bdew/deepcore
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * https://raw.github.com/bdew/deepcore/master/MMPL-1.0.txt
 */

package net.bdew.deepcore.multiblock.block

import net.bdew.deepcore.connected.IconCache
import net.bdew.deepcore.multiblock.Outputs
import net.bdew.deepcore.multiblock.tile.TileOutput
import net.bdew.lib.block.{BlockFace, BlockRef}
import net.bdew.lib.render.connected.{BlockAdditionalRender, ConnectedHelper, FaceOverlay}
import net.minecraft.world.IBlockAccess
import net.minecraftforge.common.util.ForgeDirection

class BlockOutput[T <: TileOutput](name: String, kind: String, TEClass: Class[T]) extends BlockModule(name, kind, TEClass) with BlockAdditionalRender {
  def getAdjancedFaces(face: ForgeDirection, bp: BlockRef) = {
    val sides = ConnectedHelper.faceAdjanced(face)
    Seq(
      (BlockFace(bp, sides.top), IconCache.arrows(0)),
      (BlockFace(bp, sides.right), IconCache.arrows(1)),
      (BlockFace(bp, sides.bottom), IconCache.arrows(2)),
      (BlockFace(bp, sides.left), IconCache.arrows(3))
    )
  }

  def getFaceOverlays(world: IBlockAccess, x: Int, y: Int, z: Int, face: ForgeDirection): List[FaceOverlay] = {
    var result = List.empty[FaceOverlay]
    Option(getTE(world, x, y, z)) flatMap (_.getCore) map { core =>
      val bf = BlockFace(x, y, z, face)
      if (core.outputFaces.contains(bf))
        result :+= FaceOverlay(IconCache.output, Outputs.color(core.outputFaces(bf)))
      for ((bf, icon) <- getAdjancedFaces(face, bf.origin)) {
        if (core.outputFaces.contains(bf))
          result :+= FaceOverlay(icon, Outputs.color(core.outputFaces(bf)))
      }
    }
    return result
  }

}
