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
import net.bdew.lib.gui.{Rect, BaseScreen}
import net.minecraft.util.ResourceLocation
import net.bdew.lib.gui.widgets.{WidgetLabel, WidgetFluidGauge}
import net.bdew.deepcore.gui.Textures
import net.bdew.lib.Misc
import net.bdew.lib.power.WidgetPowerGauge

class GuiTurbine(val te: TileTurbineController, player: EntityPlayer) extends BaseScreen(new ContainerTurbine(te, player), 176, 160) {
  val texture = new ResourceLocation("deepcore:textures/gui/turbine.png")
  override def initGui() {
    super.initGui()
    addWidget(new WidgetPowerGauge(new Rect(75, 19, 9, 58), Textures.powerFill, te.power))
    addWidget(new WidgetFluidGauge(new Rect(10, 19, 9, 58), Textures.tankOverlay, te.fuel))
    addWidget(new WidgetLabel(Misc.toLocal("deepcore.gui.turbine.title"), 8, 6, 4210752))

    new Rect(8,83,78,47)
  }
}
