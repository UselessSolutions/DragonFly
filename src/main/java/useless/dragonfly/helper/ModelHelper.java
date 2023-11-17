package useless.dragonfly.helper;

import com.google.gson.stream.JsonReader;
import net.minecraft.core.util.helper.Side;
import useless.dragonfly.DragonFly;
import useless.dragonfly.model.block.BenchCube;
import useless.dragonfly.model.block.BlockBenchModel;
import useless.dragonfly.model.entity.BenchEntityModel;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class ModelHelper {
	public static final Map<String, BlockBenchModel> registeredModels = new HashMap<>();
	public static HashMap<String, BenchEntityModel> benchEntityModelMap = new HashMap<>();

	/**
	 * Place mod models in the <i>assets/modid/item/</i> directory for them to be seen.
	 */
	public static BlockBenchModel getOrCreateBlockModel(String modId, String modelSource) {
		String modelKey = ModelHelper.getModelLocation(modId, modelSource);
		if (registeredModels.containsKey(modelKey)){
			return registeredModels.get(modelKey);
		}
		InputStream inputStream = BlockBenchModel.class.getResourceAsStream(modelKey);
		JsonReader reader = new JsonReader(new BufferedReader(new InputStreamReader(inputStream)));
		BlockBenchModel model = DragonFly.GSON.fromJson(reader, BlockBenchModel.class);
		model.hasFaceToRenderOnSide = new boolean[6];
		for (BenchCube cube: model.elements) {
			cube.process();
			for (int i = 0; i < model.hasFaceToRenderOnSide.length; i++) {
				model.hasFaceToRenderOnSide[i] |= cube.isOuterFace(Side.getSideById(i));
			}
		}
		for (BenchCube cube: model.elements) {
			cube.processVisibleFaces(model);
		}
		registeredModels.put(modelKey, model);
		return model;
	}
	/**
	 * Place mod models in the <i>assets/modid/item/</i> directory for them to be seen.
	 */
	public static BenchEntityModel getOrCreateEntityModel(String modID, String modelSource, Class<? extends BenchEntityModel> baseModel) {
		if (!benchEntityModelMap.containsKey(ModelHelper.getModelLocation(modID, modelSource))) {
			InputStream inputStream = baseModel.getResourceAsStream(ModelHelper.getModelLocation(modID, modelSource));
			JsonReader reader = new JsonReader(new BufferedReader(new InputStreamReader(inputStream)));
			BenchEntityModel model = DragonFly.GSON.fromJson(reader, baseModel);
			benchEntityModelMap.put(ModelHelper.getModelLocation(modID, modelSource), model);
			return model;
		} else {
			return benchEntityModelMap.get(ModelHelper.getModelLocation(modID, modelSource));
		}
	}
	public static String getModelLocation(String modID, String modelSource){
		return "/assets/" + modID + "/model/" + modelSource;
	}
}
