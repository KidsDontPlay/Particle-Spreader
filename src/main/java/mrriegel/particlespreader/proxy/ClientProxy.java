package mrriegel.particlespreader.proxy;

import mrriegel.limelib.datapart.RenderRegistry;
import mrriegel.particlespreader.ParticleSpreader;
import mrriegel.particlespreader.item.ItemSpreader.ParticlePart;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class ClientProxy extends CommonProxy {

	@Override
	public void preInit(FMLPreInitializationEvent event) {
		super.preInit(event);
		ParticleSpreader.spreader.initModel();
	}

	@Override
	public void init(FMLInitializationEvent event) {
		super.init(event);
		MinecraftForge.EVENT_BUS.register(ClientProxy.class);
		RenderRegistry.register(ParticlePart.class, new RenderRegistry.RenderDataPart<ParticlePart>() {
			@Override
			public void render(ParticlePart part, double x, double y, double z, float partialTicks) {
				ItemStack inputStack = new ItemStack(Blocks.OBSIDIAN);
				Minecraft mc = Minecraft.getMinecraft();
				if (inputStack == null || inputStack.isEmpty())
					return;

				GlStateManager.pushMatrix();
				GlStateManager.translate(x, y, z);
				RenderItem itemRenderer = mc.getRenderItem();
				GlStateManager.translate(0.5, 0.5, 0.5);
				EntityItem entityitem = new EntityItem(part.getWorld(), 0.0D, 0.0D, 0.0D, inputStack);
				entityitem.hoverStart = 0.0F;
				GlStateManager.pushMatrix();
				GlStateManager.pushAttrib();
				GlStateManager.disableLighting();
				if (!mc.isGamePaused()) {
					float rotation = (float) (1720.0 * (System.currentTimeMillis() & 0x3FFFL) / 0x3FFFL);
					GlStateManager.rotate(rotation, 0.0F, 1.0F, 0);
				}
				GlStateManager.scale(0.5F, 0.5F, 0.5F);
				//				GlStateManager.pushAttrib();
				RenderHelper.enableStandardItemLighting();
				itemRenderer.renderItem(entityitem.getEntityItem(), ItemCameraTransforms.TransformType.FIXED);
				RenderHelper.disableStandardItemLighting();
				//				GlStateManager.popAttrib();
				GlStateManager.enableLighting();
				GlStateManager.popAttrib();
				GlStateManager.popMatrix();
				GlStateManager.popMatrix();
			}
		});
	}

	@Override
	public void postInit(FMLPostInitializationEvent event) {
		super.postInit(event);
	}

}
