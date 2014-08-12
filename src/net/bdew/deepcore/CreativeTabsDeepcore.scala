/*
 * Copyright (c) bdew, 2013 - 2014
 * https://github.com/bdew/deepcore
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * https://raw.github.com/bdew/deepcore/master/MMPL-1.0.txt
 */

package net.bdew.deepcore

import net.bdew.deepcore.items.scanner.Scanner
import net.bdew.lib.CreativeTabContainer

object CreativeTabsDeepcore extends CreativeTabContainer {
  val main = new Tab("bdew.deepcore", Scanner)

}
