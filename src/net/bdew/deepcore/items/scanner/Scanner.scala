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
import net.bdew.deepcore.config.{Config, Tuning}
import net.bdew.deepcore.network.{Packets, PktWriter}
import cpw.mods.fml.common.network.{Player, PacketDispatcher}
import net.bdew.lib.gui.GuiProvider
import java.util
import net.bdew.lib.Misc
import net.minecraft.nbt.NBTTagCompound
import net.bdew.lib.items.inventory.{InventoryItemAdapter, ItemInventory}
import net.bdew.deepcore.noise.NoiseHelper

class Scanner(id: Int) extends SimpleItem(id, "Scanner") with ItemWithOverlay with GuiProvider with ItemInventory {
  lazy val cfg = Tuning.getSection("Items").getSection(name)
  lazy val radius = cfg.getInt("Radius")

  lazy val testProfile = NoiseHelper.profileFromResource("Water", Tuning.getSection("Resources").getSection("Water"))

  def guiId = 2
  val invSize = 18
  val invTagName = "Items"

  PlayerChunkTracker.init()
  Config.guiHandler.register(this)

  @SideOnly(Side.CLIENT)
  def makeGui(inv: InventoryItemAdapter, player: EntityPlayer) = new GuiScanner(inv, player)
  def makeContainer(inv: InventoryItemAdapter, player: EntityPlayer) = new ContainerScanner(inv, player)

  @SideOnly(Side.CLIENT)
  def getOverlay(stack: ItemStack) = ScannerOverlay

  override def addInformation(stack: ItemStack, player: EntityPlayer, lst: util.List[_], par4: Boolean) {
    if (stack.hasTagCompound) {
      for (x <- Misc.iterNbtList[NBTTagCompound](stack.getTagCompound.getTagList("Items"))) {
        lst.asInstanceOf[util.List[String]].add(x.toString)
      }
    }
  }

  override def onItemRightClick(stack: ItemStack, world: World, player: EntityPlayer) = {
    if (!world.isRemote && !player.isSneaking)
      doOpenGui(player, world)
    stack
  }

  override def onUpdate(stack: ItemStack, world: World, player: Entity, slot: Int, active: Boolean) {
    if (!active || world.isRemote || !player.isInstanceOf[EntityPlayerMP]) return
    val pp = player.asInstanceOf[EntityPlayerMP]
    val cx = player.posX.floor.toInt >> 4
    val cy = player.posZ.floor.toInt >> 4
    if (PlayerChunkTracker.update(pp, cx, cy)) {
      val pkt = new PktWriter(Packets.SCANNER_UPDATE)
      pkt.writeInt(radius)
      pkt.writeInt(cx)
      pkt.writeInt(cy)
      for (x <- -radius to radius; y <- -radius to radius) {
        pkt.writeFloat(testProfile.genNoise(cx + x, cy + y))
      }
      PacketDispatcher.sendPacketToPlayer(pkt.toPacket, pp.asInstanceOf[Player])
      Deepcore.logInfo("Player %s moved to (%d,%d)", pp.getDisplayName, cx, cy)
    }
  }
}
