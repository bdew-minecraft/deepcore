/*
 * Copyright (c) bdew, 2013 - 2014
 * https://github.com/bdew/deepcore
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * https://raw.github.com/bdew/deepcore/master/MMPL-1.0.txt
 */

package net.bdew.deepcore.multiblock

import net.bdew.lib.recipes.gencfg.{ConfigSection, EntryDouble}

trait MachineCore {
  var tuning: ConfigSection

  def cfgSectionToMap(name: String) = tuning.getSection(name).map({
    case (n, EntryDouble(v)) => n -> v.toInt
    case (n, x) => sys.error("Invalid config value in section %s%s.%s = %s".format(tuning.pfx, name, n, x))
  }).toMap

  lazy val modules = cfgSectionToMap("Modules")
  lazy val required = cfgSectionToMap("Required")
}
