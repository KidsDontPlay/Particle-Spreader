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
	public EnumActionResult onItemUse(ItemStack itemstack, EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		if (itemstack != null && player.canPlayerEdit(pos, facing, itemstack)) {
			ParticlePart part = new ParticlePart();
			if (itemstack.hasTagCompound())
				part.readFromNBT(itemstack.getTagCompound());
			if (DataPartRegistry.get(worldIn).addDataPart(pos.offset(facing), part, false)) {
				if (!player.isCreative())
					player.setHeldItem(hand, ItemHandlerHelper.copyStackWithSize(itemstack, itemstack.stackSize - 1));
				return EnumActionResult.SUCCESS;
			} else {
				return EnumActionResult.FAIL;
			}
		} else {
			return EnumActionResult.FAIL;
		}
	}

}
