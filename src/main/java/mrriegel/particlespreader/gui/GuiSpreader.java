package mrriegel.particlespreader.gui;

import java.awt.Color;
import java.io.IOException;
import java.util.Collections;
import java.util.Set;

import mrriegel.limelib.datapart.DataPartRegistry;
import mrriegel.limelib.gui.CommonGuiScreen;
import mrriegel.limelib.gui.GuiDrawer;
import mrriegel.limelib.gui.button.GuiButtonSimple;
import mrriegel.limelib.helper.ColorHelper;
import mrriegel.limelib.network.PacketHandler;
import mrriegel.particlespreader.ParticlePart;
import mrriegel.particlespreader.network.MessageToServer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.util.EnumFacing.Axis;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.client.config.GuiCheckBox;
import net.minecraftforge.fml.client.config.GuiSlider;

import org.apache.commons.lang3.text.WordUtils;
import org.lwjgl.input.Keyboard;

import com.google.common.base.Predicate;
import com.google.common.collect.Sets;

public class GuiSpreader extends CommonGuiScreen {
	ParticlePart part;
	GuiButton particle, redstone, axis, texture;
	GuiTextField minXp, minYp, minZp, maxXp, maxYp, maxZp;
	GuiTextField minXm, minYm, minZm, maxXm, maxYm, maxZm;
	GuiTextField minScale, maxScale, minAge, maxAge, flounc, freq, radius, gravity, spinSpeed;
	GuiCheckBox depth, collidable, reverse;
	GuiTextField rate, bright, range, rainbow, colordiff;

	private Set<GuiTextField> textFields = Sets.newHashSet();
	private DataPartRegistry reg;
	private int textfieldid = 0;

	private static Set<Character> set = Sets.newHashSet('d', 'f', 'D', 'F');
	private static Predicate<String> predDouble = s -> {
		if (s.isEmpty())
			return true;
		try {
			Double.parseDouble(s);
			return !s.chars().anyMatch(i -> set.stream().anyMatch(c -> c == i));
		} catch (Exception e) {
			return s.equals("-") || s.equals(".");
		}
	};
	private static Predicate<String> predInt = s -> {
		if (s.isEmpty())
			return true;
		try {
			Integer.parseInt(s);
			return true;
		} catch (Exception e) {
			return s.equals("-");
		}
	};

	public GuiSpreader(ParticlePart part) {
		this.part = part;
		ySize = 220;
		xSize = 248;
	}

	private void toServer() {
		updatePart();
		part.correctValues();
		PacketHandler.sendToServer(new MessageToServer(part));
	}

	private void updatePart() {
		part.minXPos = toDouble(minXp.getText());
		part.maxXPos = toDouble(maxXp.getText(), part.minXPos);
		part.minYPos = toDouble(minYp.getText());
		part.maxYPos = toDouble(maxYp.getText(), part.minYPos);
		part.minZPos = toDouble(minZp.getText());
		part.maxZPos = toDouble(maxZp.getText(), part.minZPos);
		part.minXMotion = toDouble(minXm.getText());
		part.maxXMotion = toDouble(maxXm.getText(), part.minXMotion);
		part.minYMotion = toDouble(minYm.getText());
		part.maxYMotion = toDouble(maxYm.getText(), part.minYMotion);
		part.minZMotion = toDouble(minZm.getText());
		part.maxZMotion = toDouble(maxZm.getText(), part.minZMotion);
		part.minScale = toFloat(minScale.getText());
		part.maxScale = toFloat(maxScale.getText(), part.minScale);
		part.minAge = toInt(minAge.getText());
		part.maxAge = toInt(maxAge.getText(), part.minAge);
		part.flouncing = toDouble(flounc.getText());
		part.frequency = toDouble(freq.getText());
		part.radius = toDouble(radius.getText());
		part.gravity = toFloat(gravity.getText());
		part.spinSpeed = toDouble(spinSpeed.getText());
		part.rate = toInt(rate.getText());
		part.brightness = toInt(bright.getText());
		part.visibleRange = toInt(range.getText());
		part.rainbow = toInt(rainbow.getText());
		part.colorDiff = toInt(colordiff.getText());
	}

