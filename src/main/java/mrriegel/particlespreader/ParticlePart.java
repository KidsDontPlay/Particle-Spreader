package mrriegel.particlespreader;

import java.util.List;
import java.util.Map;
import java.util.Random;

import mrriegel.limelib.LimeLib;
import mrriegel.limelib.datapart.DataPart;
import mrriegel.limelib.helper.ColorHelper;
import mrriegel.limelib.helper.ParticleHelper;
import mrriegel.limelib.particle.CommonParticle;
import net.minecraft.block.Block;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing.Axis;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public class ParticlePart extends DataPart {

	public int activeParticle = 0;
	public List<ParticleData> parDatas = Lists.newArrayList();
	{
		for (int i = 0; i < 8; i++) {
			parDatas.add(new ParticleData());
			if (i != 0)
				parDatas.get(i).red = Redstone.NEVER;
		}
	}

	private static Random random = new Random();
	static Map<String, ResourceLocation> textureMap = Maps.newHashMap();
	public static String[] textures = new String[] { "round", "sparkle", "square", "random" };

	static {
		textureMap.put("round", ParticleHelper.roundParticle);
		textureMap.put("sparkle", ParticleHelper.sparkleParticle);
		textureMap.put("square", ParticleHelper.squareParticle);
	}

	public ParticleData getActive() {
		return parDatas.get(activeParticle);
	}

	@Override
	public void readFromNBT(NBTTagCompound compound) {
		activeParticle = compound.getInteger("active");
		for (int i = 0; i < 8; i++) {
			if (compound.hasKey("partdata_" + i))
				parDatas.get(i).readFromNBT(compound.getCompoundTag("partdata_" + i));
			parDatas.get(i).correctValues();
		}
		if (compound.hasKey("minXPos")) {
			parDatas.get(0).readFromNBT(compound);
		}
		super.readFromNBT(compound);
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		for (int i = 0; i < 8; i++)
			compound.setTag("partdata_" + i, parDatas.get(i).writeToNBT(new NBTTagCompound()));
		compound.setInteger("active", activeParticle);
		return super.writeToNBT(compound);
	}

	@Override
	public boolean onRightClicked(EntityPlayer player, EnumHand hand) {
		player.openGui(ParticleSpreader.instance, 0, world, getX(), getY(), getZ());
		return true;
	}

	@Override
	public boolean onLeftClicked(EntityPlayer player, EnumHand hand) {
		if (!world.isRemote) {
			ItemStack s = new ItemStack(ParticleSpreader.spreader);
			s.setTagCompound(new NBTTagCompound());
			writeToNBT(s.getTagCompound());
			EntityItem ei = new EntityItem(world, getX() + .5, getY() + .5, getZ() + .5, s);
			ei.setPositionAndUpdate(player.posX, player.posY + .3, player.posZ);
			getRegistry().removeDataPart(pos);
			world.playEvent(2001, pos, Block.getStateId(Blocks.GLASS.getDefaultState()));
		}
		return true;
	}

	@Override
	public void updateClient(World world) {
		ParticleSpreader.proxy.highlightPart(pos);
		for (int i = 0; i < parDatas.size(); i++) {
			ParticleData parD = parDatas.get(i);
			if ((ticksExisted + i) % parD.rate != 0 || !redstoneValid(parD))
				continue;
			//			correctValues();
			switch (parD.variant) {
			case NORMAL: {
				CommonParticle par = new CommonParticle(posX(parD), posY(parD), posZ(parD), motionX(parD), motionY(parD), motionZ(parD));
				applyValues(par, parD);
				LimeLib.proxy.renderParticle(par);
				break;
			}
			case CIRCLE: {
				for (Vec3d vec : ParticleHelper.getVecsForCircle(posX(parD), posY(parD), posZ(parD), parD.radius, parD.frequency, parD.ax)) {
					CommonParticle par = new CommonParticle(vec.xCoord, vec.yCoord, vec.zCoord, MathHelper.nextDouble(random, parD.minXMotion, parD.maxXMotion), MathHelper.nextDouble(random, parD.minYMotion, parD.maxYMotion), MathHelper.nextDouble(random, parD.minZMotion, parD.maxZMotion));
					applyValues(par, parD);
					LimeLib.proxy.renderParticle(par);
				}
				break;
			}
			case SQUARE: {
				for (Vec3d vec : ParticleHelper.getVecsForSquare(posX(parD), posY(parD), posZ(parD), parD.radius, parD.frequency, parD.ax)) {
					CommonParticle par = new CommonParticle(vec.xCoord, vec.yCoord, vec.zCoord, MathHelper.nextDouble(random, parD.minXMotion, parD.maxXMotion), MathHelper.nextDouble(random, parD.minYMotion, parD.maxYMotion), MathHelper.nextDouble(random, parD.minZMotion, parD.maxZMotion));
					applyValues(par, parD);
					LimeLib.proxy.renderParticle(par);
				}
				break;
			}
			case SPIRAL1: {
				Vec3d vec = ParticleHelper.getVecForSpirale(parD.radius / 25., parD.spinSpeed / 10., parD.frequency + 40., parD.reverse, parD.ax);
				CommonParticle par = new CommonParticle(posX(parD), posY(parD), posZ(parD), parD.ax == Axis.X ? motionX(parD) : vec.xCoord, parD.ax == Axis.Y ? motionY(parD) : vec.yCoord, parD.ax == Axis.Z ? motionZ(parD) : vec.zCoord);
				applyValues(par, parD);
				LimeLib.proxy.renderParticle(par);
				break;
			}
			case SPIRAL2: {
				List<Vec3d> lis = ParticleHelper.getVecsForCircle(posX(parD), posY(parD), posZ(parD), parD.radius, parD.frequency * 2, parD.ax);
				int index = ((int) ((ticksExisted) * (parD.spinSpeed * 10.1))) % lis.size();
				//			index=ticksExisted%lis.size();
				Vec3d vec = lis.get(index);
				CommonParticle par = new CommonParticle(vec.xCoord, vec.yCoord, vec.zCoord, MathHelper.nextDouble(random, parD.minXMotion, parD.maxXMotion), MathHelper.nextDouble(random, parD.minYMotion, parD.maxYMotion), MathHelper.nextDouble(random, parD.minZMotion, parD.maxZMotion));
				applyValues(par, parD);
				LimeLib.proxy.renderParticle(par);
				break;
			}
			case EXPLOSION: {
				for (Vec3d vec : ParticleHelper.getVecsForExplosion(parD.radius / 25., parD.frequency + 20., parD.ax)) {
					CommonParticle par = new CommonParticle(posX(parD), posY(parD), posZ(parD), parD.ax == Axis.X ? motionX(parD) : vec.xCoord, parD.ax == Axis.Y ? motionY(parD) : vec.yCoord, parD.ax == Axis.Z ? motionZ(parD) : vec.zCoord);
					applyValues(par, parD);
					LimeLib.proxy.renderParticle(par);
				}
				break;
			}
			}
		}
	}

	public void applyValues(CommonParticle par, ParticleData parD) {
		par.setScale(MathHelper.nextFloat(random, parD.minScale, parD.maxScale));
		int col = ColorHelper.getRGB(parD.rainbow > 0 ? ColorHelper.getRainbow(parD.rainbow) : parD.color, ColorHelper.getAlpha(parD.color));
		par.setColor(col, parD.rainbow > 0 ? 0 : parD.colorDiff);
		par.setFlouncing(parD.flouncing / 100.);
		par.setBrightness(parD.brightness);
		par.setDepth(parD.depth);
		par.setSmoothEnd(true);
		par.setVisibleRange(parD.visibleRange);
		par.setMaxAge2(MathHelper.getInt(random, parD.minAge, parD.maxAge));
		ResourceLocation loc = textures[parD.texture].equals("random") ? Lists.newArrayList(textureMap.values()).get(new Random().nextInt(textureMap.values().size())) : textureMap.get(textures[parD.texture]);
		par.setTexture(loc != null ? loc : ParticleHelper.roundParticle);
		par.setGravity(parD.gravity);
		par.setNoClip(!parD.collidable);
	}

	//@formatter:off
	double posX(ParticleData parD) {return getX() + MathHelper.nextDouble(random,parD.minXPos,parD.maxXPos);}
	double posY(ParticleData parD) {return getY() + MathHelper.nextDouble(random,parD.minYPos,parD.maxYPos);}
	double posZ(ParticleData parD) {return getZ() + MathHelper.nextDouble(random,parD.minZPos,parD.maxZPos);}
	double motionX(ParticleData parD) {return MathHelper.nextDouble(random,parD.minXMotion,parD.maxXMotion);}
	double motionY(ParticleData parD) {return MathHelper.nextDouble(random,parD.minYMotion,parD.maxYMotion);}
	double motionZ(ParticleData parD) {return MathHelper.nextDouble(random,parD.minZMotion,parD.maxZMotion);}
	
	private boolean redstoneValid(ParticleData parData) {
		switch (parData.red) {
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

	public static enum ParticleVariant {
		NORMAL, CIRCLE, SQUARE, SPIRAL1, SPIRAL2, EXPLOSION;
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
}
