package org.useless.dragonfly.model.newmodels;

import net.minecraft.client.render.stitcher.IconCoordinate;
import net.minecraft.core.block.Block;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.world.WorldSource;
import org.useless.dragonfly.model.newmodels.generic.StaticModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BlockModelStandardBTA<T extends Block> extends BlockModelGenericBTA<T> {
	public final List<StaticModel> inventoryModels = new ArrayList<>();
	public final List<StaticModel> worldModels = new ArrayList<>();
	public BlockModelStandardBTA(Block block) {
		super(block);
	}
	public BlockModelStandardBTA(Block block, StaticModel model) {
		super(block);
		addInventoryModel(model);
		addWorldModel(model);
	}

	public BlockModelStandardBTA<?> addInventoryModel(StaticModel modelContainer){
		inventoryModels.add(modelContainer);
		return this;
	}

	public BlockModelStandardBTA<?> addWorldModel(StaticModel modelContainer){
		worldModels.add(modelContainer);
		return this;
	}

	@Override
	public IconCoordinate getBlockOverbrightTextureFromSideAndMeta(Side side, int data) {
		return null;
	}


	@Override
	public void setWorldModel(ModelContainer modelContainer, WorldSource worldSource, int x, int y, int z, int meta) {
		for (StaticModel worldModel : worldModels) {
			modelContainer.addModel(worldModel);
		}
	}

	@Override
	public void setInventoryModel(ModelContainer modelContainer, int meta) {
		for (StaticModel inventoryModel : inventoryModels) {
			modelContainer.addModel(inventoryModel);
		}
	}
	public static Random getRandomFromPos(int x, int y, int z){
		Random rand = new Random(0);
		long l1 = rand.nextLong() / 2L * 2L + 1L;
		long l2 = rand.nextLong() / 2L * 2L + 1L;
		long l3 = rand.nextLong() / 2L * 2L + 1L;
		rand.setSeed((long)x * l1 + (long)z * l2 + y * l3);
		return rand;
	}
}
