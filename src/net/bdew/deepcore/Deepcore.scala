/*
 * Copyright (c) bdew, 2014
 * https://github.com/bdew/deepcore
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * https://raw.github.com/bdew/deepcore/master/MMPL-1.0.txt
 */

package net.bdew.deepcore

import java.util.logging.Logger
import net.bdew.deepcore.config._
import cpw.mods.fml.common.Mod
import cpw.mods.fml.common.Mod.EventHandler
import cpw.mods.fml.common.event.FMLInitializationEvent
import cpw.mods.fml.common.event.FMLPostInitializationEvent
import cpw.mods.fml.common.event.FMLPreInitializationEvent
import cpw.mods.fml.common.network.NetworkMod
import cpw.mods.fml.common.network.NetworkRegistry
import java.io.File
import net.bdew.deepcore.world.ChunkDataManager
import net.minecraftforge.common.MinecraftForge
import net.bdew.deepcore.connected.IconCache
import cpw.mods.fml.relauncher.Side
import net.minecraftforge.client.MinecraftForgeClient
import net.bdew.deepcore.items.CanisterRenderer
import net.bdew.deepcore.network.{ServerPacketHandler, ClientPacketHandler}

@Mod(modid = Deepcore.modId, version = "DEEPCORE_VER", name = "Deep Core Mining", dependencies = "after:BuildCraft|energy;after:BuildCraft|Silicon;after:IC2;after:CoFHCore;required-after:bdlib", modLanguage = "scala")
@NetworkMod(clientSideRequired = true, serverSideRequired = false)
object Deepcore {
  var log: Logger = null
  var instance = this

  final val modId = "deepcore"
  final val channel = "bdew.deepcore"

  var configDir: File = null

  def logInfo(msg: String, args: Any*) = log.info(msg.format(args: _*))
  def logWarn(msg: String, args: Any*) = log.warning(msg.format(args: _*))

  @EventHandler
  def preInit(event: FMLPreInitializationEvent) {
    log = event.getModLog
    configDir = event.getModConfigurationDirectory
    TuningLoader.load("config")
    TuningLoader.load("override", false)
    Config.load(event.getSuggestedConfigurationFile)
    if (event.getSide == Side.CLIENT) {
      MinecraftForge.EVENT_BUS.register(IconCache)
      MinecraftForgeClient.registerItemRenderer(Items.canister.itemID, new CanisterRenderer)
      NetworkRegistry.instance().registerChannel(new ClientPacketHandler, channel, Side.CLIENT)
    }
    NetworkRegistry.instance().registerChannel(new ServerPacketHandler, channel, Side.SERVER)
  }

  @EventHandler
  def init(event: FMLInitializationEvent) {
    NetworkRegistry.instance.registerGuiHandler(this, Config.guiHandler)
    ChunkDataManager.init()
    TuningLoader.loadDealayed()
  }

  @EventHandler
  def postInit(event: FMLPostInitializationEvent) {
  }
}