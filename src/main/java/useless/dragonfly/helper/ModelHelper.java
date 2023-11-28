package useless.dragonfly.helper;

import com.google.gson.stream.JsonReader;
import useless.dragonfly.DragonFly;
import useless.dragonfly.model.block.data.ModelData;
import useless.dragonfly.model.block.processed.BlockModel;
import useless.dragonfly.model.blockstates.data.BlockstateData;
import useless.dragonfly.model.blockstates.data.VariantData;
import useless.dragonfly.model.entity.BenchEntityModel;
import useless.dragonfly.utilities.NamespaceId;
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
		NamespaceId namespaceId = new NamespaceId(modId, modelSource);
		String modelKey = ModelHelper.getModelLocation(namespaceId);
		if (registeredModels.containsKey(modelKey)){
			return registeredModels.get(modelKey);
		}
		BlockModel model = new BlockModel(loadBlockModel(namespaceId));
		registeredModels.put(modelKey, model);
		return model;
	}
	/**
	 * Place mod models in the <i>assets/modid/blockstates/</i> directory for them to be seen.
	 */
	public static BlockstateData getOrCreateBlockState(String modId, String blockStateSource) {
		NamespaceId namespaceId = new NamespaceId(modId, blockStateSource);
		String modelKey = ModelHelper.getBlockStateLocation(namespaceId);
		if (registeredBlockStates.containsKey(modelKey)){
			return registeredBlockStates.get(modelKey);
		}
		JsonReader reader = new JsonReader(new BufferedReader(new InputStreamReader(Utilities.getResourceAsStream(modelKey))));
		BlockstateData blockstateData = DragonFly.GSON.fromJson(reader, BlockstateData.class);
		registeredBlockStates.put(modelKey, blockstateData);
		for (VariantData variant : blockstateData.variants.values()) {
			NamespaceId variantNamespaceId = NamespaceId.namespaceFromFormattedString(variant.model);
			getOrCreateBlockModel(variantNamespaceId.getNamespace(), variantNamespaceId.getId());
		}
		return blockstateData;
	}

	public static ModelData loadBlockModel(NamespaceId namespaceId){
		String modelKey = getModelLocation(namespaceId);
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
		String entityModelKey = ModelHelper.getModelLocation(new NamespaceId(modID, modelSource));

		if (benchEntityModelMap.containsKey(entityModelKey)){
			return benchEntityModelMap.get(entityModelKey);
		}

		JsonReader reader = new JsonReader(new BufferedReader(new InputStreamReader(Utilities.getResourceAsStream(entityModelKey))));
		BenchEntityModel model = DragonFly.GSON.fromJson(reader, baseModel);
		benchEntityModelMap.put(entityModelKey, model);
		return model;
	}
	public static String getModelLocation(NamespaceId namespaceId){
		String modelSource = namespaceId.getId();
		if (!modelSource.contains(".json")){
			modelSource += ".json";
		}
		return "/assets/" + namespaceId.getNamespace() + "/model/" + modelSource;
	}
	public static String getBlockStateLocation(NamespaceId namespaceId){
		String modelSource = namespaceId.getId();
		if (!modelSource.contains(".json")){
			modelSource += ".json";
		}
		return "/assets/" + namespaceId.getNamespace() + "/blockstates/" + modelSource;
	}
}
