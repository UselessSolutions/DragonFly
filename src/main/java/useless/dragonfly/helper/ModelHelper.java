package useless.dragonfly.helper;

import com.google.gson.stream.JsonReader;
import useless.dragonfly.DragonFly;
import useless.dragonfly.model.block.data.ModelData;
import useless.dragonfly.model.block.processed.BlockModel;
import useless.dragonfly.model.blockstates.data.BlockstateData;
import useless.dragonfly.model.blockstates.data.VariantData;
import useless.dragonfly.model.entity.BenchEntityModel;
import useless.dragonfly.registries.TextureRegistry;
import useless.dragonfly.utilities.Utilities;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class ModelHelper {
	public static final Map<String, ModelData> modelDataFiles = new HashMap<>();
	public static final Map<String, BlockModel> registeredModels = new HashMap<>();
	public static final Map<String, BlockstateData> registeredBlockStates = new HashMap<>();
	public static HashMap<String, BenchEntityModel> benchEntityModelMap = new HashMap<>();

	/**
	 * Place mod models in the <i>assets/modid/model/block/</i> directory for them to be seen.
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
	/**
	 * Place mod models in the <i>assets/modid/blockstates/</i> directory for them to be seen.
	 */
	public static BlockstateData getOrCreateBlockState(String modId, String blockStateSource) {
		String modelKey = ModelHelper.getBlockStateLocation(modId, blockStateSource);
		if (registeredBlockStates.containsKey(modelKey)){
			return registeredBlockStates.get(modelKey);
		}
		JsonReader reader = new JsonReader(new BufferedReader(new InputStreamReader(Utilities.getResourceAsStream(modelKey))));
		BlockstateData blockstateData = DragonFly.GSON.fromJson(reader, BlockstateData.class);
		registeredBlockStates.put(modelKey, blockstateData);
		for (VariantData variant : blockstateData.variants.values()) {
			String[] modelID = variant.model.split(":");
			String namespace;
			String model;
			if (modelID.length < 2){
				namespace = TextureRegistry.coreNamepaceId;
				model = modelID[0];
			} else {
				namespace = modelID[0];
				model = modelID[1];
			}
			getOrCreateBlockModel(namespace, model);
		}
		return blockstateData;
	}

	public static ModelData loadBlockModel(String modId, String modelSource){
		String modelKey = getModelLocation(modId, modelSource);
		if (modelDataFiles.containsKey(modelKey)){
			return modelDataFiles.get(modelKey);
		}
		JsonReader reader = new JsonReader(new BufferedReader(new InputStreamReader(Utilities.getResourceAsStream(modelKey))));
		ModelData modelData = DragonFly.GSON.fromJson(reader, ModelData.class);
		modelDataFiles.put(modelKey, modelData);
		return modelData;
	}

	/**
	 * Place mod models in the <i>assets/modid/model/</i> directory for them to be seen.
	 */
	public static BenchEntityModel getOrCreateEntityModel(String modID, String modelSource, Class<? extends BenchEntityModel> baseModel) {
		String entityModelKey = ModelHelper.getModelLocation(modID, modelSource);

		if (benchEntityModelMap.containsKey(entityModelKey)){
			return benchEntityModelMap.get(entityModelKey);
		}

		JsonReader reader = new JsonReader(new BufferedReader(new InputStreamReader(Utilities.getResourceAsStream(entityModelKey))));
		BenchEntityModel model = DragonFly.GSON.fromJson(reader, baseModel);
		benchEntityModelMap.put(entityModelKey, model);
		return model;
	}
	public static String getModelLocation(String modID, String modelSource){
		if (!modelSource.contains(".json")){
			modelSource += ".json";
		}
		return "/assets/" + modID + "/model/" + modelSource;
	}
	public static String getBlockStateLocation(String modID, String modelSource){
		if (!modelSource.contains(".json")){
			modelSource += ".json";
		}
		return "/assets/" + modID + "/blockstates/" + modelSource;
	}
}
