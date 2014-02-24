/*
 * Copyright (c) bdew, 2013 - 2014
 * https://github.com/bdew/deepcore
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * https://raw.github.com/bdew/deepcore/master/MMPL-1.0.txt
 */

package net.bdew.deepcore.items.scanner.overlay

import net.bdew.lib.gui.{Point, Rect}
import net.bdew.lib.gui.widgets.Widget
import net.minecraft.client.renderer.texture.TextureMap
import net.bdew.deepcore.resources.ResourceManager

class WidgetResIcon(val rect: Rect) extends Widget {
  override def draw(mouse: Point) {
    bindTexture(TextureMap.locationItemsTexture)
    if (ResourceManager.isValid(ScannerOverlay.resId)) {
      parent.drawIcon(rect, ResourceManager.byId(ScannerOverlay.resId).resIcon)
    }
  }
}