	private double toDouble(String s, double min) {
		if (s.isEmpty())
			return MathHelper.clamp(0., min, 133777.);
		double ul = 0.;
		try {
			ul = Double.parseDouble(s);
		} catch (Exception e) {
		}
		ul = MathHelper.clamp(ul, min, 133777.);
		return ul;
	}

	private double toDouble(String s) {
		return toDouble(s, -133777.);
	}

	private float toFloat(String s, float min) {
		if (s.isEmpty())
			return MathHelper.clamp(0f, min, 133777f);
		float ul = 0f;
		try {
			ul = Float.parseFloat(s);
		} catch (Exception e) {
		}
		ul = MathHelper.clamp(ul, min, 133777f);
		return ul;
	}

	private float toFloat(String s) {
		return toFloat(s, -133777f);
	}

	private int toInt(String s, int min) {
		if (s.isEmpty())
			return MathHelper.clamp(0, min, 133777);
		int ul = 0;
		try {
			ul = Integer.parseInt(s);
		} catch (Exception e) {
		}
		ul = MathHelper.clamp(ul, min, 133777);
		return ul;
	}

	private int toInt(String s) {
		return toInt(s, -133777);
	}

	@Override
	public void initGui() {
		super.initGui();
		reg = DataPartRegistry.get(mc.world);
		buttonList.add(new GuiButtonSimple(-1, 194 + guiLeft, 201 + guiTop, 44, 14, "Color", 0xff000000, part.color, null));
		buttonList.add(particle = new GuiButtonSimple(0, 5 + guiLeft, 6 + guiTop, 55, 12, "", Collections.singletonList("Particle Mode")));
		buttonList.add(redstone = new GuiButtonSimple(1, 64 + guiLeft, 6 + guiTop, 55, 12, "", Collections.singletonList("Redstone Mode")));
		buttonList.add(axis = new GuiButtonSimple(2, 124 + guiLeft, 6 + guiTop, 55, 12, "", Collections.singletonList("Axis")));
		buttonList.add(texture = new GuiButtonSimple(3, 184 + guiLeft, 6 + guiTop, 55, 12, "", Collections.singletonList("Texture")));
		buttonList.add(depth = new GuiCheckBox(4, 120 + guiLeft, 189 + guiTop, "", part.depth));
		buttonList.add(collidable = new GuiCheckBox(5, 120 + guiLeft, 202 + guiTop, "", part.collidable));
		buttonList.add(reverse = new GuiCheckBox(6, 170 + guiLeft, 189 + guiTop, "", part.reverse));
		minXp = getTextField(19, 35, part.minXPos + "", true);
		maxXp = getTextField(67, 35, part.maxXPos + "", true);
		minYp = getTextField(19, 51, part.minYPos + "", true);
		maxYp = getTextField(67, 51, part.maxYPos + "", true);
		minZp = getTextField(19, 67, part.minZPos + "", true);
		maxZp = getTextField(67, 67, part.maxZPos + "", true);
		minXm = getTextField(19, 100, part.minXMotion + "", true);
		maxXm = getTextField(67, 100, part.maxXMotion + "", true);
		minYm = getTextField(19, 116, part.minYMotion + "", true);
		maxYm = getTextField(67, 116, part.maxYMotion + "", true);
		minZm = getTextField(19, 132, part.minZMotion + "", true);
		maxZm = getTextField(67, 132, part.maxZMotion + "", true);
		minScale = getTextField(19, 165, part.minScale + "", true);
		maxScale = getTextField(67, 165, part.maxScale + "", true);
		minAge = getTextField(19, 198, part.minAge + "", false);
		maxAge = getTextField(67, 198, part.maxAge + "", false);
		flounc = getTextField(194, 36 - 10, part.flouncing + "", true);
		freq = getTextField(194, 52 - 10, part.frequency + "", true);
		radius = getTextField(194, 68 - 10, part.radius + "", true);
		gravity = getTextField(194, 84 - 10, part.gravity + "", true);
		spinSpeed = getTextField(194, 100 - 10, part.spinSpeed + "", true);
		rate = getTextField(194, 120 - 10, part.rate + "", false);
		bright = getTextField(194, 136 - 10, part.brightness + "", false);
		range = getTextField(194, 152 - 10, part.visibleRange + "", false);
		rainbow = getTextField(194, 168 - 10, part.rainbow + "", false);
		colordiff = getTextField(194, 184 - 10, part.colorDiff + "", false);
	}

