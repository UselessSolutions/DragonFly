package useless.dragonfly.registries;

import net.minecraft.core.Global;
import net.minecraft.core.block.Block;
import turniplabs.halplibe.helper.TextureHelper;

import java.util.HashMap;

public class TextureRegistry {
	public static HashMap<String, Integer> textureNameToIndex = new HashMap<>();
	public static HashMap<Integer, String> textureIndexToName = new HashMap<>();
	public static void init(){}
	protected static void registerTexture(String namespace, String textureName, int index){
		String texKey = getTextureKey(namespace.toLowerCase(), textureName.toLowerCase());
		if (index < 0 || index > Global.TEXTURE_ATLAS_WIDTH_TILES * Global.TEXTURE_ATLAS_WIDTH_TILES){
			throw new RuntimeException("Texture index: " + index + " is outside the texture atlas!");
		}
		if (textureNameToIndex.containsKey(texKey)){
			throw new RuntimeException("There is already a texture with the identifier: " + texKey + " all textures must be assigned unique identifiers");
		}
		if (textureIndexToName.containsKey(index)){
			throw new RuntimeException("There is already an identifier for texture: " + index + " called: " + textureIndexToName.get(index));
		}
		textureNameToIndex.put(texKey, index);
		textureIndexToName.put(index, texKey);
	}
	public static void registerTexture(String namespace, String textureName, int texX, int texY){
		registerTexture(namespace, textureName, Block.texCoordToIndex(texX, texY));
	}
	public static void registerModTexture(String modId, String textureName){
		int[] texCoords = TextureHelper.getOrCreateBlockTexture(modId, textureName);
		registerTexture(modId, textureName, texCoords[0], texCoords[1]);
	}

