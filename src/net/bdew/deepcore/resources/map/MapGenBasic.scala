/*
 * Copyright (c) bdew, 2013 - 2014
 * https://github.com/bdew/deepcore
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * https://raw.github.com/bdew/deepcore/master/MMPL-1.0.txt
 */

package net.bdew.deepcore.resources.map

import net.bdew.lib.Misc

case class MapGenBasic(id: Int, xscale: Double, yscale: Double, bands: Int, trans: String,
                       gain: Double, lacunarity: Double, low: Double, high: Double, mul: Double, add: Double) extends ResourceMapGen {

  val tfunc: (Double) => Double = trans match {
    case "direct" => x => (x + 1) / 2
    case "inverse" => x => 1 - (x + 1) / 2
    case "abs" => x => Math.abs(x)
    case "absinv" => x => 1 - Math.abs(x)
  }

  override def toString = "id=%d xs=%f ys=%f b=%d tf=%s gain=%f lac=%f low=%f high=%f mul=%f add=%f".format(productIterator.toArray: _*)

  def getValue(x: Int, y: Int, seed: Long, dim: Int): Float = {
    var total = 0.0
    var frequency = 1.0
    var amplitude = gain
    var amps = 0.0

    val smod = (seed + dim) * id * 6364136223846793005L + 1442695040888963407L
    val xs = x + (smod & 0x7FFFFFFF).toInt
    val ys = y + ((smod >> 32) & 0x7FFFFFFF).toInt

    for (i <- 0 until bands) {
      amps += amplitude
      total += SimplexNoise.noise(xscale * xs * frequency, yscale * ys * frequency) * amplitude
      frequency *= lacunarity
      amplitude *= gain
    }

    val t = tfunc(total / amps)

    Misc.clamp((if (t < low || t > high) 0 else t) * mul + add, 0.0, 1.0).toFloat
  }
}

