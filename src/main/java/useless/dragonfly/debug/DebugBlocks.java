package useless.dragonfly.debug;

import net.minecraft.core.block.Block;
import net.minecraft.core.block.material.Material;
import turniplabs.halplibe.helper.BlockBuilder;
import useless.dragonfly.debug.block.BlockModel;
import useless.dragonfly.helper.ModelHelper;
import useless.dragonfly.model.block.BlockModelDragonFly;

import static useless.dragonfly.DragonFly.MOD_ID;

public class DebugBlocks {
	public static final Block testBlock = new BlockBuilder(MOD_ID)
		.setBlockModel(new BlockModelDragonFly(ModelHelper.getOrCreateBlockModel(MOD_ID, "TestBlock.json")))
		.setTextures(3,1)
		.build(new BlockModel("testblock", 1000, Material.dirt, ModelHelper.getOrCreateBlockModel(MOD_ID, "TestBlock.json")));
	public static final Block testBlock2 = new BlockBuilder(MOD_ID)
		.setBlockModel(new BlockModelDragonFly(ModelHelper.getOrCreateBlockModel(MOD_ID, "TestBlock2.json")))
		.setTextures(13,13)
		.build(new BlockModel("testblock2", 1001, Material.dirt, ModelHelper.getOrCreateBlockModel(MOD_ID, "TestBlock2.json")));
	public static final Block testBlock3 = new BlockBuilder(MOD_ID)
		.setBlockModel(new BlockModelDragonFly(ModelHelper.getOrCreateBlockModel(MOD_ID, "TestBlock3.json")))
		.setTextures(4,0)
		.build(new BlockModel("testblock3", 1002, Material.dirt, ModelHelper.getOrCreateBlockModel(MOD_ID, "TestBlock3.json")));
	public static final Block testBlock5 = new BlockBuilder(MOD_ID)
		.setBlockModel(new BlockModelDragonFly(ModelHelper.getOrCreateBlockModel(MOD_ID, "beacon.json")))
		.setTextures(1,3)
		.build(new BlockModel("testblock5", 1004, Material.dirt, ModelHelper.getOrCreateBlockModel(MOD_ID, "beacon.json")));
	public static final Block testBlock6 = new BlockBuilder(MOD_ID)
		.setBlockModel(new BlockModelDragonFly(ModelHelper.getOrCreateBlockModel(MOD_ID, "directionPyramid.json")))
		.setTextures(1,3)
		.build(new BlockModel("testblock6", 1005, Material.dirt, ModelHelper.getOrCreateBlockModel(MOD_ID, "directionPyramid.json")));
	public static final Block testBlock7 = new BlockBuilder(MOD_ID)
		.setBlockModel(new BlockModelDragonFly(ModelHelper.getOrCreateBlockModel(MOD_ID, "stool.json")))
		.setTextures(1,3)
		.build(new BlockModel("testblock7", 1006, Material.dirt, ModelHelper.getOrCreateBlockModel(MOD_ID, "stool.json")));
	public static void init() {}
}
