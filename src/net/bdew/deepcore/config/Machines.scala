/*
 * Copyright (c) bdew, 2014
 * https://github.com/bdew/deepcore
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * https://raw.github.com/bdew/deepcore/master/MMPL-1.0.txt
 */

package net.bdew.deepcore.config

import net.bdew.deepcore.CreativeTabsDeepcore
import net.bdew.deepcore.blocks.turbineController.MachineTurbine
import net.bdew.lib.config.MachineManager

object Machines extends MachineManager(Tuning.getSection("Machines"), Config.guiHandler, CreativeTabsDeepcore.main) {
  val turbine = registerMachine(MachineTurbine)
}
