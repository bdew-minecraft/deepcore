/*
 * Copyright (c) bdew, 2014
 * https://github.com/bdew/deepcore
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * https://raw.github.com/bdew/deepcore/master/MMPL-1.0.txt
 */

package net.bdew.deepcore.items

import net.minecraft.item.ItemStack
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.world.World
import net.minecraft.block.Block
import net.bdew.lib.items.SimpleItem
import net.minecraftforge.fluids._
import net.minecraftforge.common.{MinecraftForge, ForgeDirection}
import net.minecraft.block.material.Material
import net.minecraft.inventory.IInventory
import net.minecraftforge.event.entity.player.PlayerInteractEvent
import net.minecraftforge.event.entity.player.PlayerInteractEvent.Action
import net.minecraftforge.event.ForgeSubscribe
import net.bdew.deepcore.config.Tuning

class HandPump(id: Int) extends SimpleItem(id, "HandPump") {
  lazy val cfg = Tuning.getSection("Items").getSection(name)
  lazy val maxDrain = cfg.getInt("MaxDrain")

  setMaxStackSize(1)
  MinecraftForge.EVENT_BUS.register(this)

  def getFluid(stack: ItemStack): FluidStack = FluidStack.loadFluidStackFromNBT(stack.getTagCompound)

  def findFillTarget(fs: FluidStack, inventory: IInventory, mustTakeAll: Boolean): ItemStack = {
    if (fs == null) return null
    for (i <- 0 until inventory.getSizeInventory) {
      val item = inventory.getStackInSlot(i)
      if (item != null && item.getItem != null && item.getItem.isInstanceOf[IFluidContainerItem]) {
        val fc = item.getItem.asInstanceOf[IFluidContainerItem]
        val canFill = fc.fill(item, fs, false)
        if ((mustTakeAll && canFill == fs.amount) || (!mustTakeAll && canFill > 0)) return item
      }
    }
    return null
  }

  def drainBlock(world: World, block: Block, x: Int, y: Int, z: Int, stack: ItemStack, dir: ForgeDirection, player: EntityPlayer): Boolean = {
    if (block.isInstanceOf[BlockFluidBase]) {
      val bl = block.asInstanceOf[BlockFluidBase]
      val fl = bl.drain(world, x, y, z, false)
      val tofill = findFillTarget(fl, player.inventory, true)
      if (tofill != null) {
        tofill.getItem.asInstanceOf[IFluidContainerItem].fill(tofill, bl.drain(world, x, y, z, true), true)
        return true
      }
    } else if (world.getBlockMaterial(x, y, z) == Material.water && world.getBlockMetadata(x, y, z) == 0) {
      val ns = new FluidStack(FluidRegistry.WATER, 1000)
      val tofill = findFillTarget(ns, player.inventory, true)
      if (tofill != null) {
        world.setBlockToAir(x, y, z)
        tofill.getItem.asInstanceOf[IFluidContainerItem].fill(tofill, ns, true)
        return true
      }
    } else if (world.getBlockMaterial(x, y, z) == Material.lava && world.getBlockMetadata(x, y, z) == 0) {
      val ns = new FluidStack(FluidRegistry.LAVA, 1000)
      val tofill = findFillTarget(ns, player.inventory, true)
      if (tofill != null) {
        world.setBlockToAir(x, y, z)
        tofill.getItem.asInstanceOf[IFluidContainerItem].fill(tofill, ns, true)
        return true
      }
    } else {
      val te = world.getBlockTileEntity(x, y, z)
      if (te != null && te.isInstanceOf[IFluidHandler]) {
        val fh = te.asInstanceOf[IFluidHandler]
        val fs = fh.drain(dir, maxDrain, false)
        val tofill = findFillTarget(fs, player.inventory, false)
        if (tofill != null) {
          val fci = tofill.getItem.asInstanceOf[IFluidContainerItem]
          val canFill = fci.fill(tofill, fs, false)
          if (canFill > 0) {
            fci.fill(tofill, fh.drain(dir, canFill, true), true)
            return true
          }
        }
      }
    }
    return false
  }

  override def onItemRightClick(stack: ItemStack, world: World, player: EntityPlayer): ItemStack = {
    if (player.isSneaking) return stack
    val mop = getMovingObjectPositionFromPlayer(world, player, true)
    if (mop == null) return stack

    val id = world.getBlockId(mop.blockX, mop.blockY, mop.blockZ)
    if (!Block.blocksList.isDefinedAt(id) || Block.blocksList(id) == null) return stack
    val block = Block.blocksList(id)

    if (drainBlock(world, block, mop.blockX, mop.blockY, mop.blockZ, stack, ForgeDirection.values()(mop.sideHit).getOpposite, player)) {
      player.swingItem()
      player.playSound("random.drink", 0.5F, world.rand.nextFloat * 0.1F + 0.9F)
      return stack
    }

    return stack
  }

  @ForgeSubscribe
  def onInteract(ev: PlayerInteractEvent) {
    val item = ev.entityPlayer.getHeldItem
    if (ev.action == Action.RIGHT_CLICK_BLOCK && item != null && item.getItem == this && !ev.entityPlayer.isSneaking) ev.setCanceled(true)
  }
}
