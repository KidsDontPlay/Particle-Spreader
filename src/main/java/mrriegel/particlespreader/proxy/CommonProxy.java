package mrriegel.particlespreader.proxy;

import mrriegel.limelib.network.PacketHandler;
import mrriegel.particlespreader.ParticleSpreader;
import mrriegel.particlespreader.handler.ConfigHandler;
import mrriegel.particlespreader.handler.GuiHandler;
import mrriegel.particlespreader.network.MessageToServer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.oredict.ShapedOreRecipe;

public class CommonProxy {

	public void preInit(FMLPreInitializationEvent event) {
		ConfigHandler.refreshConfig(event.getSuggestedConfigurationFile());
		ParticleSpreader.spreader.registerItem();
		GameRegistry.addRecipe(new ShapedOreRecipe(ParticleSpreader.spreader, "gdg", "dld", "gdg", 'g', "blockGlass", 'd', "dye", 'l', "dustGlowstone"));
		GameRegistry.addShapelessRecipe(new ItemStack(ParticleSpreader.spreader), ParticleSpreader.spreader);
	}

	public void init(FMLInitializationEvent event) {
		NetworkRegistry.INSTANCE.registerGuiHandler(ParticleSpreader.instance, new GuiHandler());
		MinecraftForge.EVENT_BUS.register(CommonProxy.class);
		PacketHandler.registerMessage(MessageToServer.class, Side.SERVER);
	}

	public void postInit(FMLPostInitializationEvent event) {
	}

	public void highlightPart(BlockPos pos) {
	}

}
