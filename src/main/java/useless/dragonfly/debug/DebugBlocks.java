package useless.dragonfly.debug;

import net.minecraft.client.render.block.color.BlockColorGrass;
import net.minecraft.client.render.block.color.BlockColorWater;
import net.minecraft.client.sound.block.BlockSounds;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockStairs;
import net.minecraft.core.block.material.Material;
import net.minecraft.core.block.tag.BlockTags;
import turniplabs.halplibe.helper.BlockBuilder;
import useless.dragonfly.debug.block.BlockModel;
import useless.dragonfly.debug.block.BlockRotatable;
import useless.dragonfly.debug.block.metastates.BookshelfMetaState;
import useless.dragonfly.debug.block.metastates.BrewingMetaState;
import useless.dragonfly.debug.block.metastates.FenceMetaState;
import useless.dragonfly.debug.block.metastates.GrassMetaState;
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
	private static int blockId = 4000;
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
			ModelHelper.getOrCreateBlockState(MOD_ID, "test_stairs.json"), new StairsMetaStateInterpreter(), true))
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
		.build(new BlockModel("dir" + blockId, blockId++, Material.dirt, ModelHelper.getOrCreateBlockModel(MOD_ID, "block/test_block.json")));
	public static final Block brewing = new BlockBuilder(MOD_ID)
		.setBlockModel(new BlockModelDragonFly(ModelHelper.getOrCreateBlockModel(NamespaceId.coreNamespaceId, "block/brewing_stand.json"),
			ModelHelper.getOrCreateBlockState(NamespaceId.coreNamespaceId, "brewing_stand.json"), new BrewingMetaState(), true))
		.build(new BlockModel("brew" + blockId, blockId++, Material.dirt, ModelHelper.getOrCreateBlockModel(NamespaceId.coreNamespaceId, "block/brewing_stand.json"))).withLitInteriorSurface(true);
	public static final Block fence = new BlockBuilder(MOD_ID)
		.setBlockModel(new BlockModelDragonFly(ModelHelper.getOrCreateBlockModel(NamespaceId.coreNamespaceId, "block/birch_fence_inventory.json"),
			ModelHelper.getOrCreateBlockState(MOD_ID, "test_fence.json"), new FenceMetaState(), true))
		.build(new BlockModel("fence" + blockId, blockId++, Material.dirt, ModelHelper.getOrCreateBlockModel(NamespaceId.coreNamespaceId, "block/birch_fence_inventory.json"))).withLitInteriorSurface(true).withTags(BlockTags.FENCES_CONNECT);
	public static final Block bookshelf = new BlockBuilder(MOD_ID)
		.setBlockModel(new BlockModelDragonFly(ModelHelper.getOrCreateBlockModel(NamespaceId.coreNamespaceId, "block/chiseled_bookshelf_inventory.json"),
			ModelHelper.getOrCreateBlockState(NamespaceId.coreNamespaceId, "chiseled_bookshelf.json"), new BookshelfMetaState(), true))
		.build(new BlockRotatable("shelf" + blockId, blockId++, Material.dirt)).withLitInteriorSurface(true);
	public static final Block grassBlock = new BlockBuilder(MOD_ID)
		.setBlockModel(new BlockModelDragonFly(ModelHelper.getOrCreateBlockModel(NamespaceId.coreNamespaceId, "block/grass_block.json"),
			ModelHelper.getOrCreateBlockState(NamespaceId.coreNamespaceId, "grass_block.json"), new GrassMetaState(), true))
		.setBlockColor(new BlockColorGrass())
		.setBlockSound(BlockSounds.GRASS)
		.build(new Block("grass" + blockId, blockId++, Material.grass));
	public static void init() {
		blockId = 5000;
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
