package mrriegel.particlespreader.item;

import mrriegel.limelib.LimeLib;
import mrriegel.limelib.datapart.DataPart;
import mrriegel.limelib.datapart.DataPartRegistry;
import mrriegel.limelib.helper.ColorHelper;
import mrriegel.limelib.helper.ParticleHelper;
import mrriegel.limelib.item.CommonItem;
import mrriegel.limelib.particle.CommonParticle;
import mrriegel.limelib.util.Utils;
import mrriegel.particlespreader.ParticleSpreader;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumFacing.Axis;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.items.ItemHandlerHelper;

public class ItemSpreader extends CommonItem {

	public ItemSpreader() {
		super("spreader");
		setCreativeTab(CreativeTabs.DECORATIONS);
		setMaxStackSize(1);
	}

	@Override
	public void registerItem() {
		super.registerItem();
		DataPartRegistry.register("particlePart", ParticlePart.class);
	}

	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		ItemStack itemstack = player.getHeldItem(hand);
		if (!itemstack.isEmpty() && player.canPlayerEdit(pos, facing, itemstack)) {
			ParticlePart part = new ParticlePart();
			if (DataPartRegistry.get(worldIn).addDataPart(pos.offset(facing), part, false)) {
				player.setHeldItem(hand, ItemHandlerHelper.copyStackWithSize(itemstack, itemstack.getCount() - 1));
				return EnumActionResult.SUCCESS;
			} else {
				return EnumActionResult.FAIL;
			}
		} else {
			return EnumActionResult.FAIL;
		}
	}

	public static enum ParticleVariant {
		NORMAL, CIRCLE, SPIRALE, EXPLOSION;
		private static ParticleVariant[] vals = values();

		public ParticleVariant next() {
			return vals[(this.ordinal() + 1) % vals.length];
		}
	}

	public static enum Redstone {
		ALWAYS, NEVER, ON, OFF;
		private static Redstone[] vals = values();

		public Redstone next() {
			return vals[(this.ordinal() + 1) % vals.length];
		}
	}

	public static class ParticlePart extends DataPart {

		public double minXPos = .4, maxXPos = .6, minYPos = .4, maxYPos = .6, minZPos = .4, maxZPos = .6;
		public double minXMotion = 0., maxXMotion = 0., minYMotion = .03, maxYMotion = .03, minZMotion = 0., maxZMotion = 0.;
		public double minScale = 1., maxScale = 2., flouncing = .06, frequence = 2., radius = 2.5, force = .1, spinSpeed = .01;
		public float gravity = 0F;
		public int rate = 4, color = 0xFFFFFFFF, brightness = -1, visibleRange = 32, rainbow = -1;
		public int minAge = 40, maxAge = 80, colorDiff = 0, alpha = 255;
		private int texture = 0;
		public boolean depth = true, collidable = false, reverse = false;
		public ParticleVariant variant = ParticleVariant.NORMAL;
		public Redstone red = Redstone.ALWAYS;
		public Axis ax = Axis.Y;

		public static String[] textures = new String[] { "round", "sparkle", "square" };

		public void nextTexture() {
			texture = (texture + 1) % textures.length;
		}

		@Override
		public void onRightClicked(EntityPlayer player, EnumHand hand) {
			player.openGui(ParticleSpreader.instance, 0, world, getX(), getY(), getZ());
		}

		@Override
		public void onLeftClicked(EntityPlayer player, EnumHand hand) {
			if (!world.isRemote) {
				EntityItem ei = new EntityItem(world, getX() + .5, getY() + .5, getZ() + .5, new ItemStack(ParticleSpreader.spreader));
				ei.setPositionAndUpdate(player.posX, player.posY + .3, player.posZ);
				getRegistry().removeDataPart(pos);
			}
		}

		private boolean redstoneValid() {
			switch (red) {
			case ALWAYS:
				return true;
			case NEVER:
				return false;
			case ON:
				return world.isBlockPowered(pos);
			case OFF:
				return !world.isBlockPowered(pos);
			default:
				return false;
			}

		}

		@Override
		public void updateClient(World world) {
			if (ticksExisted % rate != 0 || !redstoneValid())
				return;
			switch (variant) {
			case NORMAL: {
				CommonParticle par = new CommonParticle(posX(), posY(), posZ(), motionX(), motionY(), motionZ());
				applyValues(par);
				LimeLib.proxy.renderParticle(par);
				break;
			}
			case CIRCLE: {
				for (Vec3d vec : ParticleHelper.getVecsForCircle(posX(), posY(), posZ(), radius, frequence, Axis.Y)) {
					CommonParticle par = new CommonParticle(vec.xCoord, vec.yCoord, vec.zCoord, Utils.getRandomNumber(minXMotion, maxXMotion), Utils.getRandomNumber(minYMotion, maxYMotion), Utils.getRandomNumber(minZMotion, maxZMotion));
					applyValues(par);
					LimeLib.proxy.renderParticle(par);
				}
				break;
			}
			case SPIRALE: {
				Vec3d vec = ParticleHelper.getVecForSpirale(force, spinSpeed, frequence + 40., reverse, ax);
				CommonParticle par = new CommonParticle(posX(), posY(), posZ(), ax == Axis.X ? motionX() : vec.xCoord, ax == Axis.Y ? motionY() : vec.yCoord, ax == Axis.Z ? motionZ() : vec.zCoord);
				applyValues(par);
				LimeLib.proxy.renderParticle(par);
				break;
			}
			case EXPLOSION: {
				for (Vec3d vec : ParticleHelper.getVecsForExplosion(force, frequence + 20., ax)) {
					CommonParticle par = new CommonParticle(posX(), posY(), posZ(), ax == Axis.X ? motionX() : vec.xCoord, ax == Axis.Y ? motionY() : vec.yCoord, ax == Axis.Z ? motionZ() : vec.zCoord);
					applyValues(par);
					LimeLib.proxy.renderParticle(par);
				}
				break;
			}
			}
		}

		//@formatter:off
		double posX() {return getX() + Utils.getRandomNumber(minXPos, maxXPos);}
		double posY() {return getY() + Utils.getRandomNumber(minYPos, maxYPos);}
		double posZ() {return getZ() + Utils.getRandomNumber(minZPos, maxZPos);}
		double motionX() {return Utils.getRandomNumber(minXMotion, maxXMotion);}
		double motionY() {return Utils.getRandomNumber(minYMotion, maxYMotion);}
		double motionZ() {return Utils.getRandomNumber(minZMotion, maxZMotion);}
		//@formatter:on

		private void applyValues(CommonParticle par) {
			par.setScale((float) Utils.getRandomNumber(minScale, maxScale));
			int col = ColorHelper.getRGB(rainbow > 0 ? ColorHelper.getRainbow(rainbow) : color, alpha);
			par.setColor(col, colorDiff);
			par.setFlouncing(flouncing / 10.);
			par.setBrightness(brightness);
			par.setDepth(depth);
			par.setSmoothEnd(true);
			par.setVisibleRange(visibleRange);
			par.setMaxAge2(Utils.getRandomNumber(minAge, maxAge));
			if (textures[texture].equalsIgnoreCase("round"))
				par.setTexture(ParticleHelper.roundParticle);
			else if (textures[texture].equalsIgnoreCase("sparkle"))
				par.setTexture(ParticleHelper.sparkleParticle);
			else if (textures[texture].equalsIgnoreCase("square"))
				par.setTexture(ParticleHelper.squareParticle);
			par.setGravity(gravity);
			par.setNoClip(!collidable);
		}

		@Override
		public AxisAlignedBB getHighlightBox() {
			return new AxisAlignedBB(.3, .3, .3, .7, .7, .7);
		}
	}

}
