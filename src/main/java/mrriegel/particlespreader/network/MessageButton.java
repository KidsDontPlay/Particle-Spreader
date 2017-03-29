package mrriegel.particlespreader.network;

import mrriegel.limelib.helper.ParticleHelper;
import mrriegel.limelib.network.AbstractMessage;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.relauncher.Side;

public class MessageButton extends AbstractMessage<MessageButton> {

	public static final int SMELT = 0;
	public static final int TELE = 1;

	public MessageButton() {
	}

	public MessageButton(BlockPos pos, int id) {
		nbt.setLong("pos", pos.toLong());
		nbt.setInteger("id", id);
	}

	@Override
	public void handleMessage(EntityPlayer player, NBTTagCompound nbt, Side side) {
		BlockPos pos = BlockPos.fromLong(nbt.getLong("pos"));
		switch (nbt.getInteger("id")) {
		case SMELT:
			for (Vec3d vec : ParticleHelper.getVecsForBlock(pos, 5))
				player.world.spawnParticle(EnumParticleTypes.FLAME, vec.xCoord, vec.yCoord, vec.zCoord, 0, 0.02, 0);
			break;
		case TELE:
			for (Vec3d vec : ParticleHelper.getVecsForBlock(pos, 40))
				player.world.spawnParticle(EnumParticleTypes.PORTAL, vec.xCoord, vec.yCoord, vec.zCoord, 0, -0.22, 0);
			break;
		}
	}

}
