/*
 * Copyright (c) bdew, 2014
 * https://github.com/bdew/deepcore
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * https://raw.github.com/bdew/deepcore/master/MMPL-1.0.txt
 */

package net.bdew.deepcore.config

import net.bdew.lib.config.BlockManager
import net.bdew.deepcore.connected.{BakedTest, ConnectedBlock}
import net.bdew.deepcore.connected.test.{BlockTestModule1, BlockTestCore}

object Blocks extends BlockManager(Config.IDs) {
  val connected = regBlock(new ConnectedBlock(ids.getBlockId("Connected")),"Connected")
  val baked = regBlock(new BakedTest(ids.getBlockId("Baked")),"Baked")

  regBlock(new BlockTestCore(ids.getBlockId("TestCore")),"TestCore")
  regBlock(new BlockTestModule1(ids.getBlockId("TestModule1")),"TestModule1")
}