/*
 * Copyright (c) bdew, 2014
 * https://github.com/bdew/deepcore
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * https://raw.github.com/bdew/deepcore/master/MMPL-1.0.txt
 */

package net.bdew.deepcore.items.scanner

import java.util

import cpw.mods.fml.relauncher.{Side, SideOnly}
import net.bdew.deepcore.Deepcore
import net.bdew.deepcore.config.{Config, Items, Tuning}
import net.bdew.deepcore.items.ScannerReport
import net.bdew.deepcore.items.scanner.overlay.ScannerOverlay
import net.bdew.deepcore.network.{MsgScannerUpdate, NetHandler}
import net.bdew.deepcore.overlay.ItemWithOverlay
import net.bdew.deepcore.resources.ResourceManager
import net.bdew.lib.Misc
import net.bdew.lib.gui.GuiProvider
import net.bdew.lib.items.inventory.{InventoryItemAdapter, ItemInventory}
import net.bdew.lib.items.{ItemUtils, SimpleItem}
import net.minecraft.entity.Entity
import net.minecraft.entity.player.{EntityPlayer, EntityPlayerMP}
import net.minecraft.item.ItemStack
import net.minecraft.util.{ChatComponentTranslation, ChatStyle, EnumChatFormatting}
import net.minecraft.world.World

object Scanner extends SimpleItem("Scanner") with ItemWithOverlay with GuiProvider with ItemInventory {
  lazy val cfg = Tuning.getSection("Items").getSection(name)
  lazy val radius = cfg.getInt("Radius")

  def guiId = 2
  val invSize = 18
  val invTagName = "Items"

  PlayerChunkTracker.init()
  Config.guiHandler.register(this)

  @SideOnly(Side.CLIENT)
  def makeGui(inv: InventoryItemAdapter, player: EntityPlayer) = new GuiScanner(inv, player)
  def makeContainer(inv: InventoryItemAdapter, player: EntityPlayer) = new ContainerScanner(inv, player)
  override def makeAdaptor(player: EntityPlayer) = new InventoryScanner(player, player.inventory.currentItem, invSize, invTagName)

  @SideOnly(Side.CLIENT)
  def getOverlay(stack: ItemStack) = ScannerOverlay

  def switchModule(stack: ItemStack, dir: Int, player: EntityPlayer) {
    if (!stack.hasTagCompound) return
    val mods = getModules(stack).size
    if (mods > 0) {
      val tag = stack.getTagCompound
      val v = ((tag.getByte("activeModule") + dir) % mods + mods) % mods
      tag.setByte("activeModule", v.toByte)
      PlayerChunkTracker.reset(player)
    }
  }

  def getModules(stack: ItemStack) = {
    if (stack.hasTagCompound)
      Misc.iterNbtCompoundList(stack.getTagCompound, invTagName)
        .map(x => ItemStack.loadItemStackFromNBT(x))
        .filter(x => x.getItem == Items.scannerModule && ResourceManager.isValid(x.getItemDamage))
        .map(x => ResourceManager.byId.get(x.getItemDamage))
        .flatten.toList
    else
      List.empty
  }

  override def addInformation(stack: ItemStack, player: EntityPlayer, lst: util.List[_], par4: Boolean) {
    val l = lst.asInstanceOf[util.List[String]]
    getModules(stack).foreach(x => l.add("* " + x.getLocalizedName))
  }

  def getActiveModule(stack: ItemStack) = {
    val mods = getModules(stack)
    if (mods.isEmpty)
      None
    else
      mods.lift(stack.getTagCompound.getByte("activeModule") % mods.size)
  }

  override def onItemRightClick(stack: ItemStack, world: World, player: EntityPlayer): ItemStack = {
    if (world.isRemote) return stack
    if (player.isSneaking) {
      for (res <- getActiveModule(stack);
           slot <- ItemUtils.findItemInInventory(player.inventory, Items.scannerReportBlank)) {
        player.inventory.decrStackSize(slot, 1)
        player.inventory.markDirty()
        val chunkX = player.posX.floor.toInt >> 4
        val chunkY = player.posZ.floor.toInt >> 4
        val v = res.map.getValue(chunkX, chunkY, world.getSeed, player.dimension)
        if (v > 0) {
          val newStack = ScannerReport.newStack(
            x = chunkX,
            y = chunkY,
            dim = player.dimension,
            resource = res,
            depth = res.depthFromVal(v),
            abundance = res.abundanceFromVal(v)
          )
          ItemUtils.dropItemToPlayer(world, player, newStack)
        } else player.addChatMessage(
          new ChatComponentTranslation("deepcore.message.scanner.noresource", "deepcore.resource." + res.name)
            .setChatStyle(new ChatStyle().setColor(EnumChatFormatting.RED))
        )
      }
    } else {
      doOpenGui(player, world)
    }
    return stack
  }

  override def onUpdate(stack: ItemStack, world: World, pentity: Entity, slot: Int, active: Boolean) {
    if (!active || world.isRemote || !pentity.isInstanceOf[EntityPlayerMP]) return
    val player = pentity.asInstanceOf[EntityPlayerMP]
    val chunkX = pentity.posX.floor.toInt >> 4
    val chunkY = pentity.posZ.floor.toInt >> 4
    val activeMod = getActiveModule(stack).getOrElse({
      if (PlayerChunkTracker.update(player, chunkX, chunkY, -1)) {
        NetHandler.sendTo(MsgScannerUpdate(chunkX, chunkY, 0, -1, null), player)
      }
      return
    })
    if (PlayerChunkTracker.update(player, chunkX, chunkY, activeMod.id)) {
      val map = for (x <- -radius to radius; y <- -radius to radius)
      yield activeMod.map.getValue(chunkX + x, chunkY + y, world.getSeed, world.provider.dimensionId)

      NetHandler.sendTo(MsgScannerUpdate(chunkX, chunkY, radius, activeMod.id, map.toArray), player)

      Deepcore.logInfo("Player %s moved to (%d,%d)", player.getDisplayName, chunkX, chunkY)
    }
  }
}
