package useless.dragonfly.debug;

import net.minecraft.client.render.block.color.BlockColorWater;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockStairs;
import net.minecraft.core.block.material.Material;
import turniplabs.halplibe.helper.BlockBuilder;
import useless.dragonfly.debug.block.BlockModel;
import useless.dragonfly.debug.block.metastates.StairsMetaStateInterpreter;
import useless.dragonfly.helper.ModelHelper;
import useless.dragonfly.model.block.BlockModelDragonFly;
import useless.dragonfly.utilities.NamespaceId;
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
	.setBlockModel(new BlockModelDragonFly(ModelHelper.getOrCreateBlockModel(MOD_ID, "block/testblock.json")))
	.build(new BlockModel("testblock" + blockId, blockId++, Material.dirt, ModelHelper.getOrCreateBlockModel(MOD_ID, "block/testblock.json")));
	public static final Block testBlock2 = new BlockBuilder(MOD_ID)
		.setBlockModel(new BlockModelDragonFly(ModelHelper.getOrCreateBlockModel(MOD_ID, "block/testblock2.json")))
		.build(new BlockModel("testblock" + blockId, blockId++, Material.dirt, ModelHelper.getOrCreateBlockModel(MOD_ID, "block/testblock2.json")));
	public static final Block testBlock3 = new BlockBuilder(MOD_ID)
		.setBlockModel(new BlockModelDragonFly(ModelHelper.getOrCreateBlockModel(MOD_ID, "block/testblock3.json")))
		.build(new BlockModel("testblock" + blockId, blockId++, Material.dirt, ModelHelper.getOrCreateBlockModel(MOD_ID, "block/testBlock3.json")));
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
		.setBlockModel(new BlockModelDragonFly(ModelHelper.getOrCreateBlockModel(NamespaceId.coreNamespaceId, "block/cut_copper_stairs.json"),
			ModelHelper.getOrCreateBlockState(MOD_ID, "test_stairs.json"), new StairsMetaStateInterpreter(), true, 0.25f))
		.build(new BlockStairs(Block.dirt,blockId++)).withLitInteriorSurface(true);
	public static final Block trel = new BlockBuilder(MOD_ID)
		.setBlockModel(new BlockModelDragonFly(ModelHelper.getOrCreateBlockModel(MOD_ID, "block/bean_trellis_bottom_0.json")))
		.build(new BlockModel("trel" + blockId, blockId++, Material.dirt, ModelHelper.getOrCreateBlockModel(MOD_ID, "block/bean_trellis_bottom_0.json")));
	public static final Block trel1 = new BlockBuilder(MOD_ID)
		.setBlockModel(new BlockModelDragonFly(ModelHelper.getOrCreateBlockModel(MOD_ID, "block/bean_trellis_bottom_1.json")))
		.build(new BlockModel("trel" + blockId, blockId++, Material.dirt, ModelHelper.getOrCreateBlockModel(MOD_ID, "block/bean_trellis_bottom_1.json")));
	public static final Block trel2 = new BlockBuilder(MOD_ID)
		.setBlockModel(new BlockModelDragonFly(ModelHelper.getOrCreateBlockModel(MOD_ID, "block/bean_trellis_bottom_2.json")))
		.build(new BlockModel("trel" + blockId, blockId++, Material.dirt, ModelHelper.getOrCreateBlockModel(MOD_ID, "block/bean_trellis_bottom_2.json")));
	public static final Block sieve = new BlockBuilder(MOD_ID)
		.setBlockModel(new BlockModelDragonFly(ModelHelper.getOrCreateBlockModel(MOD_ID, "block/wooden_sieve.json")))
		.build(new BlockModel("sieve" + blockId, blockId++, Material.dirt, ModelHelper.getOrCreateBlockModel(MOD_ID, "block/wooden_sieve.json")));
	public static final Block dirTest = new BlockBuilder(MOD_ID)
		.setBlockModel(new BlockModelDragonFly(ModelHelper.getOrCreateBlockModel(MOD_ID, "block/test_block.json")))
		.build(new BlockModel("sieve" + blockId, blockId++, Material.dirt, ModelHelper.getOrCreateBlockModel(MOD_ID, "block/test_block.json")));

	public static void init() {
		try {
			for (String string : getResourceFiles("assets/minecraft/model/block/")) {
				System.out.println(string);
				if (string.contains("cauldron")){
					new BlockBuilder(MOD_ID)
						.setBlockModel(new BlockModelDragonFly(ModelHelper.getOrCreateBlockModel(NamespaceId.coreNamespaceId, "block/" + string)))
						.setBlockColor(new BlockColorWater())
						.build(new BlockModel(string.replace(".json", ""), blockId++, Material.dirt, ModelHelper.getOrCreateBlockModel(NamespaceId.coreNamespaceId, "block/" + string)));
				} else {
					new BlockBuilder(MOD_ID)
						.setBlockModel(new BlockModelDragonFly(ModelHelper.getOrCreateBlockModel(NamespaceId.coreNamespaceId, "block/" + string)))
						.setHardness(1)
						.build(new BlockModel(string.replace(".json", ""), blockId++, Material.dirt, ModelHelper.getOrCreateBlockModel(NamespaceId.coreNamespaceId, "block/" + string)));
				}

				System.out.println(string + " created");
			}
			for (String string : getResourceFiles("assets/minecraft/blockstates/")) {
				System.out.println(string);
				try {
					System.out.println(ModelHelper.getOrCreateBlockState(NamespaceId.coreNamespaceId, string));
				}
				catch (Exception e){
					System.out.println(e);
				}
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

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
