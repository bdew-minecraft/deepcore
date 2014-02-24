/*
 * Copyright (c) bdew, 2013 - 2014
 * https://github.com/bdew/deepcore
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * https://raw.github.com/bdew/deepcore/master/MMPL-1.0.txt
 */

package net.bdew.deepcore.resources

import net.bdew.lib.recipes.gencfg.ConfigSection
import net.bdew.deepcore.resources.map.{MapGenIntersect, MapGenBasic}
import net.bdew.deepcore.config.Tuning

object ResourceManager {
  val list = Tuning.getSection("Resources").filterType(classOf[ConfigSection]).map({ case (x, s) => new Resource(x, s) }).toSeq
  val byId = list.map(x => x.id -> x).toMap
  val byName = list.map(x => x.name -> x).toMap

  def isValid(id: Int) = byId.contains(id)

  def mapGenFromCfg(name: String, cfg: ConfigSection) =
    cfg.getString("MapType") match {
      case "basic" => MapGenBasic(
        id = cfg.getInt("ID"),
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
      case "intersect" => MapGenIntersect(
        map1 = cfg.getString("Map1"),
        map2 = cfg.getString("Map2"),
        low = cfg.getDouble("Low"),
        high = cfg.getDouble("High"),
        mul = cfg.getDouble("Mul"),
        add = cfg.getDouble("Add")
      )
    }
}
