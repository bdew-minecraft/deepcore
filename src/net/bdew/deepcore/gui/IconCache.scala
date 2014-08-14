/*
 * Copyright (c) bdew, 2014
 * https://github.com/bdew/deepcore
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * https://raw.github.com/bdew/deepcore/master/MMPL-1.0.txt
 */

package net.bdew.deepcore.gui

import net.bdew.lib.render.IconPreloader

object IconCache extends IconPreloader(0) {
  val edgeIcon = TextureLoc("deepcore:connected/edge")
  val output = TextureLoc("deepcore:connected/output")
  val disabled = TextureLoc("deepcore:connected/disabled")
  val arTop = TextureLoc("deepcore:connected/artop")
  val arRight = TextureLoc("deepcore:connected/arright")
  val arBottom = TextureLoc("deepcore:connected/arbottom")
  val arLeft = TextureLoc("deepcore:connected/arleft")
}
