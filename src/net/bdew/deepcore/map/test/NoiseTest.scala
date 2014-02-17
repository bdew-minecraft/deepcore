package net.bdew.deepcore.map.test

/*
 * Copyright (c) bdew, 2013 - 2014
 * https://github.com/bdew/deepcore
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * https://raw.github.com/bdew/deepcore/master/MMPL-1.0.txt
 */

import java.awt.Color
import java.awt.image.BufferedImage
import java.io.File
import java.util.logging.{Level, Logger}
import javax.imageio.ImageIO
import net.bdew.deepcore.config.{Tuning, TuningLoader}
import net.bdew.deepcore.Deepcore
import net.bdew.deepcore.map._
import net.bdew.deepcore.map.ResourceProfile
import net.bdew.lib.recipes.gencfg.ConfigSection
import net.bdew.lib.{BdLib, Misc}
import scala.util.Random

object NoiseTest {
  val ITERATIONS = 100000
  val IMAGE = 1000

  def stddev(xs: Iterable[Float]): Double = {
    val avg = xs.sum / xs.size
    Math.sqrt(xs.map(x => Math.pow(x - avg, 2)).sum / xs.size)
  }

  def testProfile(gen: ResourceMapGen, dim: Int = 0, seed: Long = 0) {
    val rand = new Random()
    val s = System.nanoTime()
    val list = Range(0, ITERATIONS).map(x => gen.getValue(rand.nextInt(5000) - 2500, rand.nextInt(5000) - 2500, seed, dim)).toList
    val t = (System.nanoTime() - s).toDouble / ITERATIONS
    val stats = "min=%.3f max=%.3f avg=%.3f stdev=%.3f T=%.3f".format(
      list.min, list.max, list.sum / list.size, stddev(list), t
    )
    println("Testing %s: %s".format(gen.name, stats))
    val bufferedImage = new BufferedImage(IMAGE, IMAGE + 40, BufferedImage.TYPE_INT_ARGB)
    val hi = IMAGE / 2
    for (x <- 0 until IMAGE; y <- 0 until IMAGE) {
      val v = Misc.clamp((gen.getValue(x - hi, y - hi, seed, dim) * 255).toInt, 0, 255)
      val vv = 0xFF000000 | v << 16 | v << 8 | v
      bufferedImage.setRGB(x, y, vv)
    }
    val g = bufferedImage.createGraphics()
    g.setColor(new Color(255, 0, 0))
    g.fillRect(0, IMAGE, IMAGE, 40)
    g.setColor(new Color(255, 255, 255))
    g.drawString(gen.toString, 5, IMAGE + 15)
    g.drawString(stats, 5, IMAGE + 30)
    ImageIO.write(bufferedImage, "png", new File("ntest/" + gen.name + ".png"))
  }

  def testBasic(name: String, scale: Double, bands: Int, tf: String) = testProfile(ResourceProfile(name, 1, scale, scale, bands, tf, 0.5, 2, -100, +100, 1, 0), 0, 0)

  def main(args: Array[String]) {
    val f = new File("ntest")
    f.mkdirs()
    println("Output to: " + f.getAbsolutePath)

    val l = new Random()
    testProfile(new WhiteNoiseGen)
    testProfile(new SimplexNoiseGen(0.1))
    for (f <- ResourceHelper.transFunc.keys; x <- 1 to 4) testBasic(f + "_" + x + "b", 0.1, x, f)

    val log = Logger.getLogger("main")
    log.setLevel(Level.OFF)
    BdLib.log = log
    Deepcore.log = log

    TuningLoader.load("resources")

    for ((n, c) <- Tuning.getSection("Resources").filterType(classOf[ConfigSection]))
      testProfile(ResourceHelper.profileFromResource(n, c))

    var S = l.nextLong()
    testProfile(ResourceHelper.profileFromResource("Oil D0 S0", Tuning.getSection("Resources").getSection("Oil")), 0, S)
    testProfile(ResourceHelper.profileFromResource("Oil D1 S0", Tuning.getSection("Resources").getSection("Oil")), 1, S)
    testProfile(ResourceHelper.profileFromResource("Oil D2 S0", Tuning.getSection("Resources").getSection("Oil")), -1, S)
    S = l.nextLong()
    testProfile(ResourceHelper.profileFromResource("Oil D0 S1", Tuning.getSection("Resources").getSection("Oil")), 0, S)
    testProfile(ResourceHelper.profileFromResource("Oil D1 S1", Tuning.getSection("Resources").getSection("Oil")), 1, S)
    testProfile(ResourceHelper.profileFromResource("Oil D2 S1", Tuning.getSection("Resources").getSection("Oil")), -1, S)
    S = l.nextLong()
    testProfile(ResourceHelper.profileFromResource("Oil D0 S2", Tuning.getSection("Resources").getSection("Oil")), 0, S)
    testProfile(ResourceHelper.profileFromResource("Oil D1 S2", Tuning.getSection("Resources").getSection("Oil")), 1, S)
    testProfile(ResourceHelper.profileFromResource("Oil D2 S2", Tuning.getSection("Resources").getSection("Oil")), -1, S)

  }
}