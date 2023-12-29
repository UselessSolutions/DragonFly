package useless.dragonfly.debug.block.metastates;

import net.minecraft.core.block.Block;
import net.minecraft.core.world.WorldSource;
import useless.dragonfly.model.blockstates.processed.MetaStateInterpreter;

import java.util.HashMap;

public class BookshelfMetaState extends MetaStateInterpreter {
	@Override
	public HashMap<String, String> getStateMap(WorldSource worldSource, int x, int y, int z, Block block, int meta) {
		int hRotation = meta & 0b11;
		HashMap<String, String> result = new HashMap<>();
		result.put("facing", new String[]{"east", "west", "south", "north"}[hRotation]);
		result.put("slot_0_occupied", (meta & 0b100) == 0b100 ? "true" : "false");
		result.put("slot_1_occupied", (meta & 0b1000) == 0b1000 ? "true" : "false");
		result.put("slot_2_occupied", (meta & 0b10000) == 0b10000 ? "true" : "false");
		result.put("slot_3_occupied", (meta & 0b100000) == 0b100000 ? "true" : "false");
		result.put("slot_4_occupied", (meta & 0b1000000) == 0b1000000 ? "true" : "false");
		result.put("slot_5_occupied", (meta & 0b10000000) == 0b10000000 ? "true" : "false");
		return result;
	}
}
