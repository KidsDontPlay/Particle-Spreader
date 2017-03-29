package mrriegel.particlespreader.gui;

import mrriegel.limelib.datapart.DataPart;
import mrriegel.limelib.gui.CommonGuiScreen;

public class GuiSpreader extends CommonGuiScreen {
	DataPart part;

	public GuiSpreader(DataPart part) {
		this.part = part;
		ySize = 190;
		xSize = 230;
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		drawer.drawBackgroundTexture();
		super.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);
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
	}

	@Override
	public boolean doesGuiPauseGame() {
		return !super.doesGuiPauseGame();
	}

}
