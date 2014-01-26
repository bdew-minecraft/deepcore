/*
 * Copyright (c) bdew, 2013 - 2014
 * https://github.com/bdew/deepcore
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * https://raw.github.com/bdew/deepcore/master/MMPL-1.0.txt
 */

package net.bdew.deepcore.multiblock.gui

import net.bdew.lib.gui._
import net.bdew.deepcore.multiblock.interact.CIOutputFaces
import net.bdew.lib.gui.Rect
import net.bdew.deepcore.multiblock.data.OutputConfigPower
import net.bdew.lib.gui.widgets.{WidgetSubcontainer, WidgetMultipane}

class WidgetOutputs(p: Point, te: CIOutputFaces, rows: Int) extends WidgetSubcontainer(new Rect(p, 92, 19 * rows)) {
  for (i <- 0 until rows)
    add(new WidgetOutputRow(Point(0, 19 * i), te, i))
}

class WidgetOutputDisplay extends WidgetSubcontainer(Rect(20, 0, 72, 18))

class WidgetOutputRow(p: Point, te: CIOutputFaces, output: Int) extends WidgetMultipane(new Rect(p, 92, 18)) {
  add(new WidgetOutputIcon(Point(1, 1), te, output))
  val emptyPane = addPane(new WidgetSubcontainer(rect))
  val powerPane = addPane(new WidgetPowerOutput(te, output))

  def getActivePane =
    if (te.outputConfig.contains(output))
      if (te.outputConfig(output).isInstanceOf[OutputConfigPower])
        powerPane
      else
        emptyPane
    else
      emptyPane
}