	private GuiTextField getTextField(int x, int y, String s, boolean doub) {
		GuiTextField text = new GuiTextField(textfieldid++, fontRenderer, x + guiLeft, y + guiTop, 44, 12);
		text.setMaxStringLength(6);
		text.setText(s);
		text.setTextColor(-1);
		text.setValidator(doub ? predDouble : predInt);
		textFields.add(text);
		return text;
	}

	int textColor = Color.darkGray.getRGB();

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		drawDefaultBackground();
		drawer.drawBackgroundTexture();
		super.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);
		//position
		drawer.drawFrame(minXp.xPosition - 12 - guiLeft, minXp.yPosition - 4 - guiTop, 108, 51, 1, Color.gray.getRGB());
		fontRenderer.drawString("Position", minXp.xPosition - 7, minXp.yPosition - 13, textColor);
		fontRenderer.drawString("X", minXp.xPosition - 9, minXp.yPosition + 4, textColor);
		fontRenderer.drawString("Y", minYp.xPosition - 9, minYp.yPosition + 4, textColor);
		fontRenderer.drawString("Z", minZp.xPosition - 9, minZp.yPosition + 4, textColor);
		//motion
		drawer.drawFrame(minXm.xPosition - 12 - guiLeft, minXm.yPosition - 4 - guiTop, 108, 51, 1, Color.gray.getRGB());
		fontRenderer.drawString("Motion", minXm.xPosition - 7, minXm.yPosition - 13, textColor);
		fontRenderer.drawString("X", minXm.xPosition - 9, minXm.yPosition + 4, textColor);
		fontRenderer.drawString("Y", minYm.xPosition - 9, minYm.yPosition + 4, textColor);
		fontRenderer.drawString("Z", minZm.xPosition - 9, minZm.yPosition + 4, textColor);
		//scale
		drawer.drawFrame(minScale.xPosition - 12 - guiLeft, minScale.yPosition - 4 - guiTop, 108, 19, 1, Color.gray.getRGB());
		fontRenderer.drawString("Scale", minScale.xPosition - 7, minScale.yPosition - 13, textColor);
		//age
		drawer.drawFrame(minAge.xPosition - 12 - guiLeft, minAge.yPosition - 4 - guiTop, 108, 19, 1, Color.gray.getRGB());
		fontRenderer.drawString("Age", minAge.xPosition - 7, minAge.yPosition - 13, textColor);

		drawString(flounc, "Flouncing");
		drawString(freq, "Frequency");
		drawString(radius, "Radius");
		drawString(gravity, "Gravity");
		drawString(spinSpeed, "Spin speed");
		drawString(rate, "Speed rate");
		drawString(bright, "Brightness");
		drawString(range, "Visible Range");
		drawString(rainbow, "Rainbow");
		drawString(colordiff, "Color Variant");

		drawString(depth, "Depth");
		drawString(collidable, "Collidable");
		drawString(reverse, "Reverse");

		textFields.forEach(GuiTextField::drawTextBox);
	}

	private void drawString(GuiTextField field, String text) {
		fontRenderer.drawString(text, field.xPosition - (fontRenderer.getStringWidth(text) + 4), field.yPosition + 4, textColor);
	}

	private void drawString(GuiCheckBox field, String text) {
		fontRenderer.drawString(text, field.xPosition + 14, field.yPosition + 3, textColor);
	}

	@Override
	public void updateScreen() {
		super.updateScreen();
		if (part == null || reg.getDataPart(part.getPos()) == null) {
			mc.player.closeScreen();
			return;
		}
		particle.displayString = part.variant.toString();
		redstone.displayString = part.red.toString();
		axis.displayString = part.ax.getName().toUpperCase();
		texture.displayString = (ParticlePart.textures[part.texture].equals("random") ? TextFormatting.ITALIC.toString() : "") + WordUtils.capitalize(ParticlePart.textures[part.texture]);
	}

	@Override
	protected void keyTyped(char typedChar, int keyCode) throws IOException {
		boolean typed = false;
		for (GuiTextField g : textFields) {
			if (g.textboxKeyTyped(typedChar, keyCode)) {
				typed = true;
				break;
			}
		}
		if (!typed)
			super.keyTyped(typedChar, keyCode);
		else
			toServer();
	}

	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		super.mouseClicked(mouseX, mouseY, mouseButton);
		textFields.forEach(g -> g.mouseClicked(mouseX, mouseY, mouseButton));
		toServer();
	}

	@Override
	protected void actionPerformed(GuiButton button) throws IOException {
		if (button.id == -1)
			GuiDrawer.openGui(new GuiColor(this));
		else if (button.id == 0)
			part.variant = part.variant.next();
		else if (button.id == 1)
			part.red = part.red.next();
		else if (button.id == 2)
			part.ax = Axis.values()[(part.ax.ordinal() + 1) % Axis.values().length];
		else if (button.id == 3)
			part.texture = (part.texture + 1) % ParticlePart.textures.length;
		else if (button.id == 4)
			part.depth ^= true;
		else if (button.id == 5)
			part.collidable ^= true;
		else if (button.id == 6)
			part.reverse ^= true;
	}

	@Override
	public boolean doesGuiPauseGame() {
		return false;
	}

	private static class GuiColor extends CommonGuiScreen {
		GuiSpreader parent;

		public GuiColor(GuiSpreader parent) {
			super();
			this.parent = parent;
			xSize = 100;
			ySize = 130;
		}

		@Override
		public void initGui() {
			super.initGui();
			buttonList.add(new GuiSlider(0, 7 + guiLeft, 7 + guiTop, 86, 20, "Red ", "", 0, 255, ColorHelper.getRed(parent.part.color), false, true));
			buttonList.add(new GuiSlider(1, 7 + guiLeft, 32 + guiTop, 86, 20, "Green ", "", 0, 255, ColorHelper.getGreen(parent.part.color), false, true));
			buttonList.add(new GuiSlider(2, 7 + guiLeft, 57 + guiTop, 86, 20, "Blue ", "", 0, 255, ColorHelper.getBlue(parent.part.color), false, true));
			buttonList.add(new GuiSlider(3, 7 + guiLeft, 83 + guiTop, 86, 20, "Alpha ", "", 0, 255, ColorHelper.getAlpha(parent.part.color), false, true));
		}

		@Override
		protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
			drawDefaultBackground();
			drawer.drawBackgroundTexture();
			super.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);
			drawer.drawColoredRectangle(7, 109, 86, 12, parent.part.color);
			drawer.drawFrame(7, 108, 86, 13, 1, Color.darkGray.getRGB());
		}

		@Override
		public void drawScreen(int mouseX, int mouseY, float partialTicks) {
			parent.drawScreen(mouseX, mouseY, partialTicks);
			RenderHelper.disableStandardItemLighting();
			super.drawScreen(mouseX, mouseY, partialTicks);
		}

		@Override
		protected void keyTyped(char typedChar, int keyCode) throws IOException {
			if (keyCode == Keyboard.KEY_ESCAPE) {
				GuiDrawer.openGui(parent);
				parent.toServer();
			} else
				super.keyTyped(typedChar, keyCode);
		}

		@Override
		public void updateScreen() {
			super.updateScreen();
			int red = (int) MathHelper.clamp(((GuiSlider) buttonList.get(0)).sliderValue * 255, 0, 255);
			int green = (int) MathHelper.clamp(((GuiSlider) buttonList.get(1)).sliderValue * 255, 0, 255);
			int blue = (int) MathHelper.clamp(((GuiSlider) buttonList.get(2)).sliderValue * 255, 0, 255);
			int alpha = (int) MathHelper.clamp(((GuiSlider) buttonList.get(3)).sliderValue * 255, 0, 255);
			int color = new Color(red, green, blue, alpha).getRGB();
			parent.part.color = color;
		}

		@Override
		protected void mouseReleased(int mouseX, int mouseY, int state) {
			parent.toServer();
			super.mouseReleased(mouseX, mouseY, state);
		}

		@Override
		protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
			super.mouseClicked(mouseX, mouseY, mouseButton);
			boolean in = mouseX > guiLeft && mouseX < guiLeft + width && mouseY > guiTop && mouseX < guiTop + height;
			if (!in && mouseButton == 0)
				try {
					keyTyped('a', Keyboard.KEY_ESCAPE);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}

		@Override
		public boolean doesGuiPauseGame() {
			return false;
		}

	}

}