	public static String getTextureKey(String namespace, String texturename){
		return namespace + ":" + texturename;
	}
	private static void registerVanillaTexture(String texturename, int texX, int texY){
		registerTexture("minecraft", texturename, texX, texY);
	}
	private static void registerVanillaBlockTexture(String texturename, int texX, int texY){
		registerVanillaTexture("block/"+texturename, texX, texY);
	}
	private static void registerVanillaItemTexture(String texturename, int texX, int texY){
		registerVanillaTexture("item/"+texturename, texX, texY);
	}
	static {
		registerVanillaBlockTexture("grass_top", 0, 0);
		registerVanillaBlockTexture("cobbled_stone", 0, 1);
		registerVanillaBlockTexture("moss_overlay", 0, 2);
		registerVanillaBlockTexture("sponge", 0, 3);
		registerVanillaBlockTexture("torch_coal", 0, 5);
		registerVanillaBlockTexture("lever_cobble_stone", 0, 6);
		registerVanillaBlockTexture("rail_turn", 0, 7);
		registerVanillaBlockTexture("rail_straight", 0, 8);
		registerVanillaBlockTexture("block_lapis", 0, 9);
		registerVanillaBlockTexture("block_redstone", 0, 10);
		registerVanillaBlockTexture("sandstone_top", 0, 11);
		registerVanillaBlockTexture("sandstone_side", 0, 12);
		registerVanillaBlockTexture("sandstone_bottom", 0, 13);
		registerVanillaBlockTexture("brick_sandstone", 0, 14);
		registerVanillaBlockTexture("animation_breaking_0", 0, 15);
		registerVanillaBlockTexture("rail_powered_overlay", 0, 16);
		registerVanillaBlockTexture("chest_planks_oak_painted_top_overlay", 0, 17);
		registerVanillaBlockTexture("chest_planks_oak_painted_front_double_left_overlay", 0, 18);
		registerVanillaBlockTexture("chest_planks_oak_painted_back_double_left_overlay", 0, 19);
		registerVanillaBlockTexture("log_oak_side", 0, 20);
		registerVanillaBlockTexture("log_oak_mossy_side", 0, 21);
		registerVanillaBlockTexture("log_pine_side", 0, 23);
		registerVanillaBlockTexture("log_birch_side", 0, 24);
		registerVanillaBlockTexture("log_cherry_side", 0, 25);
		registerVanillaBlockTexture("log_eucalyptus_side", 0, 26);
		registerVanillaBlockTexture("debug_colortest", 0, 31);

		registerVanillaBlockTexture("stone", 1, 0);
		registerVanillaBlockTexture("bedrock", 1, 1);
		registerVanillaBlockTexture("cobbled_stone_retro", 1, 2);
		registerVanillaBlockTexture("glass", 1, 3);
		registerVanillaBlockTexture("mobspawner", 1, 4);
		registerVanillaBlockTexture("door_planks_oak_top", 1, 5);
		registerVanillaBlockTexture("door_planks_oak_bottom", 1, 6);
		registerVanillaBlockTexture("debug_trommel_front", 1, 7);
		registerVanillaBlockTexture("trommel_idle_west", 1, 8);
		registerVanillaBlockTexture("trommel_front_base", 1, 9); // Unsure on this one
		registerVanillaBlockTexture("trommel_top", 1, 10);
		registerVanillaBlockTexture("trommel_active_west", 1, 11);
		registerVanillaBlockTexture("animation_breaking_1", 1, 15);
		registerVanillaBlockTexture("motionsensor_overlay", 1, 16);
		registerVanillaBlockTexture("chest_planks_oak_painted_side_overlay", 1, 17);
		registerVanillaBlockTexture("chest_planks_oak_painted_front_double_right_overlay", 1, 18);
		registerVanillaBlockTexture("chest_planks_oak_painted_back_double_right_overlay", 1, 19);
		registerVanillaBlockTexture("log_oak_top", 1, 20);
		registerVanillaBlockTexture("log_oak_mossy_top", 1, 21);
		registerVanillaBlockTexture("log_pine_top", 1, 23);
		registerVanillaBlockTexture("log_birch_top", 1, 24);
		registerVanillaBlockTexture("log_cherry_top", 1, 25);
		registerVanillaBlockTexture("log_eucalyptus_top", 1, 26);
		registerVanillaBlockTexture("debug_black", 1, 31);

		registerVanillaBlockTexture("dirt", 2, 0);
		registerVanillaBlockTexture("sand", 2, 1);
		registerVanillaBlockTexture("planks_oak_painted", 2, 3);
		registerVanillaBlockTexture("snow", 2, 4);
		registerVanillaBlockTexture("door_iron_top", 2, 5);
		registerVanillaBlockTexture("door_iron_bottom", 2, 6);
		registerVanillaBlockTexture("debug_trommel_back", 2, 7);
		registerVanillaBlockTexture("trommel_east", 2, 8);
		registerVanillaBlockTexture("trommel_front_overlay", 2, 9); // not sure on this one
		registerVanillaBlockTexture("trommel_idle_front", 2, 10);
		registerVanillaBlockTexture("trommel_idle_front2", 2, 11); // Unused duplicate texture?
		registerVanillaBlockTexture("polished_limestone", 2, 12);
		registerVanillaBlockTexture("polished_granite", 2, 13);
		registerVanillaBlockTexture("polished_basalt", 2, 14);
		registerVanillaBlockTexture("animation_breaking_2", 2, 15);
		registerVanillaBlockTexture("netherrack_igneous_overlay", 2, 16);
		registerVanillaBlockTexture("chest_planks_oak_painted_single_front_overlay", 2, 17);
		registerVanillaBlockTexture("leaves_oak_fancy", 2, 20);
		registerVanillaBlockTexture("leaves_oak_mossy_fancy", 2, 21);
		registerVanillaBlockTexture("leaves_oak_retro_fancy", 2, 22);
		registerVanillaBlockTexture("leaves_pine_fancy", 2, 23);
		registerVanillaBlockTexture("leaves_birch_fancy", 2, 24);
		registerVanillaBlockTexture("leaves_cherry_fancy", 2, 25);
		registerVanillaBlockTexture("leaves_eucalyptus_fancy", 2, 26);
		registerVanillaBlockTexture("leaves_shrub_fancy", 2, 27);
		registerVanillaBlockTexture("debug_red", 2, 31);

		registerVanillaBlockTexture("grass_side", 3, 0);
		registerVanillaBlockTexture("gravel", 3, 1);
		registerVanillaBlockTexture("bookshelf_side", 3, 2);
		registerVanillaBlockTexture("trapdoor_planks_oak_side", 3, 3);
		registerVanillaBlockTexture("ice", 3, 4);
		registerVanillaBlockTexture("ladder", 3, 5);
		registerVanillaBlockTexture("torch_redstone_active", 3, 6);
		registerVanillaBlockTexture("torch_redstone_idle", 3, 7);
		registerVanillaBlockTexture("repeater_idle_base", 3, 8);
		registerVanillaBlockTexture("repeater_active_base", 3, 9);
		registerVanillaBlockTexture("rail_powered_idle", 3, 10);
		registerVanillaBlockTexture("rail_powered_active", 3, 11);
		registerVanillaBlockTexture("rail_detector", 3, 12);
		registerVanillaBlockTexture("carved_limestone_top", 3, 13);
		registerVanillaBlockTexture("carved_limestone_side", 3, 14);
		registerVanillaBlockTexture("animation_breaking_3", 3, 15);
		registerVanillaBlockTexture("ore_nethercoal_netherrack_overlay", 3, 16);
		registerVanillaBlockTexture("leaves_cherry_overlay", 3, 17);
		registerVanillaBlockTexture("leaves_oak_fast", 3, 20);
		registerVanillaBlockTexture("leaves_oak_mossy_fast", 3, 21);
		registerVanillaBlockTexture("leaves_oak_retro_fast", 3, 22);
		registerVanillaBlockTexture("leaves_pine_fast", 3, 23);
		registerVanillaBlockTexture("leaves_birch_fast", 3, 24);
		registerVanillaBlockTexture("leaves_cherry_fast", 3, 25);
		registerVanillaBlockTexture("leaves_eucalyptus_fast", 3, 26);
		registerVanillaBlockTexture("leaves_shrub_fast", 3, 27);
		registerVanillaBlockTexture("debug_green", 3, 31);

		registerVanillaBlockTexture("planks_oak", 4, 0);
		registerVanillaBlockTexture("cobbled_stone_mossy", 4, 2);
		registerVanillaBlockTexture("trapdoor_iron_side", 4, 3);
		registerVanillaBlockTexture("grass_snowy_side", 4, 4);
		registerVanillaBlockTexture("trapdoor_planks_oak_top", 4, 5);
		registerVanillaBlockTexture("grass_path_top", 4, 6);
		registerVanillaBlockTexture("trapdoor_iron_top", 4, 7);
		registerVanillaBlockTexture("basket_side", 4, 8);
		registerVanillaBlockTexture("basket_bottom", 4, 9);
		registerVanillaBlockTexture("wire_redstone_cross", 4, 10);
		registerVanillaBlockTexture("debug_transparent", 4, 11); // Unused??
		registerVanillaBlockTexture("lamp_idle", 4, 12);
		registerVanillaBlockTexture("carved_granite_top", 4, 13);
		registerVanillaBlockTexture("carved_granite_side", 4, 14);
		registerVanillaBlockTexture("animation_breaking_4", 4, 15);
		registerVanillaBlockTexture("ore_redstone_overlay", 4, 16);
		registerVanillaBlockTexture("leaves_cocoa_overlay", 4, 17);
		registerVanillaBlockTexture("sapling_oak", 4, 20);
		registerVanillaBlockTexture("sapling_oak_retro", 4, 22);
		registerVanillaBlockTexture("sapling_pine", 4, 23);
		registerVanillaBlockTexture("sapling_birch", 4, 24);
		registerVanillaBlockTexture("sapling_cherry", 4, 25);
		registerVanillaBlockTexture("sapling_eucalyptus", 4, 26);
		registerVanillaBlockTexture("sapling_shrub", 4, 27);
		registerVanillaBlockTexture("debug_blue", 4, 31);

		registerVanillaBlockTexture("carved_stone_side", 5, 0);
		registerVanillaBlockTexture("polished_stone", 5, 1);
		registerVanillaBlockTexture("obsidian", 5, 2);
		registerVanillaBlockTexture("trapdoor_glass_side", 5, 3);
		registerVanillaBlockTexture("cactus_top", 5, 4);
		registerVanillaBlockTexture("pumpkin_bottom", 5, 5);
		registerVanillaBlockTexture("grass_path_side", 5, 6);
		registerVanillaBlockTexture("candle_soulwax", 5, 7);
		registerVanillaBlockTexture("leather_seat", 5, 8); // No real clue what im looking at in this one tbh
		registerVanillaBlockTexture("bed_bottom_front", 5, 9);
		registerVanillaBlockTexture("wire_redstone_line", 5, 10);
		registerVanillaBlockTexture("debug_transparent2", 5, 11); // Unused?
		registerVanillaBlockTexture("lamp_active", 5, 12);
		registerVanillaBlockTexture("carved_basalt_top", 5, 13);
		registerVanillaBlockTexture("carved_basalt_side", 5, 14);
		registerVanillaBlockTexture("animation_breaking_5", 5, 15);
		registerVanillaBlockTexture("wool_black", 5, 17);
		registerVanillaBlockTexture("wool_red", 5, 18);
		registerVanillaBlockTexture("wool_green", 5, 19);
		registerVanillaBlockTexture("wool_brown", 5, 20);
		registerVanillaBlockTexture("wool_blue", 5, 21);
		registerVanillaBlockTexture("wool_purple", 5, 22);
		registerVanillaBlockTexture("wool_cyan", 5, 23);
		registerVanillaBlockTexture("wool_light_grey", 5, 24);
		registerVanillaBlockTexture("debug_white", 5, 31);

		registerVanillaBlockTexture("carved_stone_top", 6, 0);
		registerVanillaBlockTexture("algae", 6, 1);
		registerVanillaBlockTexture("grass_side_overlay", 6, 2);
		registerVanillaBlockTexture("grass_retro_side", 6, 3);
		registerVanillaBlockTexture("cactus_side", 6, 4);
		registerVanillaBlockTexture("farmland_wet_top", 6, 5);
		registerVanillaBlockTexture("pumpkin_top", 6, 6);
		registerVanillaBlockTexture("pumpkin_side", 6, 7);
		registerVanillaBlockTexture("bed_bottom_top", 6, 8);
		registerVanillaBlockTexture("bed_bottom_side", 6, 9);
		registerVanillaBlockTexture("basalt", 6, 10);
		registerVanillaBlockTexture("spike_side", 6, 11);
		registerVanillaBlockTexture("glass_tinted", 6, 12);
		registerVanillaBlockTexture("bricks_slate", 6, 13);
		registerVanillaBlockTexture("carved_slate_side", 6, 14);
		registerVanillaBlockTexture("animation_breaking_6", 6, 15);
		registerVanillaBlockTexture("wool_grey", 6, 17);
		registerVanillaBlockTexture("wool_pink", 6, 18);
		registerVanillaBlockTexture("wool_lime", 6, 19);
		registerVanillaBlockTexture("wool_yellow", 6, 29);
		registerVanillaBlockTexture("wool_light_blue", 6, 21);
		registerVanillaBlockTexture("wool_magenta", 6, 22);
		registerVanillaBlockTexture("wool_orange", 6, 23);
		registerVanillaBlockTexture("wool_white", 6, 24);
		registerVanillaBlockTexture("debug_cyan", 6, 31);

		registerVanillaBlockTexture("brick_clay", 7, 0);
		registerVanillaBlockTexture("tallgrass", 7, 2);
		registerVanillaBlockTexture("deadbush", 7, 3);
		registerVanillaBlockTexture("cactus_bottom", 7, 4);
		registerVanillaBlockTexture("farmland_dry_top", 7, 5);
		registerVanillaBlockTexture("netherrack", 7, 6);
		registerVanillaBlockTexture("pumpkin_carved_idle", 7, 7);
		registerVanillaBlockTexture("bed_top_top", 7, 8);
		registerVanillaBlockTexture("bed_top_side", 7, 9);
		registerVanillaBlockTexture("cobbled_basalt", 7, 10);
		registerVanillaBlockTexture("spike_top", 7, 11);
		registerVanillaBlockTexture("motionsensor_front_idle", 7, 12);
		registerVanillaBlockTexture("slate_side", 7, 13);
		registerVanillaBlockTexture("polished_slate", 7, 14);
		registerVanillaBlockTexture("animation_breaking_7", 7, 15);
		registerVanillaBlockTexture("pebbles_overlay0", 7, 18);
		registerVanillaBlockTexture("fence_chainlink_center", 7, 19);
		registerVanillaBlockTexture("fence_chainlink_pole", 7, 20);
		registerVanillaBlockTexture("debug_magenta", 7, 31);

		registerVanillaBlockTexture("tnt_side", 8, 0);
		registerVanillaBlockTexture("grass_retro_top", 8, 1);
		registerVanillaBlockTexture("grass_top2", 8, 2); // Unused?
		registerVanillaBlockTexture("fern", 8, 3);
		registerVanillaBlockTexture("block_clay", 8, 4);
		registerVanillaBlockTexture("crops_wheat_stage0", 8, 5);
		registerVanillaBlockTexture("soulsand", 8, 6);
		registerVanillaBlockTexture("pumpkin_carved_active", 8, 7);
		registerVanillaBlockTexture("brick_iron", 8, 8);
		registerVanillaBlockTexture("bed_top_back", 8, 9);
		registerVanillaBlockTexture("brick_basalt", 8, 10);
		registerVanillaBlockTexture("mesh", 8, 11);
		registerVanillaBlockTexture("motionsensor_front_active", 8, 12);
		registerVanillaBlockTexture("slate_top", 8, 13);
		registerVanillaBlockTexture("carved_slate_top", 8, 14);
		registerVanillaBlockTexture("animation_breaking_8", 8, 15);
		registerVanillaBlockTexture("pebbles_overlay1", 8, 18);
		registerVanillaBlockTexture("fence_chainlink_top", 8, 19);
		registerVanillaBlockTexture("debug_yellow", 8, 31);

		registerVanillaBlockTexture("tnt_top", 9, 0);
		registerVanillaBlockTexture("chest_planks_oak_top", 9, 1);
		registerVanillaBlockTexture("chest_planks_oak_double_front_left", 9, 2);
		registerVanillaBlockTexture("chest_planks_oak_double_back_left", 9, 3);
		registerVanillaBlockTexture("sugarcane", 9, 4);
		registerVanillaBlockTexture("crops_wheat_stage1", 9, 5);
		registerVanillaBlockTexture("glowstone", 9, 6);
		registerVanillaBlockTexture("cake_top", 9, 7);
		registerVanillaBlockTexture("brick_lapis", 9, 8);
		registerVanillaBlockTexture("brick_gold", 9, 9);
		registerVanillaBlockTexture("limestone", 9, 10);
		registerVanillaBlockTexture("granite", 9, 11);
		registerVanillaBlockTexture("marble", 9, 12);
		registerVanillaBlockTexture("capstone_marble_side", 9, 13);
		registerVanillaBlockTexture("capstone_marble_top", 9, 14);
		registerVanillaBlockTexture("animation_breaking_9", 9, 15);
		registerVanillaBlockTexture("pebbles_overlay2", 9, 18);

		registerVanillaBlockTexture("tnt_bottom", 10, 0);
		registerVanillaBlockTexture("chest_planks_oak_side", 10, 1);
		registerVanillaBlockTexture("chest_planks_oak_front_right", 10, 2);
		registerVanillaBlockTexture("chest_planks_oak_back_right", 10, 3);
		registerVanillaBlockTexture("noteblock", 10, 4);
		registerVanillaBlockTexture("crops_wheat_stage2", 10, 5);
		registerVanillaBlockTexture("piston_head_front_sticky", 10, 6);
		registerVanillaBlockTexture("cake_side", 10, 7);
		registerVanillaBlockTexture("brick_stone", 10, 8);
		registerVanillaBlockTexture("ore_nethercoal_netherrack", 10, 9);
		registerVanillaBlockTexture("cobbled_limestone", 10, 10);
		registerVanillaBlockTexture("cobbled_granite", 10, 11);
		registerVanillaBlockTexture("motionsensor_side", 10, 12);
		registerVanillaBlockTexture("brick_marble", 10, 13);
		registerVanillaBlockTexture("carved_marble_side", 10, 14); // unused
		registerVanillaBlockTexture("ice_perma", 10, 15);

		registerVanillaBlockTexture("cobweb", 11, 0);
		registerVanillaBlockTexture("chest_planks_oak_single_front", 11, 1);
		registerVanillaBlockTexture("workbench_top", 11, 2);
		registerVanillaBlockTexture("workbench_front", 11, 3);
		registerVanillaBlockTexture("jukebox_top", 11, 4);
		registerVanillaBlockTexture("crops_wheat_stage3", 11, 5);
		registerVanillaBlockTexture("piston_head_front", 11, 6);
		registerVanillaBlockTexture("cake_inner", 11, 7);
		registerVanillaBlockTexture("brick_stone_mossy", 11, 8);
		registerVanillaBlockTexture("sponge_wet", 11, 9);
		registerVanillaBlockTexture("brick_limestone", 11, 10);
		registerVanillaBlockTexture("brick_granite", 11, 11);
		registerVanillaBlockTexture("column_marble_side", 11, 12);
		registerVanillaBlockTexture("column_marble_top", 11, 13);
		registerVanillaBlockTexture("carved_marble_top", 11, 14); // unused
		registerVanillaBlockTexture("permafrost", 11, 15);

		registerVanillaBlockTexture("flower_red", 12, 0);
		registerVanillaBlockTexture("mushroom_red", 12, 1);
		registerVanillaBlockTexture("furnace_stone_idle_front", 12, 2);
		registerVanillaBlockTexture("workbench_side", 12, 3);
		registerVanillaBlockTexture("netherrack_igneous", 12,4);
		registerVanillaBlockTexture("crops_wheat_stage4", 12,5);
		registerVanillaBlockTexture("piston_side", 12,6);
		registerVanillaBlockTexture("cake_bottom", 12,7);
		registerVanillaBlockTexture("dirt_scorched_rich", 12,8);
		registerVanillaBlockTexture("brick_stone_polished", 12,9);
		registerVanillaBlockTexture("spinifex", 12,10);
		registerVanillaBlockTexture("block_olivine", 12,11);
		registerVanillaBlockTexture("block_quartz", 12,12);
		registerVanillaBlockTexture("stool_side", 12,13);
		registerVanillaBlockTexture("cobbled_permafrost", 12,15);

		registerVanillaBlockTexture("flower_yellow", 13,0);
		registerVanillaBlockTexture("mushroom_brown", 13,1);
		registerVanillaBlockTexture("furnace_stone_side", 13,2);
		registerVanillaBlockTexture("furnace_stone_active_front", 13,3);
		registerVanillaBlockTexture("soulslate", 13,4); // making up a name no clue what this is
		registerVanillaBlockTexture("crops_wheat_stage5", 13,5);
		registerVanillaBlockTexture("piston_back", 13,6);
		registerVanillaBlockTexture("furnace_blast_idle_front", 13,7);
		registerVanillaBlockTexture("dirt_scorched", 13,8);
		registerVanillaBlockTexture("debug_transparent3", 13,9);
		registerVanillaBlockTexture("lantern_firefly_blue", 13,10);
		registerVanillaBlockTexture("lantern_firefly_green", 13,11);
		registerVanillaBlockTexture("water0", 13,12);
		registerVanillaBlockTexture("stool_top", 13,13);
		registerVanillaBlockTexture("lava0", 13,14);
		registerVanillaBlockTexture("brick_permafrost", 13,15);

		registerVanillaBlockTexture("debug_blue2", 14,0);
		registerVanillaBlockTexture("dispenser_cobble_stone_front", 14,2);
		registerVanillaBlockTexture("furnace_stone_top", 14,3);
		registerVanillaBlockTexture("netherrack_bloody", 14,4);
		registerVanillaBlockTexture("crops_wheat_stage6", 14,5);
		registerVanillaBlockTexture("piston_front_extended", 14,6);
		registerVanillaBlockTexture("furnace_blast_side", 14,7);
		registerVanillaBlockTexture("furnace_blast_active_front", 14,8);
		registerVanillaBlockTexture("furnace_blast_bottom", 14,9);
		registerVanillaBlockTexture("lantern_firefly_orange", 14,10);
		registerVanillaBlockTexture("lantern_firefly_red", 14,11);
		registerVanillaBlockTexture("water1", 14,12);
		registerVanillaBlockTexture("water2", 14,13);
		registerVanillaBlockTexture("lava1", 14,14);
		registerVanillaBlockTexture("lava2", 14,15);

		registerVanillaBlockTexture("debug_fire", 15,1);
		registerVanillaBlockTexture("debug_fire2", 15,2);
		registerVanillaBlockTexture("crops_wheat_stage7", 15,5);
		registerVanillaBlockTexture("block_green_diamond", 15,6);
		registerVanillaBlockTexture("block_charcoal", 15,7);
		registerVanillaBlockTexture("furnace_blast_top", 15,8);
		registerVanillaBlockTexture("lantern_inside", 15,11);
		registerVanillaBlockTexture("water3", 15,12);
		registerVanillaBlockTexture("water4", 15,13);
		registerVanillaBlockTexture("lava3", 15,14);
		registerVanillaBlockTexture("lava4", 15,15);
		registerVanillaBlockTexture("debug_transparent4", 15,16);

		registerVanillaBlockTexture("ore_gold_stone", 16,0);
		registerVanillaBlockTexture("ore_gold_basalt", 16,1);
		registerVanillaBlockTexture("ore_gold_limestone", 16,2);
		registerVanillaBlockTexture("ore_gold_granite", 16,3);
		registerVanillaBlockTexture("block_iron_top", 16,4);
		registerVanillaBlockTexture("block_iron_side", 16,5);
		registerVanillaBlockTexture("block_iron_bottom", 16,6);
		registerVanillaBlockTexture("block_coal", 16,7);
		registerVanillaBlockTexture("debug_north", 16,8);
		registerVanillaBlockTexture("debug_east", 16,9);
		registerVanillaBlockTexture("grass_scorched_top", 16,11);

		registerVanillaBlockTexture("ore_iron_stone", 17,0);
		registerVanillaBlockTexture("ore_iron_basalt", 17,1);
		registerVanillaBlockTexture("ore_iron_limestone", 17,2);
		registerVanillaBlockTexture("ore_iron_granite", 17,3);
		registerVanillaBlockTexture("block_gold_top", 17,4);
		registerVanillaBlockTexture("block_gold_side", 17,5);
		registerVanillaBlockTexture("block_gold_bottom", 17,6);
		registerVanillaBlockTexture("block_nethercoal", 17,7);
		registerVanillaBlockTexture("debug_south", 17,8);
		registerVanillaBlockTexture("debug_west", 17,9);
		registerVanillaBlockTexture("grass_scorched_side", 17,11);

		registerVanillaBlockTexture("ore_coal_stone", 18,0);
		registerVanillaBlockTexture("ore_coal_basalt", 18,1);
		registerVanillaBlockTexture("ore_coal_limestone", 18,2);
		registerVanillaBlockTexture("ore_coal_granite", 18,3);
		registerVanillaBlockTexture("block_diamond_top", 18,4);
		registerVanillaBlockTexture("block_diamond_side", 18,5);
		registerVanillaBlockTexture("block_diamond_bottom", 18,6);
		registerVanillaBlockTexture("debug_down", 18,8);
		registerVanillaBlockTexture("debug_up", 18,9);

		registerVanillaBlockTexture("ore_lapis_stone", 19,0);
		registerVanillaBlockTexture("ore_lapis_basalt", 19,1);
		registerVanillaBlockTexture("ore_lapis_limestone", 19,2);
		registerVanillaBlockTexture("ore_lapis_granite", 19,3);
		registerVanillaBlockTexture("block_steel_top", 19,4);
		registerVanillaBlockTexture("block_steel_side", 19,5);
		registerVanillaBlockTexture("block_steel_bottom", 19,6);

		registerVanillaBlockTexture("ore_diamond_stone", 20,0);
		registerVanillaBlockTexture("ore_diamond_basalt", 20,1);
		registerVanillaBlockTexture("ore_diamond_limestone", 20,2);
		registerVanillaBlockTexture("ore_diamond_granite", 20,3);

		registerVanillaBlockTexture("ore_redstone_stone", 21,0);
		registerVanillaBlockTexture("ore_redstone_basalt", 21,1);
		registerVanillaBlockTexture("ore_redstone_limestone", 21,2);
		registerVanillaBlockTexture("ore_redstone_granite", 21,3);

		registerVanillaBlockTexture("mud", 22,0);
		registerVanillaBlockTexture("mud_dry", 23,0);
	}
}
