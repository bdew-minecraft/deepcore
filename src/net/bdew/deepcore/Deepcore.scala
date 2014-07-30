/*
 * Copyright (c) bdew, 2014
 * https://github.com/bdew/deepcore
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * https://raw.github.com/bdew/deepcore/master/MMPL-1.0.txt
 */

package net.bdew.deepcore

import java.io.File

import cpw.mods.fml.common.Mod
import cpw.mods.fml.common.Mod.EventHandler
import cpw.mods.fml.common.event.{FMLInitializationEvent, FMLPostInitializationEvent, FMLPreInitializationEvent, FMLServerStartingEvent}
import cpw.mods.fml.common.network.NetworkRegistry
import cpw.mods.fml.relauncher.Side
import net.bdew.deepcore.compat.PowerProxy
import net.bdew.deepcore.config._
import net.bdew.deepcore.items.scanner.overlay.ScannerMouseEventHandler
import net.bdew.deepcore.items.{Canister, CanisterRenderer}
import net.bdew.deepcore.network.NetHandler
import net.bdew.deepcore.overlay.OverlayRenderHandler
import net.bdew.lib.Event
import net.minecraftforge.client.MinecraftForgeClient
import org.apache.logging.log4j.Logger

@Mod(modid = Deepcore.modId, version = "DEEPCORE_VER", name = "Deep Core Mining", dependencies = "after:BuildCraft|energy;after:BuildCraft|Silicon;after:IC2;after:CoFHCore;after:ThermalExpansion;required-after:bdlib", modLanguage = "scala")
object Deepcore {
  var log: Logger = null
  var instance = this

  final val modId = "deepcore"
  final val channel = "bdew.deepcore"

  var configDir: File = null

  def logInfo(msg: String, args: Any*) = log.info(msg.format(args: _*))
  def logWarn(msg: String, args: Any*) = log.warn(msg.format(args: _*))

  @EventHandler
  def preInit(event: FMLPreInitializationEvent) {
    log = event.getModLog
    configDir = event.getModConfigurationDirectory
    PowerProxy.logModVersions()
    TuningLoader.load("config")
    TuningLoader.load("resources")
    TuningLoader.load("override", false)
    Config.load()
    if (event.getSide == Side.CLIENT) {
      connected.IconCache.init()
      resources.IconLoader.init()
      MinecraftForgeClient.registerItemRenderer(Canister, CanisterRenderer)
      OverlayRenderHandler.init()
      ScannerMouseEventHandler.init()
    }
    NetHandler.init()
  }

  @EventHandler
  def init(event: FMLInitializationEvent) {
    NetworkRegistry.INSTANCE.registerGuiHandler(this, Config.guiHandler)
    //ChunkDataManager.init()
    TuningLoader.loadDealayed()
  }

  @EventHandler
  def postInit(event: FMLPostInitializationEvent) {
  }

  val serverStarting = Event[FMLServerStartingEvent]
  @EventHandler
  def onServerStarting(ev: FMLServerStartingEvent) = serverStarting.trigger(ev)
}