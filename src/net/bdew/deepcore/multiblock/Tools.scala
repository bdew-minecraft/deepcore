/*
 * Copyright (c) bdew, 2014
 * https://github.com/bdew/deepcore
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * https://raw.github.com/bdew/deepcore/master/MMPL-1.0.txt
 */

package net.bdew.deepcore.multiblock

import net.bdew.deepcore.multiblock.tile.{TileCore, TileModule}
import net.bdew.lib.block.BlockRef
import net.minecraft.world.World
import net.minecraftforge.common.util.ForgeDirection

import scala.collection.mutable

object Tools {
  def canConnect(world: World, core: BlockRef, kind: String): Boolean = {
    val t = core.getTile[TileCore](world).getOrElse(return false)
    t.getNumOfMoudules(kind) < t.cfg.modules.getOrElse(kind, return false)
  }

  def findConnections(world: World, start: BlockRef, kind: String) =
    ForgeDirection.VALID_DIRECTIONS.flatMap(x => {
      val p = start.neighbour(x)
      p.tile(world) match {
        case t: TileModule =>
          t.connected flatMap { core =>
            if (canConnect(world, core, kind))
              core.asInstanceOf[TileModule].connected.cval
            else
              None
          }
        case t: TileCore =>
          if (canConnect(world, p, kind))
            Some(p)
          else
            None
        case _=> None
      }
    }).distinct

  def getAdjancedConnected(w: World, core: BlockRef, pos: BlockRef, seen: mutable.Set[BlockRef]) =
    pos.neighbours.values
      .filterNot(seen.contains)
      .flatMap(_.getTile[TileModule](w))
      .filter(x => x.connected.contains(core))
      .map(_.mypos)

  def findReachableModules(world: World, core: BlockRef): Set[BlockRef] = {
    val seen = mutable.Set.empty[BlockRef]
    val queue = mutable.Queue.empty[BlockRef]
    queue ++= getAdjancedConnected(world, core, core, seen)
    while (queue.size > 0) {
      val current = queue.dequeue()
      seen.add(current)
      queue ++= getAdjancedConnected(world, core, current, seen)
    }
    return seen.toSet
  }
}
