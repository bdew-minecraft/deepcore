/*
 * Copyright (c) bdew, 2013 - 2014
 * https://github.com/bdew/deepcore
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * https://raw.github.com/bdew/deepcore/master/MMPL-1.0.txt
 */

package net.bdew.deepcore.map.test

import net.bdew.deepcore.map.{ResourceMapGen, SimplexNoise}

class SimplexNoiseGen(scale: Double) extends ResourceMapGen {
  val name = "Simplex [%f]".format(scale)
  def getValue(x: Int, y: Int, seed: Long, dim: Int): Float = SimplexNoise.noise(x * scale, y * scale).toFloat
}
