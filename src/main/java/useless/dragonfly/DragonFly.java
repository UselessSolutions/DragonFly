package useless.dragonfly;

import com.google.gson.Gson;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.entrypoint.PreLaunchEntrypoint;
import net.minecraft.client.render.TextureFX;
import net.minecraft.core.Global;
import net.minecraft.core.util.helper.Side;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import turniplabs.halplibe.HalpLibe;
import useless.dragonfly.debug.DebugMain;
import useless.dragonfly.registries.TextureRegistry;
import useless.dragonfly.utilities.Utilities;

import java.io.IOException;
import java.util.List;

public class DragonFly implements ModInitializer, PreLaunchEntrypoint {
	static {
		// DO NOT TOUCH THIS! It's an error prevention method. Thanks Useless!
		try {
			Class.forName("turniplabs.halplibe.HalpLibe");
			Class.forName("net.minecraft.core.block.Block");
			Class.forName("net.minecraft.core.item.Item");
		} catch (ClassNotFoundException ignored) {}
	}
    public static final String MOD_ID = "dragonfly";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	public static final Gson GSON = new Gson();
	public static final Side[] sides = new Side[]{Side.BOTTOM, Side.TOP, Side.NORTH, Side.SOUTH, Side.WEST, Side.EAST};
	public static double terrainAtlasWidth = TextureFX.tileWidthTerrain * Global.TEXTURE_ATLAS_WIDTH_TILES;
	public static boolean isDev = FabricLoader.getInstance().getModContainer(MOD_ID).get().getMetadata().getVersion().getFriendlyString().equals("${version}");
    @Override
    public void onInitialize() {
		if (isDev){
			DebugMain.init();
		}
        LOGGER.info("DragonFly initialized.");
    }

	@Override
	public void onPreLaunch() {
		new HalpLibe().onPreLaunch();
		TextureRegistry.init();
		
		// Load every mod's resources
		List<String> assetDirectories = Utilities.getResourceFiles("/assets/");
		System.out.println(assetDirectories);

		for (String modAssetDir: assetDirectories) { // for each asset directory
			// Register all block textures
			for (String blockTextureID: Utilities.getResourceFiles("/assets/" + modAssetDir + "/block/")) {
				if (blockTextureID.contains(".mcmeta")) continue;
				TextureRegistry.softRegisterTexture(modAssetDir + ":block/" + blockTextureID.replace(".png", ""));
			}

			// Register all item textures
			for (String itemTextureID: Utilities.getResourceFiles("/assets/" + modAssetDir + "/item/")) {
				if (itemTextureID.contains(".mcmeta")) continue;
				TextureRegistry.softRegisterTexture(modAssetDir + ":item/" + itemTextureID.replace(".png", ""));
			}
		}
		LOGGER.info("DragonFly pre launch initialized.");
		//throw new RuntimeException();
	}
}
