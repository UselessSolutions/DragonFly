package useless.dragonfly.debug.block.metastates;

import net.minecraft.core.block.Block;
import net.minecraft.core.world.WorldSource;
import useless.dragonfly.model.blockstates.processed.MetaStateInterpreter;

import java.util.HashMap;

public class BrewingMetaState extends MetaStateInterpreter {
	@Override
	public HashMap<String, String> getStateMap(WorldSource worldSource, int x, int y, int z, Block block, int meta) {
		HashMap<String, String> result = new HashMap<>();
		result.put("has_bottle_0", (meta & 0b1) == 0b1 ? "true" : "false");
		result.put("has_bottle_1", (meta & 0b10) == 0b10 ? "true" : "false");
		result.put("has_bottle_2", (meta & 0b100) == 0b100 ? "true" : "false");
		return result;
	}
}
