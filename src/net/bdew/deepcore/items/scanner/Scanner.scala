/*
 * Copyright (c) bdew, 2014
 * https://github.com/bdew/deepcore
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * https://raw.github.com/bdew/deepcore/master/MMPL-1.0.txt
 */

package net.bdew.deepcore.items.scanner

import net.minecraft.item.ItemStack
import net.minecraft.entity.player.{EntityPlayerMP, EntityPlayer}
import net.minecraft.world.World
import net.bdew.lib.items.SimpleItem
import net.bdew.deepcore.overlay.ItemWithOverlay
import cpw.mods.fml.relauncher.{Side, SideOnly}
import net.minecraft.entity.Entity
import net.bdew.deepcore.Deepcore
import net.bdew.deepcore.config.{Items, Config, Tuning}
import net.bdew.deepcore.network.{Packets, PktWriter}
import net.bdew.lib.gui.GuiProvider
import java.util
import net.bdew.lib.Misc
import net.minecraft.nbt.NBTTagCompound
import net.bdew.lib.items.inventory.{InventoryItemAdapter, ItemInventory}
import net.bdew.deepcore.resources.{Resource, ResourceManager}
import net.bdew.deepcore.items.scanner.overlay.ScannerOverlay

class Scanner(id: Int) extends SimpleItem(id, "Scanner") with ItemWithOverlay with GuiProvider with ItemInventory {
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

  def getModules(stack: ItemStack): List[Resource] = {
    if (!stack.hasTagCompound) return List.empty
    Misc.iterNbtList[NBTTagCompound](stack.getTagCompound.getTagList(invTagName))
      .map(x => ItemStack.loadItemStackFromNBT(x))
      .filter(x => x.getItem == Items.scannerModule && ResourceManager.isValid(x.getItemDamage))
      .map(x => ResourceManager.byId.get(x.getItemDamage))
      .flatten.toList
  }

  override def addInformation(stack: ItemStack, player: EntityPlayer, lst: util.List[_], par4: Boolean) {
    val l = lst.asInstanceOf[util.List[String]]
    getModules(stack).foreach(x => l.add("* " + x.getLocalizedName))
  }

  override def onItemRightClick(stack: ItemStack, world: World, player: EntityPlayer) = {
    if (!world.isRemote && !player.isSneaking)
      doOpenGui(player, world)
    stack
  }

  override def onUpdate(stack: ItemStack, world: World, pentity: Entity, slot: Int, active: Boolean) {
    if (!active || world.isRemote || !pentity.isInstanceOf[EntityPlayerMP]) return
    val player = pentity.asInstanceOf[EntityPlayerMP]
    val chunkX = pentity.posX.floor.toInt >> 4
    val chunkY = pentity.posZ.floor.toInt >> 4
    val mods = getModules(stack)
    val tag = stack.getTagCompound
    if (mods.size == 0 || !mods.isDefinedAt(tag.getByte("activeModule") % mods.size)) {
      if (PlayerChunkTracker.update(player, chunkX, chunkY, -1)) {
        val pkt = new PktWriter(Packets.SCANNER_UPDATE)
        pkt.writeInt(0)
        pkt.writeInt(chunkX)
        pkt.writeInt(chunkY)
        pkt.writeInt(-1)
        pkt.sendToPlayer(player)
        pkt.writeFloat(0)
      }
      return
    }
    val activeMod = mods(tag.getByte("activeModule") % mods.size)
    if (PlayerChunkTracker.update(player, chunkX, chunkY, activeMod.id)) {
      val pkt = new PktWriter(Packets.SCANNER_UPDATE)
      pkt.writeInt(radius)
      pkt.writeInt(chunkX)
      pkt.writeInt(chunkY)
      pkt.writeInt(activeMod.id)
      for (x <- -radius to radius; y <- -radius to radius) {
        pkt.writeFloat(activeMod.map.getValue(chunkX + x, chunkY + y, world.getSeed, world.provider.dimensionId))
      }
      pkt.sendToPlayer(player)
      Deepcore.logInfo("Player %s moved to (%d,%d)", player.getDisplayName, chunkX, chunkY)
    }
  }
}
