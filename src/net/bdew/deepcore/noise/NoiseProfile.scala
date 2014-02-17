/*
 * Copyright (c) bdew, 2013 - 2014
 * https://github.com/bdew/deepcore
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * https://raw.github.com/bdew/deepcore/master/MMPL-1.0.txt
 */

package net.bdew.deepcore.noise

import net.bdew.lib.Misc
import net.bdew.lib.recipes.gencfg.ConfigSection

case class NoiseProfile(name: String, xscale: Double, yscale: Double, bands: Int, trans: String,
                        gain: Double, lacunarity: Double, low: Double, high: Double, mul: Double, add: Double) {
  val tfunc = NoiseHelper.transFunc(trans)

  //  val gain = 0.5
  //  val lacunarity = 2

  override def toString = "%s: xs=%f ys=%f b=%d tf=%s gain=%f lac=%f low=%f high=%f mul=%f add=%f".format(productIterator.toArray: _*)

  def genNoise(x: Int, y: Int): Float = {
    var total = 0.0
    var frequency = 1.0
    var amplitude = gain
    var amps = 0.0

    for (i <- 0 until bands) {
      amps += amplitude
      total += SimplexNoise.noise(xscale * x * frequency, yscale * y * frequency) * amplitude
      frequency *= lacunarity
      amplitude *= gain
    }

    val t = tfunc(total / amps)

    Misc.clamp((if (t < low || t > high) 0 else t) * mul + add, 0.0, 1.0).toFloat
  }
}

object NoiseHelper {
  val transFunc = Map[String, (Double) => Double](
    ("direct", x => (x + 1) / 2),
    ("inverse", x => 1 - (x + 1) / 2),
    ("abs", x => Math.abs(x)),
    ("absinv", x => 1 - Math.abs(x))
  )

  def profileFromResource(name: String, cfg: ConfigSection) =
    NoiseProfile(name,
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