/*
 * Copyright (c) bdew, 2014
 * https://github.com/bdew/deepcore
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * https://raw.github.com/bdew/deepcore/master/MMPL-1.0.txt
 */

package net.bdew.deepcore.connected

import net.bdew.lib.render.IconPreloader

object IconCache extends IconPreloader(0) {
  val edgeIcon = Entry("deepcore:connected/edge")
  val output = Entry("deepcore:connected/output")
  val disabled = Entry("deepcore:connected/disabled")
  val arrows = List(
    Entry("deepcore:connected/artop"),
    Entry("deepcore:connected/arright"),
    Entry("deepcore:connected/arbottom"),
    Entry("deepcore:connected/arleft")
  )
}
