package mrriegel.particlespreader.proxy;

import java.awt.Color;
import java.util.Random;

import mrriegel.limelib.datapart.RenderRegistry;
import mrriegel.limelib.helper.ColorHelper;
import mrriegel.limelib.particle.CommonParticle;
import mrriegel.particlespreader.ParticlePart;
import mrriegel.particlespreader.ParticleSpreader;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
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
		Minecraft.getMinecraft().getItemColors().registerItemColorHandler((ItemStack stack, int tint) -> {
			if (!stack.hasTagCompound())
				return ColorHelper.getRainbow(25);
			if ("".isEmpty()) {
				return stack.getTagCompound().getCompoundTag("partdata_" + stack.getTagCompound().getInteger("active")).getInteger("color");
				//				return stack.getTagCompound().getInteger("color");
			}
			ParticlePart part = new ParticlePart();
			part.readFromNBT(stack.getTagCompound());
			CommonParticle par = new CommonParticle(0, 0, 0);
			part.applyValues(par, part.parDatas.get(part.activeParticle));
			return new Color(par.getRedColorF(), par.getGreenColorF(), par.getBlueColorF()).getRGB();
		}, ParticleSpreader.spreader);
		if (!"".isEmpty())
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
					itemRenderer.renderItem(entityitem.getItem(), ItemCameraTransforms.TransformType.FIXED);
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

	@Override
	public void highlightPart(BlockPos pos) {
		if (GuiScreen.isShiftKeyDown() && GuiScreen.isCtrlKeyDown()) {
			Random random = new Random();
			Minecraft.getMinecraft().world.spawnParticle(EnumParticleTypes.VILLAGER_HAPPY, MathHelper.nextDouble(random, 0, 1) + pos.getX(), MathHelper.nextDouble(random, 0, 1) + pos.getY(), MathHelper.nextDouble(random, 0, 1) + pos.getZ(), 0, 0, 0);
		}
	}

}
