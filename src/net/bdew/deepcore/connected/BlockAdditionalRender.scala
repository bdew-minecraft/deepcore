/*
 * Copyright (c) bdew, 2014
 * https://github.com/bdew/deepcore
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * https://raw.github.com/bdew/deepcore/master/MMPL-1.0.txt
 */

package net.bdew.deepcore.connected

import net.minecraft.world.IBlockAccess
import net.minecraftforge.common.ForgeDirection
import net.minecraft.util.Icon
import net.bdew.lib.gui.Color

case class IconColor(icon: Icon, color: Color) {
  def this(icon: Icon, color: (Float, Float, Float)) = this(icon, new Color(color._1, color._2, color._3))
}

trait BlockAdditionalRender {
  def getOverlayIconAndColor(world: IBlockAccess, x: Int, y: Int, z: Int, face: ForgeDirection): IconColor
}
