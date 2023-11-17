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

		registerVanillaBlockTexture("debug_black", 3, 0);

	}
}
