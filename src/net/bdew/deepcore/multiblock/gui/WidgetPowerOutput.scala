/*
 * Copyright (c) bdew, 2013 - 2014
 * https://github.com/bdew/deepcore
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * https://raw.github.com/bdew/deepcore/master/MMPL-1.0.txt
 */

package net.bdew.deepcore.multiblock.gui

import net.bdew.deepcore.multiblock.interact.CIOutputFaces
import net.bdew.deepcore.multiblock.data.{RSMode, OutputConfigPower}
import java.text.DecimalFormat
import net.bdew.lib.gui.Point
import net.bdew.deepcore.gui.Textures
import net.bdew.lib.Misc
import net.minecraft.nbt.NBTTagCompound
import net.bdew.deepcore.network.PacketHelper
import net.bdew.lib.gui.widgets.WidgetDynLabel

class WidgetPowerOutput(te: CIOutputFaces, output: Int) extends WidgetOutputDisplay {
  def cfg = te.outputConfig(output).asInstanceOf[OutputConfigPower]
  val dec = new DecimalFormat("#,##0")
  val bt = add(new WidgetButtonIcon(Point(56, 1), clicked))
  add(new WidgetDynLabel("%s %s/t".format(dec.format(cfg.avg), cfg.unit), 1, 5, 0x404040))

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
    bt.icon = icons(cfg.rsMode)
    bt.hover = Misc.toLocal("deepcore.rsmode." + cfg.rsMode.toString.toLowerCase)
    super.draw(mouse)
  }

  def clicked(b: WidgetButtonIcon) {
    val d = new NBTTagCompound()
    d.setByte("rsMode", next(cfg.rsMode).id.toByte)
    PacketHelper.sendOutputConfig(output, d)
  }
}
