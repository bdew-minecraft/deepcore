/*
 * Copyright (c) bdew, 2013 - 2014
 * https://github.com/bdew/deepcore
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * https://raw.github.com/bdew/deepcore/master/MMPL-1.0.txt
 */

package net.bdew.deepcore.map

import net.bdew.lib.recipes.gencfg.ConfigSection

object ResourceHelper {
  val transFunc = Map[String, (Double) => Double](
    ("direct", x => (x + 1) / 2),
    ("inverse", x => 1 - (x + 1) / 2),
    ("abs", x => Math.abs(x)),
    ("absinv", x => 1 - Math.abs(x))
  )

  def profileFromResource(name: String, cfg: ConfigSection) =
    ResourceProfile(name,
      id = cfg.getInt("id"),
      xscale = cfg.getDouble("xScale"),
      yscale = cfg.getDouble("yScale"),
      bands = cfg.getInt("Bands"),
      trans = cfg.getString("TFunc"),
      gain = cfg.getDouble("Gain"),
      lacunarity = cfg.getDouble("Lac"),
      low = cfg.getDouble("Low"),
      high = cfg.getDouble("High"),
      mul = cfg.getDouble("Mul"),
      add = cfg.getDouble("Add")
    )
}
