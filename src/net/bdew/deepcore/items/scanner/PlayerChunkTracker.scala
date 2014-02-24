/*
 * Copyright (c) bdew, 2013 - 2014
 * https://github.com/bdew/deepcore
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * https://raw.github.com/bdew/deepcore/master/MMPL-1.0.txt
 */

package net.bdew.deepcore.items.scanner

import cpw.mods.fml.common.IPlayerTracker
import net.minecraft.entity.player.EntityPlayer
import cpw.mods.fml.common.registry.GameRegistry
import net.bdew.deepcore.Deepcore

object PlayerChunkTracker extends IPlayerTracker {
  var map = Map.empty[EntityPlayer, (Int, Int, Int)]

  def init() {
    Deepcore.serverStarting.listen(x => map = map.empty)
    GameRegistry.registerPlayerTracker(this)
  }

  def update(p: EntityPlayer, x: Int, y: Int, res: Int): Boolean = {
    if (map.contains(p) && map(p) ==(x, y, res)) return false
    map += (p ->(x, y, res))
    return true
  }

  def reset(p: EntityPlayer) {
    if (map.contains(p)) map -= p
  }

  def onPlayerLogin(player: EntityPlayer) {}
  def onPlayerLogout(player: EntityPlayer) = reset(player)
  def onPlayerChangedDimension(player: EntityPlayer) = reset(player)
  def onPlayerRespawn(player: EntityPlayer) = reset(player)
}
