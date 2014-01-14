/*
 * Copyright (c) bdew, 2014
 * https://github.com/bdew/deepcore
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * https://raw.github.com/bdew/deepcore/master/MMPL-1.0.txt
 */

package net.bdew.deepcore.multiblock

import net.minecraft.world.World
import net.minecraftforge.common.ForgeDirection

object Tools {
  def canConnect(world: World, core: BlockPos, kind: String): Boolean = {
    if (core == null) return false
    val t = core.getTile(world, classOf[TileCore]).getOrElse(return false)
    val max = t.canAccept.getOrElse(kind, return false)
    return t.numConnected(kind) < max
  }

  def findConnections(world: World, start: BlockPos, kind: String): Seq[BlockPos] =
    ForgeDirection.VALID_DIRECTIONS.flatMap(x => {
      val p = start.adjanced(x)
      val t = p.getTile(world)
      if (t != null) {
        if (t.isInstanceOf[TileModule]) {
          if (canConnect(world, t.asInstanceOf[TileModule].connected, kind))
            Some(t.asInstanceOf[TileModule].connected.cval)
          else
            None
        } else if (t.isInstanceOf[TileCore]) {
          if (canConnect(world, p, kind))
            Some(p)
          else
            None
        } else None
      } else None
    })
}
