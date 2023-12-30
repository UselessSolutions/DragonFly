package useless.dragonfly.debug.block;

import net.minecraft.core.block.Block;
import net.minecraft.core.block.material.Material;
import net.minecraft.core.entity.EntityItem;
import net.minecraft.core.entity.EntityLiving;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.item.Item;
import net.minecraft.core.util.helper.Direction;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.world.World;

public class BlockRotatable extends Block {
	public BlockRotatable(String key, int id, Material material) {
		super(key, id, material);
	}
	@Override
	public void onBlockPlaced(World world, int x, int y, int z, Side side, EntityLiving entity, double sideHeight) {
		int meta = world.getBlockMetadata(x, y, z) & 0x11111100;
		Direction hRotation = entity.getHorizontalPlacementDirection(side);
		if (hRotation == Direction.NORTH) {
			meta |= 2;
		}
		if (hRotation == Direction.EAST) {
			meta |= 1;
		}
		if (hRotation == Direction.SOUTH) {
			meta |= 3;
		}
		if (hRotation == Direction.WEST) {
			meta |= 0;
		}
		world.setBlockMetadataWithNotify(x, y, z, meta);
	}
	public void onBlockRemoved(World world, int x, int y, int z, int data) {
		boolean book0 = (data & 0b100) == 0b100;
		boolean book1 = (data & 0b1000) == 0b1000;
		boolean book2 = (data & 0b10000) == 0b10000;
		boolean book3 = (data & 0b100000) == 0b100000;
		boolean book4 = (data & 0b1000000) == 0b1000000;
		boolean book5 = (data & 0b10000000) == 0b10000000;
		if (book0){
			dropBook(world, x, y, z);
		}
		if (book1){
			dropBook(world, x, y, z);
		}
		if (book2){
			dropBook(world, x, y, z);
		}
		if (book3){
			dropBook(world, x, y, z);
		}
		if (book4){
			dropBook(world, x, y, z);
		}
		if (book5){
			dropBook(world, x, y, z);
		}
	}
	@Override
	public boolean blockActivated(World world, int x, int y, int z, EntityPlayer player) {
		if (world.isClientSide) {
			return true;
		}
		if (player.getHeldItem() != null && player.getHeldItem().getItem() == Item.book){
			if (addBook(world, x, y, z)){
				player.getHeldItem().consumeItem(player);
			}
		} else {
			if (removeBook(world, x, y, z)){
				player.inventory.insertItem(Item.book.getDefaultStack(), true);
			}
		}
		return true;
	}
	private boolean addBook(World world, int x, int y, int z){
		int meta = world.getBlockMetadata(x, y, z);
		boolean book0 = (meta & 0b100) == 0b100;
		boolean book1 = (meta & 0b1000) == 0b1000;
		boolean book2 = (meta & 0b10000) == 0b10000;
		boolean book3 = (meta & 0b100000) == 0b100000;
		boolean book4 = (meta & 0b1000000) == 0b1000000;
		boolean book5 = (meta & 0b10000000) == 0b10000000;
		if (!book0){
			meta |= 0b100;
			world.setBlockMetadataWithNotify(x, y, z, meta);
			return true;
		}
		if (!book1){
			meta |= 0b1000;
			world.setBlockMetadataWithNotify(x, y, z, meta);
			return true;
		}
		if (!book2){
			meta |= 0b10000;
			world.setBlockMetadataWithNotify(x, y, z, meta);
			return true;
		}
		if (!book3){
			meta |= 0b100000;
			world.setBlockMetadataWithNotify(x, y, z, meta);
			return true;
		}
		if (!book4){
			meta |= 0b1000000;
			world.setBlockMetadataWithNotify(x, y, z, meta);
			return true;
		}
		if (!book5){
			meta |= 0b10000000;
			world.setBlockMetadataWithNotify(x, y, z, meta);
			return true;
		}
		return false;
	}
	private boolean removeBook(World world, int x, int y, int z){
		int meta = world.getBlockMetadata(x, y, z);
		boolean book0 = (meta & 0b100) == 0b100;
		boolean book1 = (meta & 0b1000) == 0b1000;
		boolean book2 = (meta & 0b10000) == 0b10000;
		boolean book3 = (meta & 0b100000) == 0b100000;
		boolean book4 = (meta & 0b1000000) == 0b1000000;
		boolean book5 = (meta & 0b10000000) == 0b10000000;
		if (book0){
			meta &= 0b11111011;
			world.setBlockMetadataWithNotify(x, y, z, meta);
			return true;
		}
		if (book1){
			meta &= 0b11110111;
			world.setBlockMetadataWithNotify(x, y, z, meta);
			return true;
		}
		if (book2){
			meta &= 0b11101111;
			world.setBlockMetadataWithNotify(x, y, z, meta);
			return true;
		}
		if (book3){
			meta &= 0b11011111;
			world.setBlockMetadataWithNotify(x, y, z, meta);
			return true;
		}
		if (book4){
			meta &= 0b10111111;
			world.setBlockMetadataWithNotify(x, y, z, meta);
			return true;
		}
		if (book5){
			meta &= 0b01111111;
			world.setBlockMetadataWithNotify(x, y, z, meta);
			return true;
		}
		return false;
	}
	private void dropBook(World world, int x, int y, int z){
		EntityItem item = world.dropItem(x, y, z, Item.book.getDefaultStack());
		item.xd *= 0.5;
		item.yd *= 0.5;
		item.zd *= 0.5;
		item.delayBeforeCanPickup = 0;
	}
}
