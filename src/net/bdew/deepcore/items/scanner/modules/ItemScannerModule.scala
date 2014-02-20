/*
 * Copyright (c) bdew, 2013 - 2014
 * https://github.com/bdew/gendustry
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * https://raw.github.com/bdew/gendustry/master/MMPL-1.0.txt
 */

package net.bdew.deepcore.items.scanner.modules

import net.minecraft.item.{ItemStack, Item}
import net.minecraft.util.Icon
import net.minecraft.creativetab.CreativeTabs
import java.util
import cpw.mods.fml.common.registry.GameRegistry
import net.bdew.deepcore.Deepcore
import net.bdew.deepcore.resources.{IconLoader, ResourceManager}

class ItemScannerModule(id: Int) extends Item(id) {
  setHasSubtypes(true)
  setMaxDamage(-1)
  setUnlocalizedName(Deepcore.modId + ".Scanner.Module")

  for (res <- ResourceManager.list)
    GameRegistry.registerCustomItemStack("Scanner.Module." + res.name, new ItemStack(this, 1, res.id))

  override def getIconFromDamage(meta: Int): Icon = ResourceManager.byId.getOrElse(meta, return IconLoader.invalid).moduleIcon

  override def getUnlocalizedName(stack: ItemStack): String =
    Deepcore.modId + ".Scanner.Module." + getResName(stack.getItemDamage)

  def getResName(id: Int): String = ResourceManager.byId.getOrElse(id, return "invalid").name

  override def getSubItems(par1: Int, par2CreativeTabs: CreativeTabs, list: util.List[_]) {
    val l = list.asInstanceOf[util.List[ItemStack]]
    for (res <- ResourceManager.list)
      l.add(new ItemStack(this, 1, res.id))
  }
}
