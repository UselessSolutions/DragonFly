package useless.dragonfly.helper;

import com.google.gson.stream.JsonReader;
import net.minecraft.core.util.helper.Side;
import useless.dragonfly.DragonFly;
import useless.dragonfly.model.block.processed.BlockCube;
import useless.dragonfly.model.block.processed.BlockModel;
import useless.dragonfly.model.block.data.ModelData;
import useless.dragonfly.model.entity.BenchEntityModel;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class ModelHelper {
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
		InputStream inputStream = BlockModel.class.getResourceAsStream(modelKey);
		JsonReader reader = new JsonReader(new BufferedReader(new InputStreamReader(inputStream)));
		BlockModel model = new BlockModel(DragonFly.GSON.fromJson(reader, ModelData.class));
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
