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
  var output: Icon = null
  var disabled: Icon = null
  var arrows: Array[Icon] = null

  @ForgeSubscribe
  def preTextureStitch(ev: TextureStitchEvent.Pre) {
    if (ev.map.getTextureType == 0) {
      Deepcore.logInfo("Registering shared textures...")
      edgeIcon = ev.map.registerIcon("deepcore:connected/edge")
      output = ev.map.registerIcon("deepcore:connected/output")
      disabled = ev.map.registerIcon("deepcore:connected/disabled")
      arrows = new Array[Icon](4)
      arrows(0) = ev.map.registerIcon("deepcore:connected/artop")
      arrows(1) = ev.map.registerIcon("deepcore:connected/arright")
      arrows(2) = ev.map.registerIcon("deepcore:connected/arbottom")
      arrows(3) = ev.map.registerIcon("deepcore:connected/arleft")
    }
  }
}
