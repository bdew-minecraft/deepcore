/*
 * Copyright (c) bdew, 2014
 * https://github.com/bdew/deepcore
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * https://raw.github.com/bdew/deepcore/master/MMPL-1.0.txt
 */

package net.bdew.deepcore.machines.compact

import net.bdew.lib.data.base.TileDataSlots
import net.bdew.lib.tile.TileExtended
import net.bdew.lib.tile.inventory.{BreakableInventoryTile, PersistentInventoryTile, SidedInventory}

class TileCompactifier extends TileExtended
with TileDataSlots
with PersistentInventoryTile
with BreakableInventoryTile
with SidedInventory {

  def getSizeInventory: Int = ???
}
