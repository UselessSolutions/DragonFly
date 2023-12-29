package useless.dragonfly.debug.item;

import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.world.World;

public class ItemDebugStick extends Item {
	public ItemDebugStick(String name, int id) {
		super(name,id);
	}
	@Override
	public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int blockX, int blockY, int blockZ, Side side, double xPlaced, double yPlaced) {
		int meta = world.getBlockMetadata(blockX, blockY, blockZ);
		int id = world.getBlockId(blockX, blockY, blockZ);
		if (player.isSneaking()){
			meta--;
		} else {
			meta++;
		}
		meta &= 0xFF;
		world.setBlockAndMetadataWithNotify(blockX, blockY, blockZ, id, meta);
		return true;
	}
}
