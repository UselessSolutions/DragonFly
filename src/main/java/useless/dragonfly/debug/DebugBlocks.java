package useless.dragonfly.debug;

import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockStairs;
import net.minecraft.core.block.material.Material;
import turniplabs.halplibe.helper.BlockBuilder;
import useless.dragonfly.debug.block.BlockModel;
import useless.dragonfly.helper.ModelHelper;
import useless.dragonfly.model.block.BlockModelDragonFly;
import useless.dragonfly.registries.TextureRegistry;
import useless.dragonfly.utilities.Utilities;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import static useless.dragonfly.DragonFly.MOD_ID;

public class DebugBlocks {
	private static int blockId = 1000;
public static final Block testBlock = new BlockBuilder(MOD_ID)
	.setBlockModel(new BlockModelDragonFly(ModelHelper.getOrCreateBlockModel(MOD_ID, "block/TestBlock.json")))
	.build(new BlockModel("testblock" + blockId, blockId++, Material.dirt, ModelHelper.getOrCreateBlockModel(MOD_ID, "block/TestBlock.json")));
	public static final Block testBlock2 = new BlockBuilder(MOD_ID)
		.setBlockModel(new BlockModelDragonFly(ModelHelper.getOrCreateBlockModel(MOD_ID, "block/TestBlock2.json")))
		.build(new BlockModel("testblock" + blockId, blockId++, Material.dirt, ModelHelper.getOrCreateBlockModel(MOD_ID, "block/TestBlock2.json")));
	public static final Block testBlock3 = new BlockBuilder(MOD_ID)
		.setBlockModel(new BlockModelDragonFly(ModelHelper.getOrCreateBlockModel(MOD_ID, "block/TestBlock3.json")))
		.build(new BlockModel("testblock" + blockId, blockId++, Material.dirt, ModelHelper.getOrCreateBlockModel(MOD_ID, "block/TestBlock3.json")));
	public static final Block testBlock6 = new BlockBuilder(MOD_ID)
		.setBlockModel(new BlockModelDragonFly(ModelHelper.getOrCreateBlockModel(MOD_ID, "block/directionPyramid.json")))
		.build(new BlockModel("testblock" + blockId, blockId++, Material.dirt, ModelHelper.getOrCreateBlockModel(MOD_ID, "block/directionPyramid.json")));
	public static final Block testBlock7 = new BlockBuilder(MOD_ID)
		.setBlockModel(new BlockModelDragonFly(ModelHelper.getOrCreateBlockModel(MOD_ID, "block/stool.json")))
		.build(new BlockModel("testblock" + blockId, blockId++, Material.dirt, ModelHelper.getOrCreateBlockModel(MOD_ID, "block/stool.json")));
	public static final Block harris = new BlockBuilder(MOD_ID)
		.setBlockModel(new BlockModelDragonFly(ModelHelper.getOrCreateBlockModel(MOD_ID, "block/harris.json")))
		.build(new BlockModel("testblock" + blockId, blockId++, Material.dirt, ModelHelper.getOrCreateBlockModel(MOD_ID, "block/harris.json")));
	public static final Block slope = new BlockBuilder(MOD_ID)
		.setBlockModel(new BlockModelDragonFly(ModelHelper.getOrCreateBlockModel(MOD_ID, "block/slope.json")))
		.build(new BlockModel("testblock" + blockId, blockId++, Material.dirt, ModelHelper.getOrCreateBlockModel(MOD_ID, "block/slope.json")));
	public static final Block stairs = new BlockBuilder(MOD_ID)
		.setBlockModel(new BlockModelDragonFly(ModelHelper.getOrCreateBlockModel(TextureRegistry.coreNamepaceId, "block/cut_copper_stairs.json"),
			ModelHelper.getOrCreateBlockState(MOD_ID, "test_stairs.json"), null, true, 0.25f))
		.build(new BlockStairs(Block.dirt,blockId++));

	public static void init() {
//		try {
//			for (String string : getResourceFiles("assets/minecraft/model/block/")) {
//				System.out.println(string);
//				if (string.contains("cauldron")){
//					new BlockBuilder(MOD_ID)
//						.setBlockModel(new BlockModelDragonFly(ModelHelper.getOrCreateBlockModel(TextureRegistry.coreNamepaceId, "block/" + string)))
//						.setBlockColor(new BlockColorWater())
//						.build(new BlockModel(string.replace(".json", ""), blockId++, Material.dirt, ModelHelper.getOrCreateBlockModel(TextureRegistry.coreNamepaceId, "block/" + string)));
//				} else {
//					new BlockBuilder(MOD_ID)
//						.setBlockModel(new BlockModelDragonFly(ModelHelper.getOrCreateBlockModel(TextureRegistry.coreNamepaceId, "block/" + string)))
//						.build(new BlockModel(string.replace(".json", ""), blockId++, Material.dirt, ModelHelper.getOrCreateBlockModel(TextureRegistry.coreNamepaceId, "block/" + string)));
//				}
//
//				System.out.println(string + " created");
//			}
//			for (String string : getResourceFiles("assets/minecraft/blockstates/")) {
//				System.out.println(string);
//				try {
//					System.out.println(ModelHelper.getOrCreateBlockState(TextureRegistry.coreNamepaceId, string));
//				}
//				catch (Exception e){
//					System.out.println(e);
//				}
//			}
//		} catch (IOException e) {
//			throw new RuntimeException(e);
//		}

	}
	private static List<String> getResourceFiles(String path) throws IOException {
		List<String> filenames = new ArrayList<>();

		try (
			InputStream in = Utilities.getResourceAsStream(path);
			BufferedReader br = new BufferedReader(new InputStreamReader(in))) {
			String resource;

			while ((resource = br.readLine()) != null) {
				filenames.add(resource);
			}
		}

		return filenames;
	}
}
