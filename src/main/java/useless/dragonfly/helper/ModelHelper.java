package useless.dragonfly.helper;

import com.google.gson.stream.JsonReader;
import useless.dragonfly.DragonFly;
import useless.dragonfly.model.block.data.ModelData;
import useless.dragonfly.model.block.processed.BlockModel;
import useless.dragonfly.model.blockstates.data.BlockStateData;
import useless.dragonfly.model.blockstates.data.ModelPart;
import useless.dragonfly.model.blockstates.data.VariantData;
import useless.dragonfly.model.entity.BenchEntityModel;
import useless.dragonfly.model.entity.processor.BenceEntityModelData;
import useless.dragonfly.utilities.NamespaceId;
import useless.dragonfly.utilities.Utilities;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class ModelHelper {
	public static final Map<NamespaceId, ModelData> modelDataFiles = new HashMap<>();
	public static final Map<NamespaceId, BlockModel> registeredModels = new HashMap<>();
	public static final Map<NamespaceId, BlockStateData> registeredBlockStates = new HashMap<>();
	public static final Map<NamespaceId, BenchEntityModel> registeredEntityModels = new HashMap<>();
	public static final Map<NamespaceId, BenceEntityModelData> entityDataFiles = new HashMap<>();

	/**
	 * Place mod models in the <i>assets/modid/model/block/</i> directory for them to be seen.
	 */
	public static BlockModel getOrCreateBlockModel(String modId, String modelSource) {
		NamespaceId namespaceId = new NamespaceId(modId, modelSource);
		if (registeredModels.containsKey(namespaceId)){
			return registeredModels.get(namespaceId);
		}
		BlockModel model = new BlockModel(namespaceId);
		registeredModels.put(namespaceId, model);
		return model;
	}
	/**
	 * Place mod models in the <i>assets/modid/blockstates/</i> directory for them to be seen.
	 */
	public static BlockStateData getOrCreateBlockState(String modId, String blockStateSource) {
		NamespaceId namespaceId = new NamespaceId(modId, blockStateSource);
		if (registeredBlockStates.containsKey(namespaceId)){
			return registeredBlockStates.get(namespaceId);
		}
		return createBlockState(namespaceId);
	}
	private static BlockStateData createBlockState(NamespaceId namespaceId){
		JsonReader reader = new JsonReader(new BufferedReader(new InputStreamReader(Utilities.getResourceAsStream(getBlockStateLocation(namespaceId)))));
		BlockStateData blockstateData = DragonFly.GSON.fromJson(reader, BlockStateData.class);
		registeredBlockStates.put(namespaceId, blockstateData);
		if (blockstateData.variants != null){
			for (ModelPart part : blockstateData.variants.values()) {
				for (VariantData variantData : part.apply){
					NamespaceId variantNamespaceId = NamespaceId.idFromString(variantData.model);
					getOrCreateBlockModel(variantNamespaceId.getNamespace(), variantNamespaceId.getId());
				}
			}
		}
		if (blockstateData.multipart != null){
			for (ModelPart part : blockstateData.multipart){
				for (VariantData variantData : part.apply){
					NamespaceId variantNamespaceId = NamespaceId.idFromString(variantData.model);
					getOrCreateBlockModel(variantNamespaceId.getNamespace(), variantNamespaceId.getId());
				}
			}
		}
		return blockstateData;
	}
	public static ModelData loadBlockModel(NamespaceId namespaceId){
		if (modelDataFiles.containsKey(namespaceId)){
			return modelDataFiles.get(namespaceId);
		}
		return Objects.requireNonNull(createBlockModel(namespaceId));
	}
	private static ModelData createBlockModel(NamespaceId namespaceId){
		JsonReader reader = new JsonReader(new BufferedReader(new InputStreamReader(Utilities.getResourceAsStream(getModelLocation(namespaceId)))));
		ModelData modelData = DragonFly.GSON.fromJson(reader, ModelData.class);
		modelDataFiles.put(namespaceId, modelData);
		return modelData;
	}

	/**
	 * Place mod models in the <i>assets/modid/model/</i> directory for them to be seen.
	 */
	public static BenchEntityModel getOrCreateEntityModel(String modID, String modelSource, Class<? extends BenchEntityModel> baseModel) {
		NamespaceId namespaceId = new NamespaceId(modID, modelSource);
		if (registeredEntityModels.containsKey(namespaceId)){
			return registeredEntityModels.get(namespaceId);
		}
        BenchEntityModel model;
        try {
            model = baseModel.getDeclaredConstructor().newInstance();
			model.geometry = loadEntityData(namespaceId);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
        model.deco();
		registeredEntityModels.put(namespaceId, model);
		return model;
	}
	public static BenceEntityModelData loadEntityData(NamespaceId namespaceId){
		if (entityDataFiles.containsKey(namespaceId)){
			return entityDataFiles.get(namespaceId);
		}
		return createEntityData(namespaceId);
	}
	private static BenceEntityModelData createEntityData(NamespaceId namespaceId){
		JsonReader reader = new JsonReader(new BufferedReader(new InputStreamReader(Utilities.getResourceAsStream(getModelLocation(namespaceId)))));
		BenceEntityModelData modelData = DragonFly.GSON.fromJson(reader, BenceEntityModelData.class);
		entityDataFiles.put(namespaceId, modelData);
		return modelData;
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
	public static void refreshModels(){
		Set<NamespaceId> blockModelDataKeys = new HashSet<>(modelDataFiles.keySet());
		Set<NamespaceId> blockStateKeys = new HashSet<>(registeredBlockStates.keySet());
		Set<NamespaceId> entityDataKeys = new HashSet<>(entityDataFiles.keySet());
		Set<NamespaceId> entityModelKeys = new HashSet<>(registeredEntityModels.keySet());

		entityDataFiles.clear();

		for (NamespaceId modelDataKey : blockModelDataKeys){
			createBlockModel(modelDataKey);
		}
		for (BlockModel model : registeredModels.values()){
			model.refreshModel();
		}
		for (NamespaceId stateKey : blockStateKeys){
			createBlockState(stateKey);
		}
		for (NamespaceId key : entityDataKeys){
			createEntityData(key);
		}
		for (NamespaceId key : entityModelKeys){
			BenchEntityModel model = registeredEntityModels.get(key);
			model.getIndexBones().clear();
			model.geometry = loadEntityData(key);
			model.deco();
		}
	}
}
