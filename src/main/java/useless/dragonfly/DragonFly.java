package useless.dragonfly;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.entrypoint.PreLaunchEntrypoint;
import net.minecraft.client.render.TextureFX;
import net.minecraft.core.Global;
import net.minecraft.core.util.helper.Side;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import useless.dragonfly.debug.DebugMain;
import useless.dragonfly.model.entity.animation.Animation;
import useless.dragonfly.model.entity.animation.AnimationDeserializer;
import useless.dragonfly.registries.TextureRegistry;
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
	public static final Gson GSON = new GsonBuilder().registerTypeAdapter(Animation.class, new AnimationDeserializer()).create();
	public static final Side[] sides = new Side[]{Side.BOTTOM, Side.TOP, Side.NORTH, Side.SOUTH, Side.WEST, Side.EAST};
	public static double terrainAtlasWidth = TextureFX.tileWidthTerrain * Global.TEXTURE_ATLAS_WIDTH_TILES;
	public static boolean isDev;
	static {
		String modVersion = FabricLoader.getInstance().getModContainer(MOD_ID).get().getMetadata().getVersion().getFriendlyString();
		isDev = modVersion.equals("${version}") || modVersion.contains("dev");
	}
    @Override
    public void onInitialize() {
		if (isDev){
			DebugMain.init();
		}
        LOGGER.info("DragonFly initialized.");
    }

	@Override
	public void onPreLaunch() {
		TextureRegistry.init();
	}
}
