package org.useless.dragonfly.data.block.mojang.state;

import net.minecraft.core.block.Block;
import net.minecraft.core.world.WorldSource;

import java.util.HashMap;

public abstract class MetaStateInterpreter {
	public abstract HashMap<String, String> getStateMap(WorldSource worldSource, int x, int y, int z, Block<?> block, int meta);
}
