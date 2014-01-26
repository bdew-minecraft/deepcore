/*
 * Copyright (c) bdew, 2014
 * https://github.com/bdew/deepcore
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * https://raw.github.com/bdew/deepcore/master/MMPL-1.0.txt
 */

package net.bdew.deepcore.blocks.turbineController

import net.minecraft.entity.player.EntityPlayer
import net.bdew.lib.gui.{Point, Rect, BaseScreen}
import net.minecraft.util.ResourceLocation
import net.bdew.lib.gui.widgets.{WidgetLabel, WidgetFluidGauge}
import net.bdew.deepcore.gui.Textures
import net.bdew.lib.Misc
import net.bdew.lib.power.WidgetPowerGauge
import net.bdew.deepcore.multiblock.gui.WidgetOutputs

class GuiTurbine(val te: TileTurbineController, player: EntityPlayer) extends BaseScreen(new ContainerTurbine(te, player), 176, 160) {
  val texture = new ResourceLocation("deepcore:textures/gui/turbine.png")
  override def initGui() {
    super.initGui()
    widgets.add(new WidgetPowerGauge(new Rect(61, 19, 9, 58), Textures.powerFill, te.power))
    widgets.add(new WidgetFluidGauge(new Rect(9, 19, 9, 58), Textures.tankOverlay, te.fuel))
    widgets.add(new WidgetLabel(Misc.toLocal("deepcore.gui.turbine.title"), 8, 6, 4210752))
    widgets.add(new WidgetOutputs(Point(76, 18), te, 6))
    new Rect(8, 83, 78, 47)
  }
}
