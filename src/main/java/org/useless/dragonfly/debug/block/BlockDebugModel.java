package org.useless.dragonfly.debug.block;

import net.minecraft.client.render.block.model.BlockModel;
import net.minecraft.client.render.block.model.BlockModelDispatcher;
import net.minecraft.core.block.BlockTransparent;
import net.minecraft.core.block.material.Material;
import net.minecraft.core.util.phys.AABB;
import net.minecraft.core.world.World;
import org.useless.dragonfly.model.block.BlockModelDragonFly;

import java.util.ArrayList;

public class BlockDebugModel extends BlockTransparent {
	public BlockDebugModel(String key, int id, Material material) {
		super(key, id, material);
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
	@Override
	public int getRenderBlockPass() {
		return 0;
	}
	@Override
	public void getCollidingBoundingBoxes(World world, int x, int y, int z, AABB aabb, ArrayList<AABB> aabbList) {
		BlockModel<?> model = BlockModelDispatcher.getInstance().getDispatch(this);
		if (!(model instanceof BlockModelDragonFly)) {
			super.getCollidingBoundingBoxes(world, x, y, z, aabb, aabbList);
			return;
		}
//		ModernBlockModel blockModel = ((BlockModelDragonFly)model).baseModel;
//		for (BlockCube cube: blockModel.blockCubes) {
//			setBlockBounds(cube.xMin(), cube.yMin(), cube.zMin(), cube.xMax(), cube.yMax(), cube.zMax());
//			super.getCollidingBoundingBoxes(world, x, y, z, aabb, aabbList);
//		}
//		this.setBlockBounds(0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f);
	}
}
