/*
 * Copyright (c) bdew, 2013 - 2014
 * https://github.com/bdew/deepcore
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * https://raw.github.com/bdew/deepcore/master/MMPL-1.0.txt
 */

package net.bdew.deepcore.blocks.euOutput

import net.bdew.lib.rotate.{IconType, RotateableTileBlock}
import net.minecraft.client.renderer.texture.IconRegister
import cpw.mods.fml.relauncher.{SideOnly, Side}
import net.minecraft.util.Icon
import net.minecraftforge.common.ForgeDirection
import net.minecraft.world.World
import net.bdew.deepcore.multiblock.block.BlockOutput

class BlockEuOutputBase[T <: TileEuOutputBase](name: String, texture: String, TEClass: Class[T])
  extends BlockOutput(name, "PowerOutput", TEClass)
  with RotateableTileBlock {
  var frontIcon: Icon = null

  override def setFacing(world: World, x: Int, y: Int, z: Int, facing: ForgeDirection) {
    super.setFacing(world, x, y, z, facing)
    getTE(world, x, y, z).tryConnect()
  }

  override def getDefaultFacing = ForgeDirection.SOUTH

  @SideOnly(Side.CLIENT)
  override def registerIcons(ir: IconRegister) {
    blockIcon = ir.registerIcon("deepcore:euoutput/main")
    frontIcon = ir.registerIcon("deepcore:euoutput/front_" + texture)
  }

  def getIcon(meta: Int, kind: IconType.Value) = if (kind == IconType.FRONT) frontIcon else blockIcon
}

class BlockEuOutputLV extends BlockEuOutputBase("EuOutputLV", "lv", classOf[TileEuOutputLV])

class BlockEuOutputMV extends BlockEuOutputBase("EuOutputMV", "mv", classOf[TileEuOutputMV])

class BlockEuOutputHV extends BlockEuOutputBase("EuOutputHV", "hv", classOf[TileEuOutputHV])