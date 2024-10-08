package org.useless.dragonfly;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.render.stitcher.TextureRegistry;
import net.minecraft.core.util.helper.Side;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.useless.dragonfly.model.block.adapters.*;
import org.useless.dragonfly.model.block.data.*;
import org.useless.dragonfly.model.blockstates.adapters.BlockStateJsonAdapter;
import org.useless.dragonfly.model.blockstates.adapters.ModelPartJsonAdapter;
import org.useless.dragonfly.model.blockstates.adapters.VariantDataJsonAdapter;
import org.useless.dragonfly.model.blockstates.data.BlockstateData;
import org.useless.dragonfly.model.blockstates.data.ModelPart;
import org.useless.dragonfly.model.blockstates.data.VariantData;
import org.useless.dragonfly.model.entity.adapters.*;
import org.useless.dragonfly.model.entity.animation.Animation;
import org.useless.dragonfly.model.entity.processor.BenchEntityBones;
import org.useless.dragonfly.model.entity.processor.BenchEntityCube;
import org.useless.dragonfly.model.entity.processor.BenchEntityGeometry;
import org.useless.dragonfly.model.entity.processor.BenchEntityModelData;
import org.useless.dragonfly.utilities.vector.Vector3f;
import org.useless.dragonfly.utilities.vector.Vector3fJsonAdapter;
import turniplabs.halplibe.HalpLibe;
import turniplabs.halplibe.util.GameStartEntrypoint;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URISyntaxException;

public class DragonFly implements GameStartEntrypoint {
    public static final String MOD_ID = "dragonfly";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	public static final Gson GSON = new GsonBuilder()
		.registerTypeAdapter(Vector3f.class, new Vector3fJsonAdapter())
		.registerTypeAdapter(Animation.class, new AnimationDeserializer())
		.registerTypeAdapter(ModelData.class, new ModelDataJsonAdapter())
		.registerTypeAdapter(PositionData.class, new PositionDataJsonAdapter())
		.registerTypeAdapter(CubeData.class, new CubeDataJsonAdapter())
		.registerTypeAdapter(FaceData.class, new FaceDataJsonAdapter())
		.registerTypeAdapter(RotationData.class, new RotationDataJsonAdapter())
		.registerTypeAdapter(ModelPart.class, new ModelPartJsonAdapter())
		.registerTypeAdapter(VariantData.class, new VariantDataJsonAdapter())
		.registerTypeAdapter(BlockstateData.class, new BlockStateJsonAdapter())
		.registerTypeAdapter(BenchEntityModelData.class, new BenchEntityDataJsonAdapter())
		.registerTypeAdapter(BenchEntityGeometry.class, new BenchEntityGeometryJsonAdapter())
		.registerTypeAdapter(BenchEntityBones.class, new BenchEntityBonesJsonAdapter())
		.registerTypeAdapter(BenchEntityCube.class, new BenchEntityCubeJsonAdapter())
		.create();
	public static final Side[] sides = new Side[]{Side.BOTTOM, Side.TOP, Side.NORTH, Side.SOUTH, Side.WEST, Side.EAST};
	public static String version;
	public static boolean isDev;
	public static String renderState = "gui";
	static {
		version = FabricLoader.getInstance().getModContainer(MOD_ID).get().getMetadata().getVersion().getFriendlyString();
		isDev = version.equals("${version}") || version.contains("dev");
	}
	@Override
	public void beforeGameStart() {

	}

	@Override
	public void afterGameStart() {
		if (isDev){
			if (HalpLibe.isClient){
				try {
					TextureRegistry.initializeAllFiles(MOD_ID, TextureRegistry.blockAtlas, true);
				} catch (URISyntaxException | IOException | NullPointerException e) {
					LOGGER.error("Failed to initialize files!", e);
				}
			}
			LOGGER.info("DragonFly " + version + " loading debug assets");
			try {
				Class<?> debug = Class.forName("org.useless.dragonfly.debug.DebugMain");
				debug.getMethod("init").invoke(null);
			} catch (ClassNotFoundException | InvocationTargetException | IllegalAccessException | NoSuchMethodException e) {
				LOGGER.warn("DragonFly " + version + " failed to find debug assets!", e);
			}
		}
		LOGGER.info("DragonFly initialized.");
		//DebugEntities.init();
	}
}
