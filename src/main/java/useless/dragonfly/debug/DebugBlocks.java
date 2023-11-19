package useless.dragonfly.debug;

import net.minecraft.core.block.Block;
import net.minecraft.core.block.material.Material;
import turniplabs.halplibe.helper.BlockBuilder;
import useless.dragonfly.debug.block.BlockModel;
import useless.dragonfly.helper.ModelHelper;
import useless.dragonfly.model.block.BlockModelDragonFly;
import useless.dragonfly.registries.TextureRegistry;

import static useless.dragonfly.DragonFly.MOD_ID;

public class DebugBlocks {
	public static final Block testBlock = new BlockBuilder(MOD_ID)
		.setBlockModel(new BlockModelDragonFly(ModelHelper.getOrCreateBlockModel(MOD_ID, "block/TestBlock.json")))
		.setTextures(3,1)
		.build(new BlockModel("testblock", 1000, Material.dirt, ModelHelper.getOrCreateBlockModel(MOD_ID, "block/TestBlock.json")));
	public static final Block testBlock2 = new BlockBuilder(MOD_ID)
		.setBlockModel(new BlockModelDragonFly(ModelHelper.getOrCreateBlockModel(MOD_ID, "block/TestBlock2.json")))
		.setTextures(13,13)
		.build(new BlockModel("testblock2", 1001, Material.dirt, ModelHelper.getOrCreateBlockModel(MOD_ID, "block/TestBlock2.json")));
	public static final Block testBlock3 = new BlockBuilder(MOD_ID)
		.setBlockModel(new BlockModelDragonFly(ModelHelper.getOrCreateBlockModel(MOD_ID, "block/TestBlock3.json")))
		.setTextures(4,0)
		.build(new BlockModel("testblock3", 1002, Material.dirt, ModelHelper.getOrCreateBlockModel(MOD_ID, "block/TestBlock3.json")));
	public static final Block testBlock5 = new BlockBuilder(MOD_ID)
		.setBlockModel(new BlockModelDragonFly(ModelHelper.getOrCreateBlockModel(TextureRegistry.coreNamepaceId, "block/beacon.json")))
		.setTextures(1,3)
		.build(new BlockModel("testblock5", 1004, Material.dirt, ModelHelper.getOrCreateBlockModel(TextureRegistry.coreNamepaceId, "block/beacon.json")));
	public static final Block testBlock6 = new BlockBuilder(MOD_ID)
		.setBlockModel(new BlockModelDragonFly(ModelHelper.getOrCreateBlockModel(MOD_ID, "block/directionPyramid.json")))
		.setTextures(1,3)
		.build(new BlockModel("testblock6", 1005, Material.dirt, ModelHelper.getOrCreateBlockModel(MOD_ID, "block/directionPyramid.json")));
	public static final Block testBlock7 = new BlockBuilder(MOD_ID)
		.setBlockModel(new BlockModelDragonFly(ModelHelper.getOrCreateBlockModel(MOD_ID, "block/stool.json")))
		.setTextures(1,3)
		.build(new BlockModel("testblock7", 1006, Material.dirt, ModelHelper.getOrCreateBlockModel(MOD_ID, "block/stool.json")));
	public static final Block harris = new BlockBuilder(MOD_ID)
		.setBlockModel(new BlockModelDragonFly(ModelHelper.getOrCreateBlockModel(MOD_ID, "block/harris.json")))
		.setTextures(1,0)
		.build(new BlockModel("testblock8", 1007, Material.dirt, ModelHelper.getOrCreateBlockModel(MOD_ID, "block/harris.json")));
	public static final Block parentModelTest = new BlockBuilder(MOD_ID)
		.setBlockModel(new BlockModelDragonFly(ModelHelper.getOrCreateBlockModel(TextureRegistry.coreNamepaceId, "block/black_wool.json")))
		.setTextures(0,0)
		.build(new BlockModel("parentModelTest", 1008, Material.dirt, ModelHelper.getOrCreateBlockModel(TextureRegistry.coreNamepaceId, "block/black_wool.json")));
	public static final Block anvil = new BlockBuilder(MOD_ID)
		.setBlockModel(new BlockModelDragonFly(ModelHelper.getOrCreateBlockModel(TextureRegistry.coreNamepaceId, "block/anvil.json")))
		.setTextures(0,0)
		.build(new BlockModel("anvilTest", 1009, Material.dirt, ModelHelper.getOrCreateBlockModel(TextureRegistry.coreNamepaceId, "block/anvil.json")));
	public static final Block shriek = new BlockBuilder(MOD_ID)
		.setBlockModel(new BlockModelDragonFly(ModelHelper.getOrCreateBlockModel(TextureRegistry.coreNamepaceId, "block/sculk_shrieker.json")))
		.setTextures(0,0)
		.build(new BlockModel("shrieker", 1010, Material.dirt, ModelHelper.getOrCreateBlockModel(TextureRegistry.coreNamepaceId, "block/sculk_shrieker.json")));
	public static final Block hopper = new BlockBuilder(MOD_ID)
		.setBlockModel(new BlockModelDragonFly(ModelHelper.getOrCreateBlockModel(TextureRegistry.coreNamepaceId, "block/hopper.json")))
		.setTextures(0,0)
		.build(new BlockModel("hopper", 1011, Material.dirt, ModelHelper.getOrCreateBlockModel(TextureRegistry.coreNamepaceId, "block/hopper.json")));
	public static void init() {}
}
