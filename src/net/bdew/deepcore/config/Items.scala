/*
 * Copyright (c) bdew, 2014
 * https://github.com/bdew/deepcore
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * https://raw.github.com/bdew/deepcore/master/MMPL-1.0.txt
 */

package net.bdew.deepcore.config

import net.bdew.deepcore.items.scanner.Scanner
import net.bdew.deepcore.items.scanner.modules.ItemScannerModule
import net.bdew.deepcore.items.{Canister, HandPump, ScannerReport}
import net.bdew.lib.config.ItemManager

object Items extends ItemManager {
  regItem(HandPump)
  regItem(Canister)
  regItem(Scanner)

  val scannerModule = regItem(ItemScannerModule, "ScannerModule")

  regSimpleItem("TurbineBlade")
  regSimpleItem("TurbineRotor")
  regSimpleItem("IronFrame")
  regSimpleItem("PowerIO")
  regSimpleItem("IronTubing")
  regSimpleItem("IronWiring")
  regSimpleItem("Controller")

  regSimpleItem("ScannerModuleFrame")

  val scannerReportBlank = regSimpleItem("ScannerReportBlank")

  regItem(ScannerReport)
}