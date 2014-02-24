/*
 * Copyright (c) bdew, 2013 - 2014
 * https://github.com/bdew/deepcore
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * https://raw.github.com/bdew/deepcore/master/MMPL-1.0.txt
 */

package net.bdew.deepcore.resources.map

import net.bdew.deepcore.resources.ResourceManager
import net.bdew.lib.Misc

case class MapGenIntersect(map1: String, map2: String, low: Double, high: Double, mul: Double, add: Double) extends ResourceMapGen {
  lazy val res1 = ResourceManager.byName(map1).map
  lazy val res2 = ResourceManager.byName(map2).map

  def getValue(x: Int, y: Int, seed: Long, dim: Int) = {
    val t = res1.getValue(x, y, seed, dim) * res2.getValue(x, y, seed, dim)
    Misc.clamp((if (t < low || t > high) 0 else t) * mul + add, 0.0, 1.0).toFloat
  }

  override def toString = "Mul(res1=%s res2=%s low=%f high=%f mul=%f add=%f)".format(productIterator.toArray: _*)
}
