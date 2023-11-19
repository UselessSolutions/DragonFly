package useless.dragonfly.helper;

import com.google.gson.stream.JsonReader;
import useless.dragonfly.DragonFly;
import useless.dragonfly.model.block.data.ModelData;
import useless.dragonfly.model.block.processed.BlockModel;
import useless.dragonfly.model.entity.BenchEntityModel;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class ModelHelper {
	public static final Map<String, ModelData> modelDataFiles = new HashMap<>();
	public static final Map<String, BlockModel> registeredModels = new HashMap<>();
	public static HashMap<String, BenchEntityModel> benchEntityModelMap = new HashMap<>();

	/**
	 * Place mod models in the <i>assets/modid/item/</i> directory for them to be seen.
	 */
	public static BlockModel getOrCreateBlockModel(String modId, String modelSource) {
		String modelKey = ModelHelper.getModelLocation(modId, modelSource);
		if (registeredModels.containsKey(modelKey)){
			return registeredModels.get(modelKey);
		}
		BlockModel model = new BlockModel(loadBlockModel(modId, modelSource));
		registeredModels.put(modelKey, model);
		return model;
	}

	public static ModelData loadBlockModel(String modId, String modelSource){
		String modelKey = getModelLocation(modId, modelSource);
		if (modelDataFiles.containsKey(modelKey)){
			return modelDataFiles.get(modelKey);
		}
		InputStream inputStream = BlockModel.class.getResourceAsStream(modelKey);
		if (inputStream == null){
			throw new NullPointerException("resource " + modelKey + " returns null! Does this file exist?");
		}
		JsonReader reader = new JsonReader(new BufferedReader(new InputStreamReader(inputStream)));
		ModelData modelData = DragonFly.GSON.fromJson(reader, ModelData.class);
		modelDataFiles.put(modelKey, modelData);
		return modelData;
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
		if (!modelSource.contains(".json")){
			modelSource += ".json";
		}
		return "/assets/" + modID + "/model/" + modelSource;
	}
}
