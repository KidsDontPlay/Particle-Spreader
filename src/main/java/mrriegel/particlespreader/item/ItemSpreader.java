package mrriegel.particlespreader.item;

import mrriegel.limelib.datapart.DataPartRegistry;
import mrriegel.limelib.item.CommonItem;
import mrriegel.particlespreader.ParticlePart;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.ItemHandlerHelper;

public class ItemSpreader extends CommonItem {

	public ItemSpreader() {
		super("spreader");
		setCreativeTab(CreativeTabs.DECORATIONS);
		setMaxStackSize(1);
	}

	@Override
	public void registerItem() {
		super.registerItem();
		DataPartRegistry.register("particlePart", ParticlePart.class);
	}

	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		ItemStack itemstack = player.getHeldItem(hand);
		if (!itemstack.isEmpty() && player.canPlayerEdit(pos, facing, itemstack)) {
			ParticlePart part = new ParticlePart();
			if (itemstack.hasTagCompound())
				part.readFromNBT(itemstack.getTagCompound());
			if (DataPartRegistry.get(worldIn).addDataPart(pos.offset(facing), part, false)) {
				if (!player.isCreative())
					player.setHeldItem(hand, ItemHandlerHelper.copyStackWithSize(itemstack, itemstack.getCount() - 1));
				return EnumActionResult.SUCCESS;
			} else {
				return EnumActionResult.FAIL;
			}
		} else {
			return EnumActionResult.FAIL;
		}
	}

	public static enum ParticleVariant {
		NORMAL, CIRCLE, SQUARE, SPIRAL, EXPLOSION;
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
