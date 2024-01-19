package useless.dragonfly;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.render.TextureFX;
import net.minecraft.core.Global;
import net.minecraft.core.util.helper.Side;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import turniplabs.halplibe.util.GameStartEntrypoint;
import useless.dragonfly.debug.DebugMain;
import useless.dragonfly.model.block.adapters.CubeDataJsonAdapter;
import useless.dragonfly.model.block.adapters.FaceDataJsonAdapter;
import useless.dragonfly.model.block.adapters.ModelDataJsonAdapter;
import useless.dragonfly.model.block.adapters.PositionDataJsonAdapter;
import useless.dragonfly.model.block.adapters.RotationDataJsonAdapter;
import useless.dragonfly.model.block.data.CubeData;
import useless.dragonfly.model.block.data.FaceData;
import useless.dragonfly.model.block.data.ModelData;
import useless.dragonfly.model.block.data.PositionData;
import useless.dragonfly.model.block.data.RotationData;
import useless.dragonfly.model.entity.animation.Animation;
import useless.dragonfly.model.entity.animation.AnimationDeserializer;
public class DragonFly implements GameStartEntrypoint {
    public static final String MOD_ID = "dragonfly";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	public static final Gson GSON = new GsonBuilder()
		.registerTypeAdapter(Animation.class, new AnimationDeserializer())
		.registerTypeAdapter(ModelData.class, new ModelDataJsonAdapter())
		.registerTypeAdapter(PositionData.class, new PositionDataJsonAdapter())
		.registerTypeAdapter(CubeData.class, new CubeDataJsonAdapter())
		.registerTypeAdapter(FaceData.class, new FaceDataJsonAdapter())
		.registerTypeAdapter(RotationData.class, new RotationDataJsonAdapter())
		.create();
	public static final Side[] sides = new Side[]{Side.BOTTOM, Side.TOP, Side.NORTH, Side.SOUTH, Side.WEST, Side.EAST};
	public static double terrainAtlasWidth = TextureFX.tileWidthTerrain * Global.TEXTURE_ATLAS_WIDTH_TILES;
	public static String version;
	public static boolean isDev;
	public static String renderState = "gui";
	static {
		version = FabricLoader.getInstance().getModContainer(MOD_ID).get().getMetadata().getVersion().getFriendlyString();
		isDev = version.equals("${version}") || version.contains("dev");
	}
	@Override
	public void beforeGameStart() {
		if (isDev){
			LOGGER.info("DragonFly " + version + " loading debug assets");
			DebugMain.init();
		}
		LOGGER.info("DragonFly initialized.");
	}

	@Override
	public void afterGameStart() {

	}
}
