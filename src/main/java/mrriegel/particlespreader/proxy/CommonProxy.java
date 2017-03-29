package mrriegel.particlespreader.proxy;

import mrriegel.limelib.network.PacketHandler;
import mrriegel.particlespreader.ParticleSpreader;
import mrriegel.particlespreader.handler.ConfigHandler;
import mrriegel.particlespreader.handler.GuiHandler;
import mrriegel.particlespreader.network.MessageToServer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.relauncher.Side;

public class CommonProxy {

	public void preInit(FMLPreInitializationEvent event) {
		ConfigHandler.refreshConfig(event.getSuggestedConfigurationFile());
		ParticleSpreader.spreader.registerItem();
	}

	public void init(FMLInitializationEvent event) {
		NetworkRegistry.INSTANCE.registerGuiHandler(ParticleSpreader.instance, new GuiHandler());
		MinecraftForge.EVENT_BUS.register(CommonProxy.class);
		PacketHandler.registerMessage(MessageToServer.class, Side.SERVER);
	}

	public void postInit(FMLPostInitializationEvent event) {
	}

}
