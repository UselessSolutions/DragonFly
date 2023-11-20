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
	private static int blockId = 1000;
public static final Block testBlock = new BlockBuilder(MOD_ID)
	.setBlockModel(new BlockModelDragonFly(ModelHelper.getOrCreateBlockModel(MOD_ID, "block/TestBlock.json")))
	.setTextures(3,1)
	.build(new BlockModel("testblock" + blockId, blockId++, Material.dirt, ModelHelper.getOrCreateBlockModel(MOD_ID, "block/TestBlock.json")));
	public static final Block testBlock2 = new BlockBuilder(MOD_ID)
		.setBlockModel(new BlockModelDragonFly(ModelHelper.getOrCreateBlockModel(MOD_ID, "block/TestBlock2.json")))
		.setTextures(13,13)
		.build(new BlockModel("testblock" + blockId, blockId++, Material.dirt, ModelHelper.getOrCreateBlockModel(MOD_ID, "block/TestBlock2.json")));
	public static final Block testBlock3 = new BlockBuilder(MOD_ID)
		.setBlockModel(new BlockModelDragonFly(ModelHelper.getOrCreateBlockModel(MOD_ID, "block/TestBlock3.json")))
		.setTextures(4,0)
		.build(new BlockModel("testblock" + blockId, blockId++, Material.dirt, ModelHelper.getOrCreateBlockModel(MOD_ID, "block/TestBlock3.json")));
	public static final Block testBlock6 = new BlockBuilder(MOD_ID)
		.setBlockModel(new BlockModelDragonFly(ModelHelper.getOrCreateBlockModel(MOD_ID, "block/directionPyramid.json")))
		.setTextures(1,3)
		.build(new BlockModel("testblock" + blockId, blockId++, Material.dirt, ModelHelper.getOrCreateBlockModel(MOD_ID, "block/directionPyramid.json")));
	public static final Block testBlock7 = new BlockBuilder(MOD_ID)
		.setBlockModel(new BlockModelDragonFly(ModelHelper.getOrCreateBlockModel(MOD_ID, "block/stool.json")))
		.setTextures(1,3)
		.build(new BlockModel("testblock" + blockId, blockId++, Material.dirt, ModelHelper.getOrCreateBlockModel(MOD_ID, "block/stool.json")));
	public static final Block harris = new BlockBuilder(MOD_ID)
		.setBlockModel(new BlockModelDragonFly(ModelHelper.getOrCreateBlockModel(MOD_ID, "block/harris.json")))
		.setTextures(1,0)
		.build(new BlockModel("testblock" + blockId, blockId++, Material.dirt, ModelHelper.getOrCreateBlockModel(MOD_ID, "block/harris.json")));
	public static final Block lantern = new BlockBuilder(MOD_ID)
		.setBlockModel(new BlockModelDragonFly(ModelHelper.getOrCreateBlockModel(TextureRegistry.coreNamepaceId, "block/lantern.json")))
		.setTextures(1,0)
		.build(new BlockModel("testblock" + blockId, blockId++, Material.dirt, ModelHelper.getOrCreateBlockModel(TextureRegistry.coreNamepaceId, "block/lantern.json")));
	public static final Block lanternsoul = new BlockBuilder(MOD_ID)
		.setBlockModel(new BlockModelDragonFly(ModelHelper.getOrCreateBlockModel(TextureRegistry.coreNamepaceId, "block/soul_lantern.json")))
		.setTextures(1,0)
		.build(new BlockModel("testblock" + blockId, blockId++, Material.dirt, ModelHelper.getOrCreateBlockModel(TextureRegistry.coreNamepaceId, "block/soul_lantern.json")));
	public static final Block slope = new BlockBuilder(MOD_ID)
		.setBlockModel(new BlockModelDragonFly(ModelHelper.getOrCreateBlockModel(MOD_ID, "block/slope.json")))
		.setTextures(1,0)
		.build(new BlockModel("testblock" + blockId, blockId++, Material.dirt, ModelHelper.getOrCreateBlockModel(MOD_ID, "block/slope.json")));
	public static final Block camp = new BlockBuilder(MOD_ID)
		.setBlockModel(new BlockModelDragonFly(ModelHelper.getOrCreateBlockModel(TextureRegistry.coreNamepaceId, "block/campfire.json")))
		.setTextures(1,0)
		.build(new BlockModel("testblock" + blockId, blockId++, Material.dirt, ModelHelper.getOrCreateBlockModel(TextureRegistry.coreNamepaceId, "block/campfire.json")));

	public static void init() {}
}
