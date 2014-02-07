/*
 * Copyright (c) bdew, 2013 - 2014
 * https://github.com/bdew/deepcore
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * https://raw.github.com/bdew/deepcore/master/MMPL-1.0.txt
 */

package net.bdew.deepcore.noise

object NoiseGen {
  def noise(x: Int, y: Int): Float = {
    val q = x + y * 57
    val n = (q << 13) ^ q
    1F - ((n * (n * n * 15731 + 789221) + 1376312589) & 0x7fffffff) / 1073741824F
  }

  def smoothNoise(x: Int, y: Int) = {
    val corners = (noise(x - 1, y - 1) + noise(x + 1, y - 1) + noise(x - 1, y + 1) + noise(x + 1, y + 1)) / 16
    val sides = (noise(x - 1, y) + noise(x + 1, y) + noise(x, y - 1) + noise(x, y + 1)) / 8
    val center = noise(x, y) / 4
    corners + sides + center
  }

  def interpolate(v1: Float, v2: Float, o: Float) = {
    val ft = o * 3.1415927
    val f = (1 - Math.cos(ft)).toFloat * 0.5F
    v1 * (1 - f) + v2 * f
  }

  def interpolatedNoise(x: Float, y: Float) = {

    val iX = x.floor.toInt
    val iY = y.floor.toInt
    val fX = x - iX
    val fY = y - iY

    val v1 = smoothNoise(iX, iY)
    val v2 = smoothNoise(iX + 1, iY)
    val v3 = smoothNoise(iX, iY + 1)
    val v4 = smoothNoise(iX + 1, iY + 1)

    val i1 = interpolate(v1, v2, fX)
    val i2 = interpolate(v3, v4, fX)
    interpolate(i1, i2, fY)
  }

  def generate(x: Int, y: Int) = {
    val p = 0.25F
    (for (i <- 1 to 5) yield {
      val f = 2 * i
      val a = p * i
      interpolatedNoise(x * f, y * f) * a
    }).sum * 0.5F + 0.5F
  }
}
