package useless.dragonfly.mixininterfaces;

import net.minecraft.core.block.Block;
import useless.dragonfly.model.block.processed.BlockModel;

public interface ExtraRendering {
	boolean renderModelNormal(BlockModel model, Block block, int x, int y, int z);
	boolean renderModelNoCulling(BlockModel model, Block block, int x, int y, int z);
	boolean renderModelBlockUsingTexture(BlockModel model, Block block, int x, int y, int z, int textureIndex);
}
