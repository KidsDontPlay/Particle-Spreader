package mrriegel.particlespreader;

import java.util.Random;

import mrriegel.limelib.LimeLib;
import mrriegel.limelib.datapart.DataPart;
import mrriegel.limelib.helper.ColorHelper;
import mrriegel.limelib.helper.NBTHelper;
import mrriegel.limelib.helper.ParticleHelper;
import mrriegel.limelib.particle.CommonParticle;
import mrriegel.particlespreader.item.ItemSpreader.ParticleVariant;
import mrriegel.particlespreader.item.ItemSpreader.Redstone;
import net.minecraft.block.Block;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumFacing.Axis;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class ParticlePart extends DataPart {

	public double minXPos = .4, maxXPos = .6, minYPos = .4, maxYPos = .6, minZPos = .4, maxZPos = .6;
	public double minXMotion = 0., maxXMotion = 0., minYMotion = .03, maxYMotion = .03, minZMotion = 0., maxZMotion = 0.;
	public double flouncing = .5, frequency = 2., radius = 2.5, spinSpeed = .1;
	public float gravity = 0F, minScale = 1F, maxScale = 2F;
	public int rate = 4, color = 0xFFFFFFFF, brightness = -1, visibleRange = 32, rainbow = -1;
	public int minAge = 40, maxAge = 80, colorDiff = 0, texture = 0;
	public boolean depth = true, collidable = true, reverse = false;
	public ParticleVariant variant = ParticleVariant.NORMAL;
	public Redstone red = Redstone.ALWAYS;
	public Axis ax = Axis.Y;

	public static String[] textures = new String[] { "round", "sparkle", "square" };
	private static Random random = new Random();

	public void nextTexture() {
		texture = (texture + 1) % textures.length;
	}

	public void correctValues() {
		maxXPos = MathHelper.clamp(maxXPos, minXPos, 133777.);
		maxYPos = MathHelper.clamp(maxYPos, minYPos, 133777.);
		maxZPos = MathHelper.clamp(maxZPos, minZPos, 133777.);
		maxXMotion = MathHelper.clamp(maxXMotion, minXMotion, 133777.);
		maxYMotion = MathHelper.clamp(maxYMotion, minYMotion, 133777.);
		maxZMotion = MathHelper.clamp(maxZMotion, minZMotion, 133777.);
		minScale = Math.max(minScale, 0.1F);
		maxScale = MathHelper.clamp(maxScale, minScale, 133777F);
		flouncing = Math.max(flouncing, 0.);
		frequency = Math.max(frequency, 0.);
		radius = Math.max(radius, 0.);
		spinSpeed = Math.max(spinSpeed, 0.);
		rate = Math.max(rate, 1);
		visibleRange = Math.max(visibleRange, 0);
		minAge = Math.max(minAge, 1);
		maxAge = MathHelper.clamp(maxAge, minAge, 133777);
		colorDiff = MathHelper.clamp(colorDiff, 0, 255);
	}

	@Override
	public void readFromNBT(NBTTagCompound compound) {
		minXPos = NBTHelper.getDouble(compound, "minXPos");
		minYPos = NBTHelper.getDouble(compound, "minYPos");
		minZPos = NBTHelper.getDouble(compound, "minZPos");
		maxXPos = NBTHelper.getDouble(compound, "maxXPos");
		maxYPos = NBTHelper.getDouble(compound, "maxYPos");
		maxZPos = NBTHelper.getDouble(compound, "maxZPos");
		minXMotion = NBTHelper.getDouble(compound, "minXMotion");
		minYMotion = NBTHelper.getDouble(compound, "minYMotion");
		minZMotion = NBTHelper.getDouble(compound, "minZMotion");
		maxXMotion = NBTHelper.getDouble(compound, "maxXMotion");
		maxYMotion = NBTHelper.getDouble(compound, "maxYMotion");
		maxZMotion = NBTHelper.getDouble(compound, "maxZMotion");
		minScale = NBTHelper.getFloat(compound, "minScale");
		maxScale = NBTHelper.getFloat(compound, "maxScale");
		flouncing = NBTHelper.getDouble(compound, "flouncing");
		frequency = NBTHelper.getDouble(compound, "frequence");
		radius = NBTHelper.getDouble(compound, "radius");
		spinSpeed = NBTHelper.getDouble(compound, "spinSpeed");
		gravity = NBTHelper.getFloat(compound, "gravity");
		rate = NBTHelper.getInt(compound, "rate");
		color = NBTHelper.getInt(compound, "color");
		brightness = NBTHelper.getInt(compound, "brightness");
		visibleRange = NBTHelper.getInt(compound, "visibleRange");
		rainbow = NBTHelper.getInt(compound, "rainbow");
		minAge = NBTHelper.getInt(compound, "minAge");
		maxAge = NBTHelper.getInt(compound, "maxAge");
		colorDiff = NBTHelper.getInt(compound, "colorDiff");
		texture = NBTHelper.getInt(compound, "texture");
		depth = NBTHelper.getBoolean(compound, "depth");
		collidable = NBTHelper.getBoolean(compound, "collidable");
		reverse = NBTHelper.getBoolean(compound, "reverse");
		variant = ParticleVariant.values()[NBTHelper.getInt(compound, "variant")];
		red = Redstone.values()[NBTHelper.getInt(compound, "red")];
		ax = Axis.values()[NBTHelper.getInt(compound, "ax")];
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		NBTHelper.setDouble(compound, "minXPos", minXPos);
		NBTHelper.setDouble(compound, "minYPos", minYPos);
		NBTHelper.setDouble(compound, "minZPos", minZPos);
		NBTHelper.setDouble(compound, "maxXPos", maxXPos);
		NBTHelper.setDouble(compound, "maxYPos", maxYPos);
		NBTHelper.setDouble(compound, "maxZPos", maxZPos);
		NBTHelper.setDouble(compound, "minXMotion", minXMotion);
		NBTHelper.setDouble(compound, "minYMotion", minYMotion);
		NBTHelper.setDouble(compound, "minZMotion", minZMotion);
		NBTHelper.setDouble(compound, "maxXMotion", maxXMotion);
		NBTHelper.setDouble(compound, "maxYMotion", maxYMotion);
		NBTHelper.setDouble(compound, "maxZMotion", maxZMotion);
		NBTHelper.setFloat(compound, "minScale", minScale);
		NBTHelper.setFloat(compound, "maxScale", maxScale);
		NBTHelper.setDouble(compound, "flouncing", flouncing);
		NBTHelper.setDouble(compound, "frequence", frequency);
		NBTHelper.setDouble(compound, "radius", radius);
		NBTHelper.setDouble(compound, "spinSpeed", spinSpeed);
		NBTHelper.setFloat(compound, "gravity", gravity);
		NBTHelper.setInt(compound, "rate", rate);
		NBTHelper.setInt(compound, "color", color);
		NBTHelper.setInt(compound, "brightness", brightness);
		NBTHelper.setInt(compound, "visibleRange", visibleRange);
		NBTHelper.setInt(compound, "rainbow", rainbow);
		NBTHelper.setInt(compound, "minAge", minAge);
		NBTHelper.setInt(compound, "maxAge", maxAge);
		NBTHelper.setInt(compound, "colorDiff", colorDiff);
		NBTHelper.setInt(compound, "texture", texture);
		NBTHelper.setBoolean(compound, "depth", depth);
		NBTHelper.setBoolean(compound, "collidable", collidable);
		NBTHelper.setBoolean(compound, "reverse", reverse);
		NBTHelper.setInt(compound, "variant", variant.ordinal());
		NBTHelper.setInt(compound, "red", red.ordinal());
		NBTHelper.setInt(compound, "ax", ax.ordinal());
		return super.writeToNBT(compound);
	}

	@Override
	public void onRightClicked(EntityPlayer player, EnumHand hand) {
		player.openGui(ParticleSpreader.instance, 0, world, getX(), getY(), getZ());
	}

	@Override
	public void onLeftClicked(EntityPlayer player, EnumHand hand) {
		if (!world.isRemote) {
			ItemStack s = new ItemStack(ParticleSpreader.spreader);
			s.setTagCompound(new NBTTagCompound());
			writeToNBT(s.getTagCompound());
			EntityItem ei = new EntityItem(world, getX() + .5, getY() + .5, getZ() + .5, s);
			ei.setPositionAndUpdate(player.posX, player.posY + .3, player.posZ);
			getRegistry().removeDataPart(pos);
			world.playEvent(2001, pos, Block.getStateId(Blocks.GLASS.getDefaultState()));
		}
	}

	@Override
	public void updateClient(World world) {
		if (ticksExisted % rate != 0 || !redstoneValid())
			return;
		//			correctValues();
		switch (variant) {
		case NORMAL: {
			CommonParticle par = new CommonParticle(posX(), posY(), posZ(), motionX(), motionY(), motionZ());
			applyValues(par);
			LimeLib.proxy.renderParticle(par);
			break;
		}
		case CIRCLE: {
			for (Vec3d vec : ParticleHelper.getVecsForCircle(posX(), posY(), posZ(), radius, frequency, ax)) {
				CommonParticle par = new CommonParticle(vec.xCoord, vec.yCoord, vec.zCoord, MathHelper.nextDouble(random, minXMotion, maxXMotion), MathHelper.nextDouble(random, minYMotion, maxYMotion), MathHelper.nextDouble(random, minZMotion, maxZMotion));
				applyValues(par);
				LimeLib.proxy.renderParticle(par);
			}
			break;
		}
		case SQUARE: {
			for (Vec3d vec : ParticleHelper.getVecsForSquare(posX(), posY(), posZ(), radius, frequency, ax)) {
				CommonParticle par = new CommonParticle(vec.xCoord, vec.yCoord, vec.zCoord, MathHelper.nextDouble(random, minXMotion, maxXMotion), MathHelper.nextDouble(random, minYMotion, maxYMotion), MathHelper.nextDouble(random, minZMotion, maxZMotion));
				applyValues(par);
				LimeLib.proxy.renderParticle(par);
			}
			break;
		}
		case SPIRAL: {
			Vec3d vec = ParticleHelper.getVecForSpirale(radius / 25., spinSpeed / 10., frequency + 40., reverse, ax);
			CommonParticle par = new CommonParticle(posX(), posY(), posZ(), ax == Axis.X ? motionX() : vec.xCoord, ax == Axis.Y ? motionY() : vec.yCoord, ax == Axis.Z ? motionZ() : vec.zCoord);
			applyValues(par);
			LimeLib.proxy.renderParticle(par);
			break;
		}
		case EXPLOSION: {
			for (Vec3d vec : ParticleHelper.getVecsForExplosion(radius / 25., frequency + 20., ax)) {
				CommonParticle par = new CommonParticle(posX(), posY(), posZ(), ax == Axis.X ? motionX() : vec.xCoord, ax == Axis.Y ? motionY() : vec.yCoord, ax == Axis.Z ? motionZ() : vec.zCoord);
				applyValues(par);
				LimeLib.proxy.renderParticle(par);
			}
			break;
		}
		}
	}

	public void applyValues(CommonParticle par) {
		par.setScale(MathHelper.nextFloat(random, minScale, maxScale));
		int col = ColorHelper.getRGB(rainbow > 0 ? ColorHelper.getRainbow(rainbow) : color, ColorHelper.getAlpha(color));
		par.setColor(col, rainbow > 0 ? 0 : colorDiff);
		par.setFlouncing(flouncing / 100.);
		par.setBrightness(brightness);
		par.setDepth(depth);
		par.setSmoothEnd(true);
		par.setVisibleRange(visibleRange);
		par.setMaxAge2(MathHelper.getInt(random, minAge, maxAge));
		if (textures[texture].equalsIgnoreCase("round"))
			par.setTexture(ParticleHelper.roundParticle);
		else if (textures[texture].equalsIgnoreCase("sparkle"))
			par.setTexture(ParticleHelper.sparkleParticle);
		else if (textures[texture].equalsIgnoreCase("square"))
			par.setTexture(ParticleHelper.squareParticle);
		par.setGravity(gravity);
		par.setNoClip(!collidable);
	}

	//@formatter:off
	double posX() {return getX() + MathHelper.nextDouble(random,minXPos, maxXPos);}
	double posY() {return getY() + MathHelper.nextDouble(random,minYPos, maxYPos);}
	double posZ() {return getZ() + MathHelper.nextDouble(random,minZPos, maxZPos);}
	double motionX() {return MathHelper.nextDouble(random,minXMotion, maxXMotion);}
	double motionY() {return MathHelper.nextDouble(random,minYMotion, maxYMotion);}
	double motionZ() {return MathHelper.nextDouble(random,minZMotion, maxZMotion);}
	
	private boolean redstoneValid() {
		switch (red) {
		case ALWAYS:return true;
		case NEVER:return false;
		case ON:return world.isBlockPowered(pos);
		case OFF:return !world.isBlockPowered(pos);
		default:return false;
		}
	}
	//@formatter:on

	@Override
	public AxisAlignedBB getHighlightBox() {
		return new AxisAlignedBB(.3, .3, .3, .7, .7, .7);
	}
}
