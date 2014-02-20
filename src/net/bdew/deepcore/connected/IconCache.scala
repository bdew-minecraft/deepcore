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
import net.bdew.deepcore.Deepcore
import net.minecraft.client.renderer.texture.IconRegister
import net.bdew.lib.render.IconPreloader

object IconCache extends IconPreloader(0) {
  var edgeIcon: Icon = null
  var output: Icon = null
  var disabled: Icon = null
  var arrows: Array[Icon] = null

  def registerIcons(reg: IconRegister) {
    Deepcore.logInfo("Registering shared textures...")
    edgeIcon = reg.registerIcon("deepcore:connected/edge")
    output = reg.registerIcon("deepcore:connected/output")
    disabled = reg.registerIcon("deepcore:connected/disabled")
    arrows = new Array[Icon](4)
    arrows(0) = reg.registerIcon("deepcore:connected/artop")
    arrows(1) = reg.registerIcon("deepcore:connected/arright")
    arrows(2) = reg.registerIcon("deepcore:connected/arbottom")
    arrows(3) = reg.registerIcon("deepcore:connected/arleft")
  }
}
