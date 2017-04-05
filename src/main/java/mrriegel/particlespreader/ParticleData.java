package mrriegel.particlespreader;

import mrriegel.limelib.helper.NBTHelper;
import mrriegel.particlespreader.ParticlePart.ParticleVariant;
import mrriegel.particlespreader.ParticlePart.Redstone;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing.Axis;
import net.minecraft.util.math.MathHelper;

public class ParticleData {
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
		return compound;
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
		spinSpeed = Math.max(spinSpeed, 0.01);
		rate = Math.max(rate, 1);
		visibleRange = Math.max(visibleRange, 0);
		minAge = Math.max(minAge, 1);
		maxAge = MathHelper.clamp(maxAge, minAge, 133777);
		colorDiff = MathHelper.clamp(colorDiff, 0, 255);
		texture %= ParticlePart.textures.length;
	}

	public void nextTexture() {
		texture = (texture + 1) % ParticlePart.textures.length;
	}

}
