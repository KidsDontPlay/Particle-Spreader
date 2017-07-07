package mrriegel.particlespreader.network;

import mrriegel.limelib.datapart.DataPart;
import mrriegel.limelib.datapart.DataPartRegistry;
import mrriegel.limelib.helper.NBTHelper;
import mrriegel.limelib.network.AbstractMessage;
import mrriegel.particlespreader.ParticlePart;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.relauncher.Side;

public class MessageToServer extends AbstractMessage {

	public MessageToServer() {
	}

	public MessageToServer(DataPart part) {
		part.writeToNBT(nbt);
		NBTHelper.set(nbt, "POSS", part.getPos());
	}

	@Override
	public void handleMessage(EntityPlayer player, NBTTagCompound nbt, Side side) {
		BlockPos pos = NBTHelper.get(nbt, "POSS", BlockPos.class);
		ParticlePart part = (ParticlePart) DataPartRegistry.get(player.world).getDataPart(pos);
		if (part != null)
			part.readFromNBT(nbt);
	}

}
