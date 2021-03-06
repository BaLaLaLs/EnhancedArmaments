package com.kenymylankca.enhancedarmaments;

import java.io.File;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.kenymylankca.enhancedarmaments.commands.WPCommandExpLevel;
import com.kenymylankca.enhancedarmaments.commands.WPCommandRarity;
import com.kenymylankca.enhancedarmaments.config.Config;
import com.kenymylankca.enhancedarmaments.init.ModEvents;
import com.kenymylankca.enhancedarmaments.network.PacketGuiAbility;
import com.kenymylankca.enhancedarmaments.proxies.CommonProxy;
import com.kenymylankca.enhancedarmaments.util.GuiHandler;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

/**
 * A simple Minecraft mod focused on the aspect of leveling certain areas
 * of the game. On top of that, other interesting leveling systems are
 * planned to enhance the overall feel of Minecraft.
 */
@Mod(modid = EnhancedArmaments.MODID, name = EnhancedArmaments.NAME, version = EnhancedArmaments.VERSION)
public class EnhancedArmaments 
{
	public static final String MODID = "enhancedarmaments";
	public static final String NAME = "Enhanced Armaments";
	public static final String VERSION = "1.3.18";
	public static final String COMMON = "com.kenymylankca.enhancedarmaments.proxies.CommonProxy";
	public static final String CLIENT = "com.kenymylankca.enhancedarmaments.proxies.ClientProxy";
	
	@SidedProxy(clientSide = EnhancedArmaments.CLIENT, serverSide = EnhancedArmaments.COMMON)
	public static CommonProxy proxy;
	@Instance(EnhancedArmaments.MODID)
	public static EnhancedArmaments instance;
	public static final Logger LOGGER = LogManager.getLogger("EnhancedArmaments");
	public static SimpleNetworkWrapper network;
	private static File configDir;
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent event)
	{
		configDir = new File(event.getModConfigurationDirectory() + "/" + EnhancedArmaments.NAME);
		configDir.mkdirs();
		Config.init(configDir);
		
		ModEvents.registerEvents();
		proxy.preInit();
		
		network = NetworkRegistry.INSTANCE.newSimpleChannel("enhancedarmaments");
		network.registerMessage(PacketGuiAbility.Handler.class, PacketGuiAbility.class, 0, Side.SERVER);
	}
	
	@EventHandler
	public void init(FMLInitializationEvent event)
	{
		proxy.init();
		NetworkRegistry.INSTANCE.registerGuiHandler(instance, new GuiHandler());
	}
	
	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {}
	
	public static File getConfigDir()
	{
		return configDir;
	}
	
	@EventHandler
	public static void serverInit(FMLServerStartingEvent event)
	{
		event.registerServerCommand(new WPCommandRarity());
		event.registerServerCommand(new WPCommandExpLevel());
	}
}