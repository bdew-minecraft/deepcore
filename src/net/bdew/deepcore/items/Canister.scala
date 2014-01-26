/*
 * Copyright (c) bdew, 2014
 * https://github.com/bdew/deepcore
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * https://raw.github.com/bdew/deepcore/master/MMPL-1.0.txt
 */

package net.bdew.deepcore.items

import net.bdew.lib.items.SimpleItem
import net.minecraftforge.fluids.{IFluidHandler, FluidStack, IFluidContainerItem}
import net.minecraft.item.ItemStack
import net.bdew.lib.Misc
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.entity.player.EntityPlayer
import java.util
import net.minecraftforge.common.ForgeDirection
import net.minecraft.world.World
import net.bdew.deepcore.config.Tuning

class Canister(id: Int) extends SimpleItem(id, "Canister") with IFluidContainerItem {
  lazy val cfg = Tuning.getSection("Items").getSection(name)
  lazy val maxPour = cfg.getInt("MaxPour")
  lazy val capacity = cfg.getInt("Capacity")

  setMaxStackSize(1)

  def getFluid(stack: ItemStack): FluidStack = FluidStack.loadFluidStackFromNBT(stack.getTagCompound)

  def drain(stack: ItemStack, max: Int, doDrain: Boolean): FluidStack = {
    val fl = getFluid(stack)
    if (fl == null) return null
    val ns = new FluidStack(fl.fluidID, Misc.clamp(fl.amount, 0, max))
    if (doDrain) {
      fl.amount -= ns.amount
      val nbt = new NBTTagCompound()
      if (fl.amount > 0)
        fl.writeToNBT(nbt)
      stack.setTagCompound(nbt)
    }
    return ns
  }

  def fill(stack: ItemStack, fl: FluidStack, doFill: Boolean): Int = {
    val currStack = getFluid(stack)
    if (fl == null || (currStack != null && !currStack.isFluidEqual(fl))) return 0
    val current = if (currStack == null) 0 else currStack.amount
    val toFill = Misc.clamp(fl.amount, 0, capacity - current)
    if (doFill) {
      val newStack = new FluidStack(fl.getFluid, toFill + current)
      val nbt = new NBTTagCompound()
      newStack.writeToNBT(nbt)
      stack.setTagCompound(nbt)
    }
    return toFill
  }

  def getCapacity(container: ItemStack): Int = capacity

  override def addInformation(stack: ItemStack, player: EntityPlayer, lst: util.List[_], par4: Boolean) {
    import scala.collection.JavaConverters._
    val l = lst.asInstanceOf[util.List[String]].asScala
    val fl = getFluid(stack)
    if (fl == null)
      l += Misc.toLocal("deepcore.label.empty")
    else
      l += "%d/%d %s".format(fl.amount, capacity, fl.getFluid.getLocalizedName)
  }

  override def onItemUseFirst(stack: ItemStack, player: EntityPlayer, world: World, x: Int, y: Int, z: Int, side: Int, hitX: Float, hitY: Float, hitZ: Float): Boolean = {
    val te = world.getBlockTileEntity(x, y, z)
    if (te != null && te.isInstanceOf[IFluidHandler]) {
      val fh = te.asInstanceOf[IFluidHandler]
      val fl = drain(stack, maxPour, false)
      if (fl == null) return false
      val dir = ForgeDirection.values()(side).getOpposite
      val tofill = fh.fill(dir, fl, false)
      if (tofill > 0) {
        fh.fill(dir, drain(stack, tofill, true), true)
        player.swingItem()
        return !world.isRemote
      }
    }
    return false
  }
}
