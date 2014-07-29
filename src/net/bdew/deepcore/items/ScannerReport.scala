/*
 * Copyright (c) bdew, 2013 - 2014
 * https://github.com/bdew/deepcore
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * https://raw.github.com/bdew/deepcore/master/MMPL-1.0.txt
 */

package net.bdew.deepcore.items

import java.util

import net.bdew.deepcore.resources.{Resource, ResourceManager}
import net.bdew.lib.Misc
import net.bdew.lib.items.SimpleItem
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound

case class ScannerReportData(x: Int, y: Int, dim: Int, resource: Option[Resource], depth: Int, abundance: Int)

object ScannerReport extends SimpleItem("ScannerReport") {

  setMaxStackSize(1)

  def newStack(x: Int, y: Int, dim: Int, resource: Resource, depth: Int, abundance: Int) = {
    val stack = new ItemStack(this)
    val nbt = new NBTTagCompound()
    nbt.setInteger("x", x)
    nbt.setInteger("y", y)
    nbt.setInteger("dim", dim)
    nbt.setInteger("res", resource.id)
    nbt.setInteger("depth", depth)
    nbt.setInteger("abundance", abundance)
    stack.setTagCompound(nbt)
    stack
  }

  def getInfo(stack: ItemStack) = {
    val nbt = stack.getTagCompound
    ScannerReportData(
      x = nbt.getInteger("x"),
      y = nbt.getInteger("y"),
      dim = nbt.getInteger("dim"),
      resource = ResourceManager.byId.get(nbt.getInteger("res")),
      depth = nbt.getInteger("depth"),
      abundance = nbt.getInteger("abundance")
    )
  }

  override def addInformation(stack: ItemStack, player: EntityPlayer, lst: util.List[_], par4: Boolean) {
    if (!stack.hasTagCompound) return
    val info = getInfo(stack)
    val list = lst.asInstanceOf[util.List[String]]
    if (info.resource.isDefined) {
      list.add(Misc.toLocalF("deepcore.label.resource", info.resource.get.getLocalizedName))
      list.add(Misc.toLocalF("deepcore.label.position", info.x, info.y, info.dim))
      list.add(Misc.toLocalF("deepcore.label.depth", info.depth))
      list.add(Misc.toLocalF("deepcore.label.abundance", info.abundance))
    } else {
      list.add(Misc.toLocal("deepcore.resource.invalid"))
    }
  }
}
