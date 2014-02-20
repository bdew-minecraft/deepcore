/*
 * Copyright (c) bdew, 2014
 * https://github.com/bdew/deepcore
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * https://raw.github.com/bdew/deepcore/master/MMPL-1.0.txt
 */

package net.bdew.deepcore.config

import net.bdew.deepcore.items.{Canister, HandPump}
import net.bdew.lib.config.ItemManager
import net.bdew.deepcore.items.scanner.Scanner
import net.bdew.deepcore.items.scanner.modules.ItemScannerModule

object Items extends ItemManager(Config.IDs) {
  val handPump = regItem(new HandPump(ids.getItemId("HandPump")))
  val canister = regItem(new Canister(ids.getItemId("Canister")))

  regItem(new Scanner(ids.getItemId("Scanner")))

  regItem(new ItemScannerModule(ids.getItemId("ScannerModule")), "ScannerModule")

  regSimpleItem("TurbineBlade")
  regSimpleItem("TurbineRotor")
  regSimpleItem("IronFrame")
  regSimpleItem("PowerIO")
  regSimpleItem("IronTubing")
  regSimpleItem("IronWiring")
  regSimpleItem("Controller")

  regSimpleItem("ScannerModuleFrame")
}