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
import net.minecraft.creativetab.CreativeTabs
import java.util
import cpw.mods.fml.common.registry.GameRegistry
import net.bdew.deepcore.Deepcore
import net.bdew.deepcore.resources.{IconLoader, ResourceManager}
import net.bdew.lib.Misc

object ItemScannerModule extends Item {
  setHasSubtypes(true)
  setMaxDamage(-1)
  setUnlocalizedName(Deepcore.modId + ".Scanner.Module")

  for (res <- ResourceManager.list if ResourceManager.isValid(res.id))
    GameRegistry.registerCustomItemStack("Scanner.Module." + res.name, new ItemStack(this, 1, res.id))

  override def getIconFromDamage(meta: Int) =
    if (ResourceManager.isValid(meta))
      ResourceManager.byId(meta).moduleIcon
    else
      IconLoader.invalid

  override def getItemDisplayName(stack: ItemStack) =
    if (ResourceManager.isValid(stack.getItemDamage))
      Misc.toLocal("item.deepcore.ScannerModule.name") + ": " + ResourceManager.byId(stack.getItemDamage).getLocalizedName
    else
      Misc.toLocal("deepcore.resource.invalid")

  override def getSubItems(par1: Item, par2CreativeTabs: CreativeTabs, list: util.List[_]) {
    val l = list.asInstanceOf[util.List[ItemStack]]
    for (res <- ResourceManager.list if ResourceManager.isValid(res.id))
      l.add(new ItemStack(this, 1, res.id))
  }
}
