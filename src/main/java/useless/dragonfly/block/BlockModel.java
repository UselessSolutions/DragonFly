package useless.dragonfly.block;

import net.minecraft.core.block.BlockTransparent;
import net.minecraft.core.block.material.Material;
import net.minecraft.core.util.phys.AABB;
import net.minecraft.core.world.World;
import useless.dragonfly.model.block.processed.BlockCube;

import java.util.ArrayList;

public class BlockModel extends BlockTransparent {
	public useless.dragonfly.model.block.processed.BlockModel model;
	public BlockModel(String key, int id, Material material, useless.dragonfly.model.block.processed.BlockModel model) {
		super(key, id, material, false);
		this.model = model;
	}

	// Setting this to false also disables the game trying to push you out of the block
	@Override
	public boolean renderAsNormalBlock() {
		return false;
	}
	@Override
	public boolean canPlaceOnSurface() {
		return true;
	}
	public void getCollidingBoundingBoxes(World world, int x, int y, int z, AABB aabb, ArrayList aabbList) {
		for (BlockCube cube: model.blockCubes) {
			setBlockBounds(cube.xMin(), cube.yMin(), cube.zMin(), cube.xMax(), cube.yMax(), cube.zMax());
			super.getCollidingBoundingBoxes(world, x, y, z, aabb, aabbList);
		}
		this.setBlockBounds(0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f);
	}
}
