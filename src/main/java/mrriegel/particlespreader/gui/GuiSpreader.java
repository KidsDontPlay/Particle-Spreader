package mrriegel.particlespreader.gui;

import java.awt.Color;
import java.io.IOException;
import java.util.Collections;
import java.util.Set;

import mrriegel.limelib.gui.CommonGuiScreen;
import mrriegel.limelib.gui.button.GuiButtonSimple;
import mrriegel.limelib.network.PacketHandler;
import mrriegel.particlespreader.item.ItemSpreader.ParticlePart;
import mrriegel.particlespreader.network.MessageToServer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.util.EnumFacing.Axis;
import net.minecraft.util.math.MathHelper;

import org.apache.commons.lang3.text.WordUtils;

import com.google.common.base.Predicate;
import com.google.common.collect.Sets;

public class GuiSpreader extends CommonGuiScreen {
	ParticlePart part;
	GuiButton particle, redstone, axis, texture;
	GuiTextField minXp, minYp, minZp, maxXp, maxYp, maxZp;
	GuiTextField minXm, minYm, minZm, maxXm, maxYm, maxZm;

	Set<GuiTextField> textFields = Sets.newHashSet();

	private int textfieldid = 0;
	//	private static Set<Integer> set = Sets.newHashSet((int) 'd', (int) 'f', (int) 'D', (int) 'F');
	private static Set<Character> set = Sets.newHashSet('d', 'f', 'D', 'F');
	private static Predicate<String> pred = s -> {
		try {
			Double.parseDouble(s);
			return !s.chars().anyMatch(i -> /*set.contains(i)*/set.stream().anyMatch(c -> c == i));
		} catch (Exception e) {
			return false;
		}
	};

	public GuiSpreader(ParticlePart part) {
		this.part = part;
		ySize = 190;
		xSize = 260;
	}

	private void toServer() {
		updatePart();
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
	}

	private double toDouble(String s, double min) {
		if (s.isEmpty())
			return 0.;
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

	@Override
	public void initGui() {
		super.initGui();
		buttonList.add(particle = new GuiButtonSimple(0, 5 + guiLeft, 6 + guiTop, 55, 12, "", Collections.singletonList("Particle")));
		buttonList.add(redstone = new GuiButtonSimple(1, 64 + guiLeft, 6 + guiTop, 55, 12, "", Collections.singletonList("Redstone")));
		buttonList.add(axis = new GuiButtonSimple(2, 124 + guiLeft, 6 + guiTop, 55, 12, "", Collections.singletonList("Axis")));
		buttonList.add(texture = new GuiButtonSimple(3, 184 + guiLeft, 6 + guiTop, 55, 12, "", Collections.singletonList("Texture")));
		//		elementList.add(new Checkbox(0, 0 + guiLeft, 0 + guiTop, drawer));
		minXp = getTextField(19, 38, part.minXPos + "");
		maxXp = getTextField(67, 38, part.maxXPos + "");
		minYp = getTextField(19, 54, part.minYPos + "");
		maxYp = getTextField(67, 54, part.maxYPos + "");
		minZp = getTextField(19, 70, part.minZPos + "");
		maxZp = getTextField(67, 70, part.maxZPos + "");
		minXm = getTextField(19, 108, part.minXMotion + "");
		maxXm = getTextField(67, 108, part.maxXMotion + "");
		minYm = getTextField(19, 124, part.minYMotion + "");
		maxYm = getTextField(67, 124, part.maxYMotion + "");
		minZm = getTextField(19, 140, part.minZMotion + "");
		maxZm = getTextField(67, 140, part.maxZMotion + "");
	}

	private GuiTextField getTextField(int x, int y, String s) {
		GuiTextField text = new GuiTextField(textfieldid++, fontRenderer, x + guiLeft, y + guiTop, 44, 12);
		text.setMaxStringLength(5);
		text.setText(s);
		text.setTextColor(-1);
		text.setValidator(pred);
		textFields.add(text);
		return text;
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		drawDefaultBackground();
		drawer.drawBackgroundTexture();
		super.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);
		//position
		drawer.drawFrame(minXp.xPosition - 12 - guiLeft, minXp.yPosition - 4 - guiTop, 108, 51, 1, Color.gray.getRGB());
		fontRenderer.drawString("Position", minXp.xPosition - 7, minXp.yPosition - 13, Color.darkGray.getRGB());
		fontRenderer.drawString("X", minXp.xPosition - 9, minXp.yPosition + 2, Color.darkGray.getRGB());
		fontRenderer.drawString("Y", minYp.xPosition - 9, minYp.yPosition + 2, Color.darkGray.getRGB());
		fontRenderer.drawString("Z", minZp.xPosition - 9, minZp.yPosition + 2, Color.darkGray.getRGB());
		//motion
		drawer.drawFrame(minXm.xPosition - 12 - guiLeft, minXm.yPosition - 4 - guiTop, 108, 51, 1, Color.gray.getRGB());
		fontRenderer.drawString("Motion", minXm.xPosition - 7, minXm.yPosition - 13, Color.darkGray.getRGB());
		fontRenderer.drawString("X", minXm.xPosition - 9, minXm.yPosition + 2, Color.darkGray.getRGB());
		fontRenderer.drawString("Y", minYm.xPosition - 9, minYm.yPosition + 2, Color.darkGray.getRGB());
		fontRenderer.drawString("Z", minZm.xPosition - 9, minZm.yPosition + 2, Color.darkGray.getRGB());

		textFields.forEach(GuiTextField::drawTextBox);
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		super.drawGuiContainerForegroundLayer(mouseX, mouseY);
	}

	@Override
	public void updateScreen() {
		super.updateScreen();
		if (part == null)
			mc.player.closeScreen();
		particle.displayString = part.variant.toString();
		redstone.displayString = part.red.toString();
		axis.displayString = part.ax.getName().toUpperCase();
		texture.displayString = WordUtils.capitalize(ParticlePart.textures[part.texture]);
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
		if (button.id == 0)
			part.variant = part.variant.next();
		else if (button.id == 1)
			part.red = part.red.next();
		else if (button.id == 2)
			part.ax = Axis.values()[(part.ax.ordinal() + 1) % Axis.values().length];
		else if (button.id == 3)
			part.texture = (part.texture + 1) % ParticlePart.textures.length;
		toServer();
	}

	@Override
	public boolean doesGuiPauseGame() {
		return false;
	}

}
