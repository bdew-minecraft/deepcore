/*
 * Copyright (c) bdew, 2013 - 2014
 * https://github.com/bdew/deepcore
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * https://raw.github.com/bdew/deepcore/master/MMPL-1.0.txt
 */

package net.bdew.deepcore.resources

import net.bdew.lib.render.IconPreloader
import net.minecraft.client.renderer.texture.IconRegister

object IconLoader extends IconPreloader(1) {
  val scannerModuleHint = Entry("deepcore:hint/scannermodule")
  val invalid = Entry("deepcore:invalid")
  override def registerIcons(reg: IconRegister) {
    for (res <- ResourceManager.list) {
      res.resIcon = reg.registerIcon("deepcore:scanner/" + res.name.toLowerCase + "/resource")
      res.moduleIcon = reg.registerIcon("deepcore:scanner/" + res.name.toLowerCase + "/module")
    }
  }
}
