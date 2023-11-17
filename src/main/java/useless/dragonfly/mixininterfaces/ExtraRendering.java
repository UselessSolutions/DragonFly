package useless.dragonfly.mixininterfaces;

import net.minecraft.core.block.Block;
import useless.dragonfly.model.block.BlockBenchModel;

public interface ExtraRendering {
	boolean renderModelNormal(BlockBenchModel model, Block block, int x, int y, int z);
	boolean renderModelNoCulling(BlockBenchModel model, Block block, int x, int y, int z);
	boolean renderModelBlockUsingTexture(BlockBenchModel model, Block block, int x, int y, int z, int textureIndex);
}
