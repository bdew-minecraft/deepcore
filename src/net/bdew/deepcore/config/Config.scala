/*
 * Copyright (c) bdew, 2014
 * https://github.com/bdew/deepcore
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * https://raw.github.com/bdew/deepcore/master/MMPL-1.0.txt
 */

package net.bdew.deepcore.config

import net.minecraftforge.common.Configuration
import java.io.File
import net.bdew.lib.config.IdManager
import net.bdew.lib.gui.GuiHandler

object Config {
  var IDs: IdManager = null
  val guiHandler = new GuiHandler

  def load(cfg: File): Configuration = {
    val c = new Configuration(cfg)
    c.load()
    try {

      IDs = new IdManager(c, 16000, 3400)
      Items.load()
      Blocks.load()
      Machines.load()

    } finally {
      c.save()
    }

    return c
  }
}