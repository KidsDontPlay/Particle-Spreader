package kdp.particlespreader;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import kdp.limelib.helper.nbt.NBTBuilder;

@Mod("particlespreader")
public class ParticleSpreader {

    public static final Logger LOG = LogManager.getLogger(ParticleSpreader.class);

    public ParticleSpreader() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::clientSetup);
        NBTBuilder.of();
    }

    private void setup(final FMLCommonSetupEvent event) {
    }

    private void clientSetup(final FMLClientSetupEvent event) {
    }
}
