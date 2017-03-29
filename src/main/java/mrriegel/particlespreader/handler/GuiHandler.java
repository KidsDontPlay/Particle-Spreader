package mrriegel.particlespreader.handler;

import mrriegel.limelib.datapart.DataPartRegistry;
import mrriegel.particlespreader.gui.GuiSpreader;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class GuiHandler implements IGuiHandler {

	@Override
	public Object getServerGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
		return null;
	}

	@Override
	public Object getClientGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
		return new GuiSpreader(DataPartRegistry.get(world).getDataPart(new BlockPos(x, y, z)));
	}

}
