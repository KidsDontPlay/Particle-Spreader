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
		minXPos = NBTHelper.get(compound, "minXPos", Double.class);
		minYPos = NBTHelper.get(compound, "minYPos", Double.class);
		minZPos = NBTHelper.get(compound, "minZPos", Double.class);
		maxXPos = NBTHelper.get(compound, "maxXPos", Double.class);
		maxYPos = NBTHelper.get(compound, "maxYPos", Double.class);
		maxZPos = NBTHelper.get(compound, "maxZPos", Double.class);
		minXMotion = NBTHelper.get(compound, "minXMotion", Double.class);
		minYMotion = NBTHelper.get(compound, "minYMotion", Double.class);
		minZMotion = NBTHelper.get(compound, "minZMotion", Double.class);
		maxXMotion = NBTHelper.get(compound, "maxXMotion", Double.class);
		maxYMotion = NBTHelper.get(compound, "maxYMotion", Double.class);
		maxZMotion = NBTHelper.get(compound, "maxZMotion", Double.class);
		flouncing = NBTHelper.get(compound, "flouncing", Double.class);
		frequency = NBTHelper.get(compound, "frequence", Double.class);
		radius = NBTHelper.get(compound, "radius", Double.class);
		spinSpeed = NBTHelper.get(compound, "spinSpeed", Double.class);
		minScale = NBTHelper.get(compound, "minScale", Float.class);
		maxScale = NBTHelper.get(compound, "maxScale", Float.class);
		gravity = NBTHelper.get(compound, "gravity", Float.class);
		rate = NBTHelper.get(compound, "rate", Integer.class);
		color = NBTHelper.get(compound, "color", Integer.class);
		brightness = NBTHelper.get(compound, "brightness", Integer.class);
		visibleRange = NBTHelper.get(compound, "visibleRange", Integer.class);
		rainbow = NBTHelper.get(compound, "rainbow", Integer.class);
		minAge = NBTHelper.get(compound, "minAge", Integer.class);
		maxAge = NBTHelper.get(compound, "maxAge", Integer.class);
		colorDiff = NBTHelper.get(compound, "colorDiff", Integer.class);
		texture = NBTHelper.get(compound, "texture", Integer.class);
		depth = NBTHelper.get(compound, "depth", Boolean.class);
		collidable = NBTHelper.get(compound, "collidable", Boolean.class);
		reverse = NBTHelper.get(compound, "reverse", Boolean.class);
		variant = ParticleVariant.values()[NBTHelper.get(compound, "variant", Integer.class)];
		red = Redstone.values()[NBTHelper.get(compound, "red", Integer.class)];
		ax = Axis.values()[NBTHelper.get(compound, "ax", Integer.class)];
		correctValues();
	}

	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		NBTHelper.set(compound, "minXPos", minXPos);
		NBTHelper.set(compound, "minYPos", minYPos);
		NBTHelper.set(compound, "minZPos", minZPos);
		NBTHelper.set(compound, "maxXPos", maxXPos);
		NBTHelper.set(compound, "maxYPos", maxYPos);
		NBTHelper.set(compound, "maxZPos", maxZPos);
		NBTHelper.set(compound, "minXMotion", minXMotion);
		NBTHelper.set(compound, "minYMotion", minYMotion);
		NBTHelper.set(compound, "minZMotion", minZMotion);
		NBTHelper.set(compound, "maxXMotion", maxXMotion);
		NBTHelper.set(compound, "maxYMotion", maxYMotion);
		NBTHelper.set(compound, "maxZMotion", maxZMotion);
		NBTHelper.set(compound, "flouncing", flouncing);
		NBTHelper.set(compound, "frequence", frequency);
		NBTHelper.set(compound, "radius", radius);
		NBTHelper.set(compound, "spinSpeed", spinSpeed);
		NBTHelper.set(compound, "minScale", minScale);
		NBTHelper.set(compound, "maxScale", maxScale);
		NBTHelper.set(compound, "gravity", gravity);
		NBTHelper.set(compound, "rate", rate);
		NBTHelper.set(compound, "color", color);
		NBTHelper.set(compound, "brightness", brightness);
		NBTHelper.set(compound, "visibleRange", visibleRange);
		NBTHelper.set(compound, "rainbow", rainbow);
		NBTHelper.set(compound, "minAge", minAge);
		NBTHelper.set(compound, "maxAge", maxAge);
		NBTHelper.set(compound, "colorDiff", colorDiff);
		NBTHelper.set(compound, "texture", texture);
		NBTHelper.set(compound, "depth", depth);
		NBTHelper.set(compound, "collidable", collidable);
		NBTHelper.set(compound, "reverse", reverse);
		NBTHelper.set(compound, "variant", variant.ordinal());
		NBTHelper.set(compound, "red", red.ordinal());
		NBTHelper.set(compound, "ax", ax.ordinal());
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
