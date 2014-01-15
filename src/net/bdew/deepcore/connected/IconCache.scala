/*
 * Copyright (c) bdew, 2014
 * https://github.com/bdew/deepcore
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * https://raw.github.com/bdew/deepcore/master/MMPL-1.0.txt
 */

package net.bdew.deepcore.connected

import net.minecraft.util.Icon
import net.minecraftforge.client.event.TextureStitchEvent
import net.bdew.deepcore.Deepcore
import net.minecraftforge.event.ForgeSubscribe

object IconCache {
  var edgeIcon: Icon = null

  @ForgeSubscribe
  def preTextureStitch(ev: TextureStitchEvent.Pre) {
    if (ev.map.getTextureType == 0) {
      Deepcore.logInfo("Preloading edge texture")
      edgeIcon = ev.map.registerIcon("deepcore:connected/edge")
    }
  }
}
