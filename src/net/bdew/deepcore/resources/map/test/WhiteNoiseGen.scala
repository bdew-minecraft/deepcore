/*
 * Copyright (c) bdew, 2013 - 2014
 * https://github.com/bdew/deepcore
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * https://raw.github.com/bdew/deepcore/master/MMPL-1.0.txt
 */

package net.bdew.deepcore.resources.map.test

import scala.util.Random
import net.bdew.deepcore.resources.map.ResourceMapGen

class WhiteNoiseGen extends ResourceMapGen {
  val r = new Random()
  def getValue(x: Int, y: Int, seed: Long, dim: Int): Float = r.nextFloat()
}
