/*
 * Copyright (c) bdew, 2014
 * https://github.com/bdew/deepcore
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * https://raw.github.com/bdew/deepcore/master/MMPL-1.0.txt
 */

package net.bdew.deepcore.config

import java.io.{File, FileReader, InputStreamReader}

import net.bdew.deepcore.Deepcore
import net.bdew.lib.recipes.gencfg._
import net.bdew.lib.recipes.{RecipeLoader, RecipeParser}

object Tuning extends ConfigSection

object TuningLoader {

  class Parser extends RecipeParser with GenericConfigParser

  class Loader extends RecipeLoader with GenericConfigLoader {
    val cfgStore = Tuning

    override def newParser() = new Parser()
  }

  val loader = new Loader

  def loadDealayed() = loader.processDelayedStatements()

  def load(part: String, checkJar: Boolean = true) {
    val f = new File(Deepcore.configDir, "%s-%s.cfg".format(Deepcore.modId, part))
    val r = if (f.exists() && f.canRead) {
      Deepcore.logInfo("Loading configuration from %s", f.toString)
      new FileReader(f)
    } else if (checkJar) {
      val res = "/assets/%s/%s-%s.cfg".format(Deepcore.modId, Deepcore.modId, part)
      val stream = this.getClass.getResourceAsStream(res)
      Deepcore.logInfo("Loading configuration from JAR - %s", this.getClass.getResource(res))
      new InputStreamReader(this.getClass.getResourceAsStream("/assets/%s/%s-%s.cfg".format(Deepcore.modId, Deepcore.modId, part)))
    } else {
      return
    }
    try {
      loader.load(r)
    } finally {
      r.close()
    }
  }
}

