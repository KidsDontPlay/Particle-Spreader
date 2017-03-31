package mrriegel.particlespreader;

import mrriegel.limelib.item.CommonItem;
import mrriegel.particlespreader.item.ItemSpreader;
import mrriegel.particlespreader.proxy.CommonProxy;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = ParticleSpreader.MODID, name = ParticleSpreader.MODNAME, version = ParticleSpreader.VERSION, dependencies = "required-after:limelib@[1.5.2,)")
public class ParticleSpreader {
	public static final String MODID = "particlespreader";
	public static final String VERSION = "1.0.0";
	public static final String MODNAME = "Particle Spreader";

	@Instance(ParticleSpreader.MODID)
	public static ParticleSpreader instance;

	@SidedProxy(clientSide = "mrriegel.particlespreader.proxy.ClientProxy", serverSide = "mrriegel.particlespreader.proxy.CommonProxy")
	public static CommonProxy proxy;

	public static final CommonItem spreader = new ItemSpreader();

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		proxy.preInit(event);
	}

	@EventHandler
	public void init(FMLInitializationEvent event) {
		proxy.init(event);
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		proxy.postInit(event);
	}

}
