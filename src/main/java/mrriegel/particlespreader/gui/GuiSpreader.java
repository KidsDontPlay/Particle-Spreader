package mrriegel.particlespreader.gui;

import java.awt.Color;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
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
import net.minecraftforge.fml.client.config.GuiButtonExt;
import net.minecraftforge.fml.client.config.GuiCheckBox;
import net.minecraftforge.fml.client.config.GuiSlider;

import org.apache.commons.lang3.text.WordUtils;
import org.lwjgl.input.Keyboard;

import com.google.common.base.Predicate;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

public class GuiSpreader extends CommonGuiScreen {
	ParticlePart part;
	GuiButton particle, redstone, axis, texture;
	GuiTextField minXp, minYp, minZp, maxXp, maxYp, maxZp;
	GuiTextField minXm, minYm, minZm, maxXm, maxYm, maxZm;
	GuiTextField minScale, maxScale, minAge, maxAge, flounc, freq, radius, gravity, spinSpeed;
	GuiCheckBox depth, collidable, reverse;
	GuiTextField rate, bright, range, rainbow, colordiff;
	List<GuiButton> particles = Lists.newArrayList();

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
		part.getActive().correctValues();
		PacketHandler.sendToServer(new MessageToServer(part));
	}

	private void updatePart() {
		part.getActive().minXPos = toDouble(minXp.getText());
		part.getActive().maxXPos = toDouble(maxXp.getText(), part.getActive().minXPos);
		part.getActive().minYPos = toDouble(minYp.getText());
		part.getActive().maxYPos = toDouble(maxYp.getText(), part.getActive().minYPos);
		part.getActive().minZPos = toDouble(minZp.getText());
		part.getActive().maxZPos = toDouble(maxZp.getText(), part.getActive().minZPos);
		part.getActive().minXMotion = toDouble(minXm.getText());
		part.getActive().maxXMotion = toDouble(maxXm.getText(), part.getActive().minXMotion);
		part.getActive().minYMotion = toDouble(minYm.getText());
		part.getActive().maxYMotion = toDouble(maxYm.getText(), part.getActive().minYMotion);
		part.getActive().minZMotion = toDouble(minZm.getText());
		part.getActive().maxZMotion = toDouble(maxZm.getText(), part.getActive().minZMotion);
		part.getActive().minScale = toFloat(minScale.getText());
		part.getActive().maxScale = toFloat(maxScale.getText(), part.getActive().minScale);
		part.getActive().minAge = toInt(minAge.getText());
		part.getActive().maxAge = toInt(maxAge.getText(), part.getActive().minAge);
		part.getActive().flouncing = toDouble(flounc.getText());
		part.getActive().frequency = toDouble(freq.getText());
		part.getActive().radius = toDouble(radius.getText());
		part.getActive().gravity = toFloat(gravity.getText());
		part.getActive().spinSpeed = toDouble(spinSpeed.getText());
		part.getActive().rate = toInt(rate.getText());
		part.getActive().brightness = toInt(bright.getText());
		part.getActive().visibleRange = toInt(range.getText());
		part.getActive().rainbow = toInt(rainbow.getText());
		part.getActive().colorDiff = toInt(colordiff.getText());
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
		buttonList.clear();
		for (int i = 0; i < 8; i++)
			buttonList.add(new GuiButtonExt(-10 - i, -23 + guiLeft, 10 + i * 23 + guiTop, 20, 20, new Integer(i + 1).toString()));
		particles.clear();
		particles.addAll(buttonList);
		particles.get(part.activeParticle).enabled = false;
		buttonList.add(new GuiButtonSimple(-1, 194 + guiLeft, 201 + guiTop, 44, 14, "Color", 0xff000000, part.getActive().color, null));
		buttonList.add(particle = new GuiButtonSimple(0, 5 + guiLeft, 6 + guiTop, 55, 12, "", Collections.singletonList("Particle Mode")));
		buttonList.add(redstone = new GuiButtonSimple(1, 64 + guiLeft, 6 + guiTop, 55, 12, "", Collections.singletonList("Redstone Mode")));
		buttonList.add(axis = new GuiButtonSimple(2, 124 + guiLeft, 6 + guiTop, 55, 12, "", Collections.singletonList("Axis")));
		buttonList.add(texture = new GuiButtonSimple(3, 184 + guiLeft, 6 + guiTop, 55, 12, "", Collections.singletonList("Texture")));
		buttonList.add(depth = new GuiCheckBox(4, 120 + guiLeft, 189 + guiTop, "", part.getActive().depth));
		buttonList.add(collidable = new GuiCheckBox(5, 120 + guiLeft, 202 + guiTop, "", part.getActive().collidable));
		buttonList.add(reverse = new GuiCheckBox(6, 170 + guiLeft, 189 + guiTop, "", part.getActive().reverse));
		textFields.clear();
		minXp = getTextField(19, 35, part.getActive().minXPos + "", true);
		maxXp = getTextField(67, 35, part.getActive().maxXPos + "", true);
		minYp = getTextField(19, 51, part.getActive().minYPos + "", true);
		maxYp = getTextField(67, 51, part.getActive().maxYPos + "", true);
		minZp = getTextField(19, 67, part.getActive().minZPos + "", true);
		maxZp = getTextField(67, 67, part.getActive().maxZPos + "", true);
		minXm = getTextField(19, 100, part.getActive().minXMotion + "", true);
		maxXm = getTextField(67, 100, part.getActive().maxXMotion + "", true);
		minYm = getTextField(19, 116, part.getActive().minYMotion + "", true);
		maxYm = getTextField(67, 116, part.getActive().maxYMotion + "", true);
		minZm = getTextField(19, 132, part.getActive().minZMotion + "", true);
		maxZm = getTextField(67, 132, part.getActive().maxZMotion + "", true);
		minScale = getTextField(19, 165, part.getActive().minScale + "", true);
		maxScale = getTextField(67, 165, part.getActive().maxScale + "", true);
		minAge = getTextField(19, 198, part.getActive().minAge + "", false);
		maxAge = getTextField(67, 198, part.getActive().maxAge + "", false);
		flounc = getTextField(194, 36 - 10, part.getActive().flouncing + "", true);
		freq = getTextField(194, 52 - 10, part.getActive().frequency + "", true);
		radius = getTextField(194, 68 - 10, part.getActive().radius + "", true);
		gravity = getTextField(194, 84 - 10, part.getActive().gravity + "", true);
		spinSpeed = getTextField(194, 100 - 10, part.getActive().spinSpeed + "", true);
		rate = getTextField(194, 120 - 10, part.getActive().rate + "", false);
		bright = getTextField(194, 136 - 10, part.getActive().brightness + "", false);
		range = getTextField(194, 152 - 10, part.getActive().visibleRange + "", false);
		rainbow = getTextField(194, 168 - 10, part.getActive().rainbow + "", false);
		colordiff = getTextField(194, 184 - 10, part.getActive().colorDiff + "", false);
		toServer();
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
		if (!isShiftKeyDown()) {
			drawDefaultBackground();
			drawer.drawBackgroundTexture();
		}
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
		if (!isShiftKeyDown())
			textColor = Color.darkGray.getRGB();
		else
			textColor = 0xffffff;
		particle.displayString = part.getActive().variant.toString();
		redstone.displayString = part.getActive().red.toString();
		axis.displayString = part.getActive().ax.getName().toUpperCase();
		String tex = ParticlePart.textures[part.getActive().texture];
		texture.displayString = (tex.equals("random") ? TextFormatting.ITALIC.toString() : "") + WordUtils.capitalize(tex);
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
		toServer();
	}

	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		textFields.forEach(g -> g.mouseClicked(mouseX, mouseY, mouseButton));
		toServer();
		super.mouseClicked(mouseX, mouseY, mouseButton);
	}

	@Override
	protected void actionPerformed(GuiButton button) throws IOException {
		if (button.id <= -10 && button.id > -30) {
			particles.get(part.activeParticle).enabled = true;
			part.activeParticle = button.id * -1 - 10;
			//			toServer();
			GuiDrawer.openGui(new GuiSpreader(part));
		} else if (button.id == -1)
			GuiDrawer.openGui(new GuiColor(this));
		else if (button.id == 0)
			part.getActive().variant = part.getActive().variant.next();
		else if (button.id == 1)
			part.getActive().red = part.getActive().red.next();
		else if (button.id == 2)
			part.getActive().ax = Axis.values()[(part.getActive().ax.ordinal() + 1) % Axis.values().length];
		else if (button.id == 3)
			part.getActive().texture = (part.getActive().texture + 1) % ParticlePart.textures.length;
		else if (button.id == 4)
			part.getActive().depth ^= true;
		else if (button.id == 5)
			part.getActive().collidable ^= true;
		else if (button.id == 6)
			part.getActive().reverse ^= true;
		toServer();
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
			buttonList.add(new GuiSlider(0, 7 + guiLeft, 7 + guiTop, 86, 20, "Red ", "", 0, 255, ColorHelper.getRed(parent.part.parDatas.get(parent.part.activeParticle).color), false, true));
			buttonList.add(new GuiSlider(1, 7 + guiLeft, 32 + guiTop, 86, 20, "Green ", "", 0, 255, ColorHelper.getGreen(parent.part.parDatas.get(parent.part.activeParticle).color), false, true));
			buttonList.add(new GuiSlider(2, 7 + guiLeft, 57 + guiTop, 86, 20, "Blue ", "", 0, 255, ColorHelper.getBlue(parent.part.parDatas.get(parent.part.activeParticle).color), false, true));
			buttonList.add(new GuiSlider(3, 7 + guiLeft, 83 + guiTop, 86, 20, "Alpha ", "", 0, 255, ColorHelper.getAlpha(parent.part.parDatas.get(parent.part.activeParticle).color), false, true));
		}

		@Override
		protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
			drawDefaultBackground();
			drawer.drawBackgroundTexture();
			super.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);
			drawer.drawColoredRectangle(7, 109, 86, 12, parent.part.getActive().color);
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
			parent.part.getActive().color = color;
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
				keyTyped('a', Keyboard.KEY_ESCAPE);
		}

		@Override
		public boolean doesGuiPauseGame() {
			return false;
		}

	}

}
