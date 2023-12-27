package useless.dragonfly.helper;

import com.google.gson.stream.JsonReader;
import net.minecraft.core.item.Item;
import useless.dragonfly.DragonFly;
import useless.dragonfly.model.block.data.ModelData;
import useless.dragonfly.model.block.processed.BlockModel;
import useless.dragonfly.model.blockstates.data.BlockstateData;
import useless.dragonfly.model.blockstates.data.VariantData;
import useless.dragonfly.model.entity.BenchEntityModel;
import useless.dragonfly.model.item.data.ItemModelData;
import useless.dragonfly.utilities.NamespaceId;
import useless.dragonfly.utilities.Utilities;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class ModelHelper {
	public static final Map<String, ModelData> blockModelDataFiles = new HashMap<>();
	public static final Map<String, BlockModel> registeredBlockModels = new HashMap<>();
	public static final Map<String, BlockstateData> registeredBlockStates = new HashMap<>();
	public static HashMap<String, BenchEntityModel> benchEntityModelMap = new HashMap<>();
	public static HashMap<String, ItemModelData> itemModelDataFiles = new HashMap<>();
	public static HashMap<Item, ItemModelData> itemToModelMap = new HashMap<>();

	/**
	 * Place mod models in the <i>assets/modid/model/item/</i> directory for them to be seen.
	 */
	public static ItemModelData getOrCreateItemModel(String modId, String modelSource) {
		String itemModelKey = ModelHelper.getModelLocation(new NamespaceId(modId, modelSource));

		if (itemModelDataFiles.containsKey(itemModelKey)){
			return itemModelDataFiles.get(itemModelKey);
		}

		JsonReader reader = new JsonReader(new BufferedReader(new InputStreamReader(Utilities.getResourceAsStream(itemModelKey))));
		ItemModelData model = DragonFly.GSON.fromJson(reader, ItemModelData.class);
		itemModelDataFiles.put(itemModelKey, model);
		return model;
	}
	/**
	 * Place mod models in the <i>assets/modid/model/block/</i> directory for them to be seen.
	 */
	public static BlockModel getOrCreateBlockModel(String modId, String modelSource) {
		NamespaceId namespaceId = new NamespaceId(modId, modelSource);
		String modelKey = ModelHelper.getModelLocation(namespaceId);
		if (registeredBlockModels.containsKey(modelKey)){
			return registeredBlockModels.get(modelKey);
		}
		BlockModel model = new BlockModel(loadBlockModel(namespaceId));
		registeredBlockModels.put(modelKey, model);
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
			NamespaceId variantNamespaceId = NamespaceId.idFromString(variant.model);
			getOrCreateBlockModel(variantNamespaceId.getNamespace(), variantNamespaceId.getId());
		}
		return blockstateData;
	}

	public static ModelData loadBlockModel(NamespaceId namespaceId){
		String modelKey = getModelLocation(namespaceId);
		if (blockModelDataFiles.containsKey(modelKey)){
			return blockModelDataFiles.get(modelKey);
		}
		JsonReader reader = new JsonReader(new BufferedReader(new InputStreamReader(Utilities.getResourceAsStream(modelKey))));
		ModelData modelData = DragonFly.GSON.fromJson(reader, ModelData.class);
		blockModelDataFiles.put(modelKey, modelData);
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
	public static void setItemModel(Item item, ItemModelData modelData){
		itemToModelMap.put(item, modelData);
	}
	public static ItemModelData getItemModel(Item item){
		return itemToModelMap.get(item);
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
