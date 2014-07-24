/*
 * Copyright (c) bdew, 2013 - 2014
 * https://github.com/bdew/deepcore
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * https://raw.github.com/bdew/deepcore/master/MMPL-1.0.txt
 */

package net.bdew.deepcore.items.scanner

import cpw.mods.fml.common.FMLCommonHandler
import cpw.mods.fml.common.eventhandler.SubscribeEvent
import cpw.mods.fml.common.gameevent.PlayerEvent.{PlayerChangedDimensionEvent, PlayerLoggedOutEvent, PlayerRespawnEvent}
import net.bdew.deepcore.Deepcore
import net.minecraft.entity.player.EntityPlayer

object PlayerChunkTracker {
  var map = Map.empty[EntityPlayer, (Int, Int, Int)]

  def init() {
    Deepcore.serverStarting.listen(x => map = map.empty)
    FMLCommonHandler.instance().bus().register(this)
  }

  def update(p: EntityPlayer, x: Int, y: Int, res: Int): Boolean = {
    if (map.contains(p) && map(p) ==(x, y, res)) return false
    map += (p ->(x, y, res))
    return true
  }

  def reset(p: EntityPlayer) {
    if (map.contains(p)) map -= p
  }

  @SubscribeEvent
  def hadlePlayerLogout(ev: PlayerLoggedOutEvent) = reset(ev.player)

  @SubscribeEvent
  def hadlePlayerChangedDimension(ev: PlayerChangedDimensionEvent) = reset(ev.player)

  @SubscribeEvent
  def hadlePlayerRespawn(ev: PlayerRespawnEvent) = reset(ev.player)
}
