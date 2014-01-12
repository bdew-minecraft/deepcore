/*
 * Copyright (c) bdew, 2014
 * https://github.com/bdew/deepcore
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * https://raw.github.com/bdew/deepcore/master/MMPL-1.0.txt
 */

package net.bdew.deepcore.machines.compact

import net.minecraft.item.crafting.{ShapedRecipes, CraftingManager}

object RecipeCache {

  def init() {
    import scala.collection.JavaConversions._
    CraftingManager.getInstance.getRecipeList.collect({
      case recipe: ShapedRecipes =>
        for (x <- recipe.recipeItems) {
          x
        }
    })

  }

}
