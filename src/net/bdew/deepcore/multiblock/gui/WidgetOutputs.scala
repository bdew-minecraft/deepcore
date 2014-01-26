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
import net.bdew.deepcore.multiblock.data.{RSMode, OutputConfigPower}
import java.text.DecimalFormat
import net.bdew.lib.gui.widgets.{WidgetSubcontainer, WidgetMultipane}
import net.bdew.deepcore.gui.Textures
import net.bdew.lib.Misc

class WidgetOutputs(p: Point, te: CIOutputFaces, rows: Int) extends WidgetSubcontainer(new Rect(p, 92, 19 * rows)) {
  for (i <- 0 until rows)
    add(new WidgetOutputRow(Point(0, 19 * i), te, i))
}

class WidgetOutputDisplay extends WidgetSubcontainer(Rect(20, 0, 72, 18))

class WidgetPowerOutput(te: CIOutputFaces, output: Int) extends WidgetOutputDisplay {
  def cfg = te.outputConfig(output).asInstanceOf[OutputConfigPower]
  val dec = new DecimalFormat("#,##0")
  val bt = add(new WidgetButtonIcon(Point(56, 1), clicked))

  val icons = Map(
    RSMode.ALWAYS -> Textures.Button16.enabled,
    RSMode.NEVER -> Textures.Button16.disabled,
    RSMode.RS_ON -> Textures.Button16.rsOn,
    RSMode.RS_OFF -> Textures.Button16.rsOff
  )

  val next = Map(
    RSMode.ALWAYS -> RSMode.RS_ON,
    RSMode.RS_ON -> RSMode.RS_OFF,
    RSMode.RS_OFF -> RSMode.NEVER,
    RSMode.NEVER -> RSMode.ALWAYS
  )

  override def draw(mouse: Point) {
    parent.getFontRenderer.drawString("%s %s/t".format(dec.format(cfg.avg), cfg.unit), 20, 5, 0x404040)
    bt.icon = icons(cfg.rsMode)
    bt.hover = Misc.toLocal("deepcore.rsmode." + cfg.rsMode.toString.toLowerCase)
    super.draw(mouse)
  }

  def clicked(b: WidgetButtonIcon) {
    cfg.rsMode = next(cfg.rsMode)
  }
}

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