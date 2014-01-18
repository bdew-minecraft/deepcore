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
import scala.collection.mutable
import net.bdew.deepcore.multiblock.data.{BlockFace, BlockPos}
import net.bdew.deepcore.multiblock.tile.{TileModule, TileCore}
import net.bdew.deepcore.multiblock.interact.CIOutputFaces

object Tools {
  def canConnect(world: World, core: BlockPos, kind: String): Boolean = {
    if (core == null) return false
    val t = core.getTile(world, classOf[TileCore]).getOrElse(return false)
    val max = t.canAccept.getOrElse(kind, return false)
    return t.getNumOfMoudules(kind) < max
  }

  def findConnections(world: World, start: BlockPos, kind: String) =
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
    }).distinct

  def getAdjancedConnected(w: World, core: BlockPos, pos: BlockPos, seen: mutable.Set[BlockPos]) =
    ForgeDirection.VALID_DIRECTIONS.map(pos.adjanced)
      .filter(!seen.contains(_))
      .flatMap(_.getTile(w, classOf[TileModule]))
      .filter(x => x.connected.cval == core)
      .map(_.mypos)

  def findReachableModules(world: World, core: BlockPos): Set[BlockPos] = {
    val seen = mutable.Set.empty[BlockPos]
    val queue = mutable.Queue.empty[BlockPos]
    queue ++= getAdjancedConnected(world, core, core, seen)
    while (queue.size > 0) {
      val current = queue.dequeue()
      seen.add(current)
      queue ++= getAdjancedConnected(world, core, current, seen)
    }
    return seen.toSet
  }

  def updateOutputs(core: CIOutputFaces, module: BlockPos, faces: Set[ForgeDirection]) {
    val known = core.outputFaces.filter(_._1.origin == module).map(_._1.face).toSet
    val toAdd = faces -- known
    val toRemove = known -- faces
    toRemove.foreach(x => core.removeOutput(module, x))
    toAdd.foreach(x => core.newOutput(module, x))
  }
}
