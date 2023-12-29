package useless.dragonfly.debug.block.metastates;

import net.minecraft.core.block.Block;
import net.minecraft.core.block.tag.BlockTags;
import net.minecraft.core.world.WorldSource;
import useless.dragonfly.model.blockstates.processed.MetaStateInterpreter;

import java.util.HashMap;

public class FenceMetaState extends MetaStateInterpreter {
	@Override
	public HashMap<String, String> getStateMap(WorldSource worldSource, int x, int y, int z, Block block, int meta) {
		HashMap<String, String> result = new HashMap<>();
		result.put("north", canConnect(worldSource.getBlock(x, y, z-1)) ? "true" : "false");
		result.put("east", canConnect(worldSource.getBlock(x+1, y, z)) ? "true" : "false");
		result.put("south", canConnect(worldSource.getBlock(x, y, z+1)) ? "true" : "false");
		result.put("west", canConnect(worldSource.getBlock(x-1, y, z)) ? "true" : "false");
		return result;
	}
	private boolean canConnect(Block block){
		if (block == null) return false;
		return block.hasTag(BlockTags.FENCES_CONNECT);
	}
}
