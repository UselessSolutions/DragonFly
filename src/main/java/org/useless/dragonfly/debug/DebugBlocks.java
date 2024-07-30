package org.useless.dragonfly.debug;

import net.minecraft.core.block.material.Material;
import net.minecraft.core.util.collection.NamespaceID;
import org.useless.dragonfly.debug.block.BlockDebugModel;
import org.useless.dragonfly.model.newmodels.BlockModelStandard;
import org.useless.dragonfly.model.newmodels.generic.StaticModelRegistry;
import org.useless.dragonfly.utilities.Utilities;
import turniplabs.halplibe.helper.BlockBuilder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import static org.useless.dragonfly.DragonFly.MOD_ID;

public class DebugBlocks {
	private static int blockId = 4000;
//public static final Block testBlock = new BlockBuilder(MOD_ID)
//	.setBlockModel(
//		block -> new DFBlockModelBuilder(MOD_ID)
//		.setBlockModel("block/testblock.json")
//		.build(block))
//	.build(new BlockDebugModel("testblock" + blockId, blockId++, Material.dirt));
//	public static final Block testBlock2 = new BlockBuilder(MOD_ID)
//		.setBlockModel(
//			block -> new DFBlockModelBuilder(MOD_ID)
//				.setBlockModel("block/testblock2.json")
//				.build(block))
//		.build(new BlockDebugModel("testblock" + blockId, blockId++, Material.dirt));
//	public static final Block testBlock3 = new BlockBuilder(MOD_ID)
//		.setBlockModel(
//			block -> new DFBlockModelBuilder(MOD_ID)
//				.setBlockModel("block/testblock3.json")
//				.build(block))
//		.build(new BlockDebugModel("testblock" + blockId, blockId++, Material.dirt));
//	public static final Block testBlock6 = new BlockBuilder(MOD_ID)
//		.setBlockModel(
//			block -> new DFBlockModelBuilder(MOD_ID)
//				.setBlockModel("block/directionpyramid.json")
//				.build(block))
//		.build(new BlockDebugModel("testblock" + blockId, blockId++, Material.dirt));
//	public static final Block testBlock7 = new BlockBuilder(MOD_ID)
//		.setBlockModel(
//			block -> new DFBlockModelBuilder(MOD_ID)
//				.setBlockModel("block/stool.json")
//				.build(block))
//		.build(new BlockDebugModel("testblock" + blockId, blockId++, Material.dirt));
//	public static final Block harris = new BlockBuilder(MOD_ID)
//		.setBlockModel(
//			block -> new DFBlockModelBuilder(MOD_ID)
//				.setBlockModel("block/harris.json")
//				.build(block))
//		.build(new BlockDebugModel("testblock" + blockId, blockId++, Material.dirt));
//	public static final Block slope = new BlockBuilder(MOD_ID)
//		.setBlockModel(
//			block -> new DFBlockModelBuilder(MOD_ID)
//				.setBlockModel("block/slope.json")
//				.build(block))
//		.build(new BlockDebugModel("testblock" + blockId, blockId++, Material.dirt));
//	public static final Block stairs = new BlockBuilder(MOD_ID)
//		.setBlockModel(
//			block -> new DFBlockModelBuilder(MOD_ID)
//				.setBlockModel("minecraft", "block/cut_copper_stairs.json")
//				.setBlockState("test_stairs.json")
//				.setMetaStateInterpreter(new StairsMetaStateInterpreter())
//				.build(block))
//		.build(new BlockStairs(Block.dirt,blockId++)).withLitInteriorSurface(true);
//	public static final Block trel = new BlockBuilder(MOD_ID)
//		.setBlockModel(
//			block -> new DFBlockModelBuilder(MOD_ID)
//				.setBlockModel("block/bean_trellis_bottom_0.json")
//				.build(block))
//		.build(new BlockDebugModel("trel" + blockId, blockId++, Material.dirt));
//	public static final Block trel1 = new BlockBuilder(MOD_ID)
//		.setBlockModel(
//			block -> new DFBlockModelBuilder(MOD_ID)
//				.setBlockModel("block/bean_trellis_bottom_1.json")
//				.build(block))
//		.build(new BlockDebugModel("trel" + blockId, blockId++, Material.dirt));
//	public static final Block trel2 = new BlockBuilder(MOD_ID)
//		.setBlockModel(
//			block -> new DFBlockModelBuilder(MOD_ID)
//				.setBlockModel("block/bean_trellis_bottom_2.json")
//				.build(block))
//		.build(new BlockDebugModel("trel" + blockId, blockId++, Material.dirt));
//	public static final Block sieve = new BlockBuilder(MOD_ID)
//		.setBlockModel(
//			block -> new DFBlockModelBuilder(MOD_ID)
//				.setBlockModel("block/wooden_sieve.json")
//				.build(block))
//		.build(new BlockDebugModel("sieve" + blockId, blockId++, Material.dirt));
//	public static final Block dirTest = new BlockBuilder(MOD_ID)
//		.setBlockModel(
//			block -> new DFBlockModelBuilder(MOD_ID)
//				.setBlockModel("block/test_block.json")
//				.build(block))
//		.build(new BlockDebugModel("dir" + blockId, blockId++, Material.dirt));
//	public static final Block brewing = new BlockBuilder(MOD_ID)
//		.setBlockModel(
//			block -> new DFBlockModelBuilder(MOD_ID)
//				.setBlockModel("minecraft", "block/brewing_stand.json")
//				.setBlockState("minecraft", "brewing_stand.json")
//				.setMetaStateInterpreter(new BrewingMetaState())
//				.build(block))
//		.build(new BlockDebugModel("brew" + blockId, blockId++, Material.dirt)).withLitInteriorSurface(true);
//	public static final Block fence = new BlockBuilder(MOD_ID)
//		.setBlockModel(
//			block -> new DFBlockModelBuilder(MOD_ID)
//				.setBlockModel("minecraft", "block/birch_fence_inventory.json")
//				.setBlockState("test_fence.json")
//				.setMetaStateInterpreter(new FenceMetaState())
//				.build(block))
//		.build(new BlockDebugModel("fence" + blockId, blockId++, Material.dirt)).withLitInteriorSurface(true).withTags(BlockTags.FENCES_CONNECT);
//	public static final Block bookshelf = new BlockBuilder(MOD_ID)
//		.setBlockModel(
//			block -> new DFBlockModelBuilder(MOD_ID)
//				.setBlockModel("minecraft", "block/chiseled_bookshelf_inventory.json")
//				.setBlockState("minecraft", "chiseled_bookshelf.json")
//				.setMetaStateInterpreter(new BookshelfMetaState())
//				.build(block))
//		.build(new BlockRotatable("shelf" + blockId, blockId++, Material.dirt)).withLitInteriorSurface(true);
//	public static final Block grassBlock = new BlockBuilder(MOD_ID)
//		.setBlockModel(
//			block -> new DFBlockModelBuilder(MOD_ID)
//				.setBlockModel("minecraft", "block/grass_block.json")
//				.setBlockState("minecraft", "grass_block.json")
//				.setMetaStateInterpreter(new GrassMetaState())
//				.build(block))
//		.setBlockSound(BlockSounds.GRASS)
//		.setBlockColor((block) -> new BlockColorCustom(Colorizers.grass))
//		.build(new Block("grass" + blockId, blockId++, Material.grass));

	public static void init() {
		blockId = 5000;
		try {
			for (String string : getResourceFiles("/assets/minecraft/models/block/")) {
//				System.out.println(string);
				String string2 = string.replace(".json", "");
				try{
					new BlockBuilder(MOD_ID)
						.setBlockModel(
							block -> new BlockModelStandard<>(block, StaticModelRegistry.getInstance().retrieveModel(new NamespaceID("minecraft", "block/" + string2))))
						.setHardness(1)
						.build(new BlockDebugModel(string2, blockId++, Material.dirt));
				} catch (Exception e){
					e.printStackTrace();
				}

//				System.out.println(string + " created");
			}
//			for (String string : getResourceFiles("assets/minecraft/blockstates/")) {
////				System.out.println(string);
//				try {
//					System.out.println(ModelHelper.getOrCreateBlockState(NamespaceId.coreNamespaceId, string));
//				}
//				catch (Exception e){
//					System.out.println(e);
//				}
//			}
		} catch (IOException e) {
			e.printStackTrace();
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
