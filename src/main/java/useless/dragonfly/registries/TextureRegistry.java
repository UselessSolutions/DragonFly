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
		registerVanillaBlockTexture("sponge_dry", 0, 3);
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
		registerVanillaBlockTexture("grass_tall", 7, 2);
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
		registerVanillaBlockTexture("slate", 7, 13);
		registerVanillaBlockTexture("polished_slate", 7, 14);
		registerVanillaBlockTexture("animation_breaking_7", 7, 15);
		registerVanillaBlockTexture("pebbles_overlay0", 7, 18);
		registerVanillaBlockTexture("fence_chainlink_center", 7, 19);
		registerVanillaBlockTexture("fence_chainlink_pole", 7, 20);
		registerVanillaBlockTexture("debug_magenta", 7, 31);

	}
}
