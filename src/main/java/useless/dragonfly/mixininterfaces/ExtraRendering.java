package useless.dragonfly.mixininterfaces;

import net.minecraft.core.block.Block;
import useless.dragonfly.model.BlockBenchModel;

public interface ExtraRendering {
	boolean renderModelNormal(BlockBenchModel model, Block block, int x, int y, int z);
}